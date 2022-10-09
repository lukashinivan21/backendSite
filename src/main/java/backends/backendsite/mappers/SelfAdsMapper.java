package backends.backendsite.mappers;

import backends.backendsite.dto.AdsDto;
import backends.backendsite.dto.CreateAdsDto;
import backends.backendsite.dto.FullAdsDto;
import backends.backendsite.entities.Ads;
import backends.backendsite.entities.SiteUser;

public interface SelfAdsMapper {

    Ads fromAdsDtoToAds(AdsDto adsDto, Ads ads);

    AdsDto fromAdsToAdsDto(Ads ads);

    Ads fromCreateAdsDtoToAds(CreateAdsDto createAdsDto);

    FullAdsDto mapToFullAdsDto(Ads ads, SiteUser siteUser);


}
