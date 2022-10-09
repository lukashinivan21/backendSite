package backends.backendsite.service.impl;

import backends.backendsite.dto.*;
import backends.backendsite.entities.Ads;
import backends.backendsite.entities.AdsComment;
import backends.backendsite.entities.SiteUser;
import backends.backendsite.mappers.AdsCommentMapper;
import backends.backendsite.mappers.SelfAdsMapper;
import backends.backendsite.repositories.AdsCommentRepository;
import backends.backendsite.repositories.AdsRepository;
import backends.backendsite.service.AdsService;
import backends.backendsite.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdsServiceImpl implements AdsService {

    Logger logger = LoggerFactory.getLogger(AdsServiceImpl.class);

    private final AdsRepository adsRepository;
    private final SelfAdsMapper selfAdsMapper;
    private final UserService userService;
    private final AdsCommentRepository adsCommentRepository;
    private final AdsCommentMapper commentMapper;

    public AdsServiceImpl(AdsRepository adsRepository, SelfAdsMapper selfAdsMapper, UserService userService,
                          AdsCommentRepository adsCommentRepository, AdsCommentMapper commentMapper) {
        this.adsRepository = adsRepository;
        this.selfAdsMapper = selfAdsMapper;
        this.userService = userService;
        this.adsCommentRepository = adsCommentRepository;
        this.commentMapper = commentMapper;
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
    public AdsDto addAds(CreateAdsDto adsDto) {
        logger.info("Create new ad");
        Ads ads = selfAdsMapper.fromCreateAdsDtoToAds(adsDto);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Integer id = userService.findUserByEmail(email).getId();
        ads.setAuthor(id);
        return selfAdsMapper.fromAdsToAdsDto(adsRepository.save(ads));
    }

    @Override
    public ResponseWrapperDto<AdsDto> getAdsMe(Integer price, String title, SiteUser user) {
        logger.info("Request for getting all ads of user with id {}", user.getId());
        List<Ads> adsMe = adsRepository.findByAuthorAndPriceAndTitle(user.getId(), price, title);
        List<AdsDto> result = adsMe.stream().map(selfAdsMapper::fromAdsToAdsDto).collect(Collectors.toList());
        ResponseWrapperDto<AdsDto> responseWrapperDto = new ResponseWrapperDto<>();
        responseWrapperDto.setList(result);
        responseWrapperDto.setCount(result.size());
        return responseWrapperDto;
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
    public AdsCommentDto addAdsComment(Integer adPk, String text) {
        logger.info("Request fod adding comment with text: \"{}\" to ad with id {}", text, adPk);
        Optional<Ads> adsOptional = adsRepository.findById(adPk);
        if (adsOptional.isEmpty()) {
            return null;
        } else {
            SiteUser siteUser = adsOptional.get().getSiteUser();
            AdsComment result = new AdsComment();
            result.setAuthor(siteUser.getId());
            result.setText(text);
            result.setCreatedAt(LocalDateTime.now());
            return commentMapper.fromAdsCommentToAdsCommentDto(adsCommentRepository.save(result));
        }
    }

    @Override
    public String deleteAdsComment(Integer adPk, Integer id) {
        logger.info("Request for deleting comment with id: {}", id);
        Optional<AdsComment> optionalAdsComment = adsCommentRepository.findById(id);
        if (optionalAdsComment.isEmpty()) {
            return null;
        } else {
            adsCommentRepository.delete(optionalAdsComment.get());
            return "SUCCESS";
        }
    }

    @Override
    public AdsCommentDto getAdsComment(Integer adPk, Integer id) {
        logger.info("Request for getting information about comment with id: {}", id);
        Optional<AdsComment> optionalAdsComment = adsCommentRepository.findById(id);
        if (optionalAdsComment.isEmpty()) {
            return null;
        } else {
            return commentMapper.fromAdsCommentToAdsCommentDto(optionalAdsComment.get());
        }
    }

    @Override
    public AdsCommentDto updateAdsComment(Integer adPk, Integer id, AdsCommentDto commentDto) {
        logger.info("Request for updating comment with id {}", id);
        Optional<AdsComment> optionalAdsComment = adsCommentRepository.findById(id);
        if (optionalAdsComment.isEmpty()) {
            return null;
        } else {
            AdsComment result = commentMapper.fromAdsCommentDtoToAdsComment(commentDto, optionalAdsComment.get());
            return commentMapper.fromAdsCommentToAdsCommentDto(adsCommentRepository.save(result));
        }
    }

    @Override
    public String removeAds(Integer id) {
        logger.info("Request for deleting ad with id {}", id);
        Optional<Ads> ads = adsRepository.findById(id);
        if (ads.isEmpty()) {
            return "Ad not found";
        } else {
            adsRepository.delete(ads.get());
            return String.format("Success deleting ad with id %d", id);
        }
    }

    @Override
    public FullAdsDto getAds(Integer id) {
        logger.info("Request for getting full info about ads with id {}", id);
        Optional<Ads> adsOptional = adsRepository.findById(id);
        if (adsOptional.isEmpty()) {
            return null;
        } else {
            Ads result = adsOptional.get();
            SiteUser adsOwner = result.getSiteUser();
            return selfAdsMapper.mapToFullAdsDto(result, adsOwner);
        }
    }

    @Override
    public AdsDto updateAds(Integer id, AdsDto adsDto) {
        logger.info("Request for updating ad with id {}", id);
        Optional<Ads> optionalAds = adsRepository.findById(id);
        if (optionalAds.isEmpty()) {
            return null;
        } else {
            Ads ads = optionalAds.get();
            Ads result = selfAdsMapper.fromAdsDtoToAds(adsDto, ads);
            return selfAdsMapper.fromAdsToAdsDto(adsRepository.save(result));
        }
    }


}
