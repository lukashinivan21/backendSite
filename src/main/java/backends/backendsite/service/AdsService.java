package backends.backendsite.service;

import backends.backendsite.dto.*;
import backends.backendsite.entities.Ads;

/**
 * Interface including methods for working with ads and comments
 */
public interface AdsService {

    /**
     * method for getting all ads from database
     * @return ResponseWrapperDto<AdsDto></AdsDto>
     */
    ResponseWrapperDto<AdsDto> getAllAds();

    /**
     * method for creating ads
     * @return AdsDto
     */
    AdsDto addAds(CreateAdsDto adsDto, String email);

    /**
     * Method for getting all ads of one user
     * @return ResponseWrapperDto<AdsDto></AdsDto>
     */
    ResponseWrapperDto<AdsDto> getAdsMe(Integer price, String title);

    /**
     * Method for getting all comments of one ad
     * @return ResponseWrapperDto<AdsCommentDto>
     */
    ResponseWrapperDto<AdsCommentDto> getAdsComments(Integer adPk);

    /**
     * Method for creating comment for one ad
     * @return AdsCommentDto
     */
    AdsCommentDto addAdsComment(Integer adPk, String text);

    /**
     * Method remove one comment of ad by ad id and comment id
     * @return String
     */
    String deleteAdsComment(Integer adPk, Integer id);

    /**
     * Method for getting one comment of ad by ad id and comment id
     * @return AdsCommentDto
     */
    AdsCommentDto getAdsComment(Integer adPk, Integer id);

    /**
     * Method for updating comment
     * @return AdsCommentDto
     */
    AdsCommentDto updateAdsComment(Integer adPk, Integer id, AdsCommentDto commentDto);

    /**
     * Method remove ad by id
     * @return String
     */
    String removeAds(Integer id);

    /**
     * Method for getting full info about ad by id
     * @return FullAdsDto
     */
    FullAdsDto getAds(Integer id);

    /**
     * Method for updating ad
     * @return AdsDto
     */
    AdsDto updateAds(Integer id, AdsDto adsDto);

    /**
     * Method for getting comments containing text
     * @return ResponseWrapperDto
     */
    ResponseWrapperDto<AdsCommentDto> getCommentWithText(String text);

    /**
     * Method for getting ads with title containing text
     * @return ResponseWrapperDto
     */
    ResponseWrapperDto<AdsDto> getAdsWithTitleContainsText(String text);

    /**
     * Method for getting ad by id
     * @return Ads
     */
    Ads getAdsByPk(Integer pk);



}
