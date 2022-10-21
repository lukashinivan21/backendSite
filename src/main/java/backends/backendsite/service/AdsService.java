package backends.backendsite.service;

import backends.backendsite.dto.*;
import backends.backendsite.entities.Ads;
import backends.backendsite.entities.SiteUser;

public interface AdsService {

    ResponseWrapperDto<AdsDto> getAllAds();

    AdsDto addAds(CreateAdsDto adsDto, String email);

    ResponseWrapperDto<AdsDto> getAdsMe(Integer price, String title, SiteUser user);

    ResponseWrapperDto<AdsCommentDto> getAdsComments(Integer adPk);

    AdsCommentDto addAdsComment(Integer adPk, String text);

    String deleteAdsComment(Integer adPk, Integer id);

    AdsCommentDto getAdsComment(Integer adPk, Integer id);

    AdsCommentDto updateAdsComment(Integer adPk, Integer id, AdsCommentDto commentDto);

    String removeAds(Integer id);

    FullAdsDto getAds(Integer id);

    AdsDto updateAds(Integer id, AdsDto adsDto);

    ResponseWrapperDto<AdsCommentDto> getCommentWithText(String text);

    ResponseWrapperDto<AdsDto> getAdsWithTitleContainsText(String text);

    Ads getAdsByPk(Integer pk);



}
