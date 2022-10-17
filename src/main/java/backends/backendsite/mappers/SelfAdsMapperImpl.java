package backends.backendsite.mappers;

import backends.backendsite.dto.AdsDto;
import backends.backendsite.dto.CreateAdsDto;
import backends.backendsite.dto.FullAdsDto;
import backends.backendsite.entities.Ads;
import backends.backendsite.entities.SiteUser;
import backends.backendsite.entities.SiteUserDetails;
import org.springframework.stereotype.Service;

@Service
public class SelfAdsMapperImpl implements SelfAdsMapper{

    @Override
    public Ads fromAdsDtoToAds(AdsDto adsDto, Ads ads) {
        if (!adsDto.getTitle().equals("")) {
            ads.setTitle(adsDto.getTitle());
        }
        if (!adsDto.getImage().equals("")) {
            ads.setImage(adsDto.getImage());
        }
        if (adsDto.getPrice() != null) {
            ads.setPrice(ads.getPrice());
        }
        return ads;
    }

    @Override
    public AdsDto fromAdsToAdsDto(Ads ads) {
        AdsDto adsDto = new AdsDto();
        adsDto.setPk(ads.getPk());
        adsDto.setTitle(ads.getTitle());
        adsDto.setPrice(ads.getPrice());
        adsDto.setImage(ads.getImage());
        adsDto.setAuthor(ads.getAuthor());
        return adsDto;
    }

    @Override
    public Ads fromCreateAdsDtoToAds(CreateAdsDto createAdsDto) {
        Ads ads = new Ads();
        ads.setTitle(createAdsDto.getTitle());
        ads.setDescription(createAdsDto.getDescription());
        ads.setPrice(createAdsDto.getPrice());
        ads.setImage(createAdsDto.getImage());
        return ads;
    }

    @Override
    public FullAdsDto mapToFullAdsDto(Ads ads, SiteUser siteUser, SiteUserDetails userDetails) {
        FullAdsDto result = new FullAdsDto();
        result.setPk(ads.getPk());
        result.setPrice(ads.getPrice());
        result.setTitle(ads.getTitle());
        result.setDescription(ads.getDescription());
        result.setImage(ads.getImage());
        result.setAuthorFirstName(userDetails.getFirstName());
        result.setAuthorLastName(userDetails.getLastName());
        result.setPhone(userDetails.getPhone());
        result.setEmail(siteUser.getUsername());
        return result;
    }
}
