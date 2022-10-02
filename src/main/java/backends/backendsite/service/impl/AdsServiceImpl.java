package backends.backendsite.service.impl;

import backends.backendsite.dto.AdsCommentDto;
import backends.backendsite.dto.AdsDto;
import backends.backendsite.dto.FullAdsDto;
import backends.backendsite.dto.ResponseWrapperDto;
import backends.backendsite.service.AdsService;
import org.springframework.stereotype.Service;

@Service
public class AdsServiceImpl implements AdsService {





    @Override
    public ResponseWrapperDto<AdsDto> getAllAds() {
        return null;
    }

    @Override
    public AdsDto addAds(AdsDto adsDto) {
        return null;
    }

    @Override
    public ResponseWrapperDto<AdsDto> getAdsMe() {
        return null;
    }

    @Override
    public ResponseWrapperDto<AdsCommentDto> getAdsComments(Integer ad_pk) {
        return null;
    }

    @Override
    public AdsCommentDto addAdsComment(Integer ad_pk) {
        return null;
    }

    @Override
    public void deleteAdsComment(Integer ad_pk, Integer id) {

    }

    @Override
    public AdsCommentDto getAdsComment(Integer ad_pk, Integer id) {
        return null;
    }

    @Override
    public AdsCommentDto updateAdsComment(Integer ad_pk, Integer id) {
        return null;
    }

    @Override
    public void removeAds(Integer id) {

    }

    @Override
    public FullAdsDto getAds(Integer id) {
        return null;
    }

    @Override
    public AdsDto updateAds(Integer id) {
        return null;
    }
}
