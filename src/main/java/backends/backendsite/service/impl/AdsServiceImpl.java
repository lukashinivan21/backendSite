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
import java.util.*;
import java.util.stream.Collectors;

import static backends.backendsite.service.StringConstants.*;

/**
 * Class implements methods for working with ads and comments
 */
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

//    method for getting all ads from database
    @Override
    public ResponseWrapperDto<AdsDto> getAllAds() {
        logger.info("Request for getting all ads from data base");
        List<Ads> adsList = adsRepository.findAll();
        if (adsList.isEmpty()) {
            List<AdsDto> list = new ArrayList<>();
            ResponseWrapperDto<AdsDto> result = new ResponseWrapperDto<>();
            result.setResults(list);
            result.setCount(list.size());
            return result;
        }
        List<AdsDto> adsDtoList = new ArrayList<>();
        for (Ads ads : adsList) {
            adsDtoList.add(selfAdsMapper.fromAdsToAdsDto(ads));
        }
        ResponseWrapperDto<AdsDto> responseWrapperDto = new ResponseWrapperDto<>();
        responseWrapperDto.setResults(adsDtoList);
        responseWrapperDto.setCount(adsDtoList.size());
        return responseWrapperDto;
    }

//    Method for getting all ads of one user
    @Override
    public ResponseWrapperDto<AdsDto> getAdsMe(Integer price, String title) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Request for getting all ads of user with username {}", email);
        Optional<SiteUser> optionalSiteUser = siteUserRepository.findSiteUserByUsername(email);
        if (optionalSiteUser.isPresent()) {
            Integer idUser = optionalSiteUser.get().getSiteUserDetails().getId();
            List<AdsDto> list = adsRepository.findAdsBySiteUserDetailsId(idUser)
                    .stream()
                    .map(selfAdsMapper::fromAdsToAdsDto)
                    .collect(Collectors.toList());
            if (price != null && title == null) {
                list = adsRepository.findAllBySiteUserDetailsIdAndPrice(idUser, price)
                        .stream()
                        .map(selfAdsMapper::fromAdsToAdsDto)
                        .collect(Collectors.toList());
            }
            if (title != null && price == null) {
                list = adsRepository.findAllBySiteUserDetailsIdAndTitleContains(idUser, title)
                        .stream()
                        .map(selfAdsMapper::fromAdsToAdsDto)
                        .collect(Collectors.toList());
            }
            if (title != null && price != null) {
                list = adsRepository.findAllBySiteUserDetailsIdAndPriceAndTitleContains(idUser, price, title)
                        .stream()
                        .map(selfAdsMapper::fromAdsToAdsDto)
                        .collect(Collectors.toList());
            }
            ResponseWrapperDto<AdsDto> responseWrapperDto = new ResponseWrapperDto<>();
            responseWrapperDto.setResults(list);
            responseWrapperDto.setCount(list.size());
            return responseWrapperDto;
        } else {
            return null;
        }
    }


//    method for creating ads
    @Override
    public AdsDto addAds(CreateAdsDto adsDto, String email) {
        logger.info("Create new ad by user with username: {}", email);
//        Optional<SiteUser> siteUser = siteUserRepository.findSiteUserByUsername(email);
//        if (siteUser.isEmpty()) {
//            return null;
//        } else {
        SiteUser siteUser = siteUserRepository.findByUsername(email);
        Ads ads = selfAdsMapper.fromCreateAdsDtoToAds(adsDto);
        ads.setAuthor(siteUser.getSiteUserDetails().getId());
        ads.setSiteUserDetails(siteUser.getSiteUserDetails());
        return selfAdsMapper.fromAdsToAdsDto(adsRepository.save(ads));
//        }
    }

//     Method for creating comment for one ad
    @Override
    public AdsCommentDto addAdsComment(Integer adPk, String text) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Request for adding comment with text: \"{}\" to ad with id {} from user with username: {}", text, adPk, username);
        Optional<Ads> adsOptional = adsRepository.findById(adPk);
        if (adsOptional.isEmpty()) {
            return null;
        } else {
            SiteUser siteUser = siteUserRepository.findByUsername(username);
            AdsComment result = new AdsComment();
            result.setSiteUserDetails(siteUser.getSiteUserDetails());
            result.setAds(adsOptional.get());
            result.setAuthor(siteUser.getSiteUserDetails().getId());
            result.setText(text);
            result.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            return commentMapper.fromAdsCommentToAdsCommentDto(adsCommentRepository.save(result));
        }
    }


//    Method for getting all comments of one ad
    @Override
    public ResponseWrapperDto<AdsCommentDto> getAdsComments(Integer adPk) {
        logger.info("Request for getting all comments of ad with id {}", adPk);
        List<AdsComment> list = adsCommentRepository.findAdsCommentsByAds_Id(adPk);
        if (list.isEmpty()) {
            return null;
        } else {
            List<AdsCommentDto> result = list.stream().map(commentMapper::fromAdsCommentToAdsCommentDto).collect(Collectors.toList());
            ResponseWrapperDto<AdsCommentDto> responseWrapperDto = new ResponseWrapperDto<>();
            responseWrapperDto.setResults(result);
            responseWrapperDto.setCount(result.size());
            return responseWrapperDto;
        }
    }


//     Method for getting one comment of ad by ad id and comment id
    @Override
    public AdsCommentDto getAdsComment(Integer adPk, Integer id) {
        logger.info("Request for getting information about comment with id: {} of ads with id: {}", id, adPk);
        Optional<Ads> optionalAds = adsRepository.findById(adPk);
        if (optionalAds.isEmpty()) {
            return null;
        } else {
            List<AdsComment> commentList = optionalAds.get().getComments();
            Optional<AdsComment> optionalAdsComment = adsCommentRepository.findById(id);
            if (optionalAdsComment.isEmpty() || commentList.isEmpty() || !commentList.contains(optionalAdsComment.get())) {
                return null;
            } else {
                return commentMapper.fromAdsCommentToAdsCommentDto(optionalAdsComment.get());
            }
        }
    }


//    Method remove ad by id
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
                return NOT_ACCESS;
            } else {
                adsRepository.delete(deletedAds);
                return SUCCESS;
            }
        }
    }


//     Method remove one comment of ad by ad id and comment id
    @Override
    public String deleteAdsComment(Integer adPk, Integer id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = authorityRepository.findAuthorityByUsername(email).getAuthority();
        logger.info("Request from user with username: \"{}\" for deleting comment with id: {}", email, id);
        Optional<Ads> adsOptional = adsRepository.findById(adPk);
        if (adsOptional.isEmpty()) {
            return NOT_FOUND;
        } else {
            List<AdsComment> commentList = adsOptional.get().getComments();
            Optional<AdsComment> optionalAdsComment = adsCommentRepository.findById(id);
            if (commentList.isEmpty() || optionalAdsComment.isEmpty() || !commentList.contains(optionalAdsComment.get())) {
                return NOT_FOUND;
            } else {
                if (role.equals(USER) && !optionalAdsComment.get().getSiteUserDetails().getSiteUser().getUsername().equals(email)) {
                    return NOT_ACCESS;
                } else {
                    adsCommentRepository.delete(optionalAdsComment.get());
                    return SUCCESS;
                }
            }
        }
    }


//    Method for getting full info about ad by id
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
                fullAdsDto.setTitle(NOT_ACCESS);
                return fullAdsDto;
            } else {
                return selfAdsMapper.mapToFullAdsDto(result, result.getSiteUserDetails());
            }
        }
    }


//    Method for updating ad
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


//    Method for updating comment
    @Override
    public AdsCommentDto updateAdsComment(Integer adPk, Integer id, AdsCommentDto commentDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = authorityRepository.findAuthorityByUsername(email).getAuthority();
        logger.info("Request from user with username: \"{}\" for updating comment with id {}", email, id);
        Optional<Ads> adsOptional = adsRepository.findById(adPk);
        if (adsOptional.isEmpty()) {
            return null;
        } else {
            List<AdsComment> commentList = adsOptional.get().getComments();
            Optional<AdsComment> optionalAdsComment = adsCommentRepository.findById(id);
            if (commentList.isEmpty() || optionalAdsComment.isEmpty() || !commentList.contains(optionalAdsComment.get())) {
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
    }


//    Method for getting comments containing text
    @Override
    public ResponseWrapperDto<AdsCommentDto> getCommentWithText(String text) {
        List<AdsComment> result = adsCommentRepository.findAdsCommentsByTextContains(text);
        if (result.isEmpty()) {
            return null;
        } else {
            List<AdsCommentDto> list = result.stream().map(commentMapper::fromAdsCommentToAdsCommentDto).collect(Collectors.toList());
            ResponseWrapperDto<AdsCommentDto> responseWrapperDto = new ResponseWrapperDto<>();
            responseWrapperDto.setResults(list);
            responseWrapperDto.setCount(list.size());
            return responseWrapperDto;
        }
    }


//     Method for getting ads with title containing text
    @Override
    public ResponseWrapperDto<AdsDto> getAdsWithTitleContainsText(String text) {
        List<Ads> adsList = adsRepository.findAdsByTitleContains(text);
        if (adsList.isEmpty()) {
            return null;
        } else {
            List<AdsDto> list = adsList.stream().map(selfAdsMapper::fromAdsToAdsDto).collect(Collectors.toList());
            ResponseWrapperDto<AdsDto> result = new ResponseWrapperDto<>();
            result.setResults(list);
            result.setCount(list.size());
            return result;
        }
    }


//    Method for getting ad by id
    @Override
    public Ads getAdsByPk(Integer pk) {
        return adsRepository.findAdsById(pk);
    }
}
