package backends.backendsite.mappers;

import backends.backendsite.dto.AdsDto;
import backends.backendsite.dto.CreateAdsDto;
import backends.backendsite.dto.FullAdsDto;
import backends.backendsite.entities.Ads;
import backends.backendsite.entities.SiteUserDetails;

/**
 * Interface including methods for converting entity ads to ads dto (or full ads dto) or
 * converting create ads dto to ads entity
 */
public interface SelfAdsMapper {

    Ads fromAdsDtoToAds(AdsDto adsDto, Ads ads);

    AdsDto fromAdsToAdsDto(Ads ads);

    Ads fromCreateAdsDtoToAds(CreateAdsDto createAdsDto);

    FullAdsDto mapToFullAdsDto(Ads ads, SiteUserDetails userDetails);


}
