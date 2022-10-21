package backends.backendsite.service.impl;

import backends.backendsite.dto.*;
import backends.backendsite.entities.Ads;
import backends.backendsite.entities.AdsComment;
import backends.backendsite.entities.SiteUser;
import backends.backendsite.mappers.AdsCommentMapper;
import backends.backendsite.mappers.SelfAdsMapper;
import backends.backendsite.repositories.AdsCommentRepository;
import backends.backendsite.repositories.AdsRepository;
import backends.backendsite.repositories.AuthorityRepository;
import backends.backendsite.repositories.SiteUserRepository;
import backends.backendsite.service.AdsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static backends.backendsite.service.StringConstants.*;

@Service
public class AdsServiceImpl implements AdsService {

    Logger logger = LoggerFactory.getLogger(AdsServiceImpl.class);

    private final AdsRepository adsRepository;
    private final SelfAdsMapper selfAdsMapper;
    private final AdsCommentRepository adsCommentRepository;
    private final AdsCommentMapper commentMapper;
    private final SiteUserRepository siteUserRepository;
    private final AuthorityRepository authorityRepository;

    public AdsServiceImpl(AdsRepository adsRepository, SelfAdsMapper selfAdsMapper, AdsCommentRepository adsCommentRepository,
                          AdsCommentMapper commentMapper, SiteUserRepository siteUserRepository, AuthorityRepository authorityRepository) {
        this.adsRepository = adsRepository;
        this.selfAdsMapper = selfAdsMapper;
        this.adsCommentRepository = adsCommentRepository;
        this.commentMapper = commentMapper;
        this.siteUserRepository = siteUserRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public ResponseWrapperDto<AdsDto> getAllAds() {
        logger.info("Request for getting all ads from data base");
        List<Ads> adsList = adsRepository.findAll();
        List<AdsDto> adsDtoList = new ArrayList<>();
        for (Ads ads : adsList) {
            adsDtoList.add(selfAdsMapper.fromAdsToAdsDto(ads));
        }
        ResponseWrapperDto<AdsDto> responseWrapperDto = new ResponseWrapperDto<>();
        responseWrapperDto.setList(adsDtoList);
        responseWrapperDto.setCount(adsDtoList.size());
        return responseWrapperDto;
    }

    @Override
    public ResponseWrapperDto<AdsDto> getAdsMe(Integer price, String title, SiteUser user) {
        logger.info("Request for getting all ads of user with id {}", user.getSiteUserDetails().getId());
        List<Ads> adsMe = adsRepository.findByAuthorAndPriceAndTitle(user.getSiteUserDetails().getId(), price, title);
        List<AdsDto> result = adsMe.stream().map(selfAdsMapper::fromAdsToAdsDto).collect(Collectors.toList());
        ResponseWrapperDto<AdsDto> responseWrapperDto = new ResponseWrapperDto<>();
        responseWrapperDto.setList(result);
        responseWrapperDto.setCount(result.size());
        return responseWrapperDto;
    }


    @Override
    public AdsDto addAds(CreateAdsDto adsDto, String email) {
        logger.info("Create new ad by user with username: {}", email);
        Optional<SiteUser> siteUser = siteUserRepository.findSiteUserByUsername(email);
        if (siteUser.isEmpty()) {
            return null;
        } else {
            Ads ads = selfAdsMapper.fromCreateAdsDtoToAds(adsDto);
            ads.setAuthor(siteUser.get().getSiteUserDetails().getId());
            ads.setSiteUserDetails(siteUser.get().getSiteUserDetails());
            return selfAdsMapper.fromAdsToAdsDto(adsRepository.save(ads));
        }
    }

    @Override
    public AdsCommentDto addAdsComment(Integer adPk, String text) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Request for adding comment with text: \"{}\" to ad with id {} from user with username: {}", text, adPk, username);
        Optional<Ads> adsOptional = adsRepository.findById(adPk);
        if (adsOptional.isEmpty()) {
            return null;
        } else {
            Optional<SiteUser> siteUser = siteUserRepository.findSiteUserByUsername(username);
            if (siteUser.isPresent()) {
                AdsComment result = new AdsComment();
                result.setSiteUserDetails(siteUser.get().getSiteUserDetails());
                result.setAds(adsOptional.get());
                result.setAuthor(siteUser.get().getSiteUserDetails().getId());
                result.setText(text);
                result.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
                return commentMapper.fromAdsCommentToAdsCommentDto(adsCommentRepository.save(result));
            } else {
                return null;
            }
        }
    }

    @Override
    public ResponseWrapperDto<AdsCommentDto> getAdsComments(Integer adPk) {
        logger.info("Request for getting all comments of ad with id {}", adPk);
        List<AdsComment> list = adsCommentRepository.findAdsCommentsByAds_Pk(adPk);
        if (list.isEmpty()) {
            return null;
        } else {
            List<AdsCommentDto> result = list.stream().map(commentMapper::fromAdsCommentToAdsCommentDto).collect(Collectors.toList());
            ResponseWrapperDto<AdsCommentDto> responseWrapperDto = new ResponseWrapperDto<>();
            responseWrapperDto.setList(result);
            responseWrapperDto.setCount(result.size());
            return responseWrapperDto;
        }
    }

    @Override
    public AdsCommentDto getAdsComment(Integer adPk, Integer id) {
        logger.info("Request for getting information about comment with id: {} of ads with id: {}", id, adPk);
        Optional<Ads> optionalAds = adsRepository.findById(adPk);
        if (optionalAds.isEmpty()) {
            return null;
        }
        List<Integer> idsOfComments = adsCommentRepository.findAdsCommentsByAds_Pk(adPk)
                .stream()
                .map(AdsComment::getPk)
                .collect(Collectors.toList());
        if (!idsOfComments.contains(id)) {
            return null;
        }
        Optional<AdsComment> optionalAdsComment = adsCommentRepository.findById(id);
        if (optionalAdsComment.isEmpty()) {
            return null;
        } else {
            return commentMapper.fromAdsCommentToAdsCommentDto(optionalAdsComment.get());
        }
    }


    @Override
    public String removeAds(Integer id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = authorityRepository.findAuthorityByUsername(email).getAuthority();
        logger.info("Request from user with username: \"{}\" for deleting ad with id {}", email, id);
        Optional<Ads> ads = adsRepository.findById(id);
        if (ads.isEmpty()) {
            return NOT_FOUND;
        } else {
            Ads deletedAds = ads.get();
            if (role.equals(USER) && !deletedAds.getSiteUserDetails().getSiteUser().getUsername().equals(email)) {
                return HAVE_NOT;
            } else {
                adsRepository.delete(deletedAds);
                return SUCCESS;
            }
        }
    }


    @Override
    public String deleteAdsComment(Integer adPk, Integer id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = authorityRepository.findAuthorityByUsername(email).getAuthority();
        logger.info("Request from user with username: \"{}\" for deleting comment with id: {}", email, id);
        Optional<Ads> adsOptional = adsRepository.findById(adPk);
        if (adsOptional.isEmpty()) {
            return null;
        }
        List<Integer> idsOfAdsCommentList = adsCommentRepository.findAdsCommentsByAds_Pk(adPk)
                .stream()
                .map(AdsComment::getPk)
                .collect(Collectors.toList());
        if (!idsOfAdsCommentList.contains(id)) {
            return null;
        }
        Optional<AdsComment> optionalAdsComment = adsCommentRepository.findById(id);
        if (optionalAdsComment.isEmpty()) {
            return null;
        } else {
            if (role.equals(USER) && !optionalAdsComment.get().getSiteUserDetails().getSiteUser().getUsername().equals(email)) {
                return HAVE_NOT;
            } else {
                adsCommentRepository.delete(optionalAdsComment.get());
                return SUCCESS;
            }
        }
    }


    @Override
    public FullAdsDto getAds(Integer id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = authorityRepository.findAuthorityByUsername(email).getAuthority();
        logger.info("Request from user with username: \"{}\" for getting full info about ads with id {}", email, id);
        Optional<Ads> adsOptional = adsRepository.findById(id);
        if (adsOptional.isEmpty()) {
            return null;
        } else {
            Ads result = adsOptional.get();
            if (role.equals(USER) && !result.getSiteUserDetails().getSiteUser().getUsername().equals(email)) {
                FullAdsDto fullAdsDto = new FullAdsDto();
                fullAdsDto.setTitle(HAVE_NOT);
                return fullAdsDto;
            } else {
                return selfAdsMapper.mapToFullAdsDto(result, result.getSiteUserDetails());
            }
        }
    }

    @Override
    public AdsDto updateAds(Integer id, AdsDto adsDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = authorityRepository.findAuthorityByUsername(username).getAuthority();
        logger.info("Request for updating ad with id {} from user with username: \"{}\"", id, username);
        Optional<Ads> optionalAds = adsRepository.findById(id);
        if (optionalAds.isEmpty()) {
            return null;
        } else {
            if (role.equals(USER) && !optionalAds.get().getSiteUserDetails().getSiteUser().getUsername().equals(username)) {
                AdsDto answer = new AdsDto();
                answer.setTitle(NOT_ACCESS);
                return answer;
            } else {
                Ads result = selfAdsMapper.fromAdsDtoToAds(adsDto, optionalAds.get());
                return selfAdsMapper.fromAdsToAdsDto(adsRepository.save(result));
            }
        }
    }

    @Override
    public AdsCommentDto updateAdsComment(Integer adPk, Integer id, AdsCommentDto commentDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = authorityRepository.findAuthorityByUsername(email).getAuthority();
        logger.info("Request from user with username: \"{}\" for updating comment with id {}", email, id);
        Optional<AdsComment> optionalAdsComment = adsCommentRepository.findById(id);
        if (optionalAdsComment.isEmpty()) {
            return null;
        } else {
            if (role.equals(USER) && !optionalAdsComment.get().getSiteUserDetails().getSiteUser().getUsername().equals(email)) {
                AdsCommentDto answer = new AdsCommentDto();
                answer.setText(NOT_ACCESS);
                return answer;
            } else {
                AdsComment result = commentMapper.fromAdsCommentDtoToAdsComment(commentDto, optionalAdsComment.get());
                return commentMapper.fromAdsCommentToAdsCommentDto(adsCommentRepository.save(result));
            }
        }
    }

    @Override
    public ResponseWrapperDto<AdsCommentDto> getCommentWithText(String text) {
        List<AdsComment> result = adsCommentRepository.findAdsCommentsByTextContains(text);
        if (result.isEmpty()) {
            return null;
        } else {
            List<AdsCommentDto> list = result.stream().map(commentMapper::fromAdsCommentToAdsCommentDto).collect(Collectors.toList());
            ResponseWrapperDto<AdsCommentDto> responseWrapperDto = new ResponseWrapperDto<>();
            responseWrapperDto.setList(list);
            responseWrapperDto.setCount(list.size());
            return responseWrapperDto;
        }
    }

    @Override
    public ResponseWrapperDto<AdsDto> getAdsWithTitleContainsText(String text) {
        List<Ads> adsList = adsRepository.findAdsByTitleContains(text);
        if (adsList.isEmpty()) {
            return null;
        } else {
            List<AdsDto> list = adsList.stream().map(selfAdsMapper::fromAdsToAdsDto).collect(Collectors.toList());
            ResponseWrapperDto<AdsDto> result = new ResponseWrapperDto<>();
            result.setList(list);
            result.setCount(list.size());
            return result;
        }
    }

    @Override
    public Ads getAdsByPk(Integer pk) {
        return adsRepository.findAdsByPk(pk);
    }
}
