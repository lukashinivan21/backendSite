package backends.backendsite.mappers;

import backends.backendsite.dto.AdsDto;
import backends.backendsite.dto.CreateAdsDto;
import backends.backendsite.dto.FullAdsDto;
import backends.backendsite.entities.Ads;
import backends.backendsite.entities.SiteUserDetails;
import org.springframework.stereotype.Service;

/**
 * Class implements methods for converting entity ads to dto and back again
 */
@Service
public class SelfAdsMapperImpl implements SelfAdsMapper {

    //     method converts from ads dto to ads entity
    @Override
    public Ads fromAdsDtoToAds(AdsDto adsDto, Ads ads) {
        if (adsDto.getTitle() != null) {
            ads.setTitle(adsDto.getTitle());
        }
        if (adsDto.getPrice() != null) {
            ads.setPrice(ads.getPrice());
        }
        return ads;
    }

    //    method converts from ads entity to ads dto
    @Override
    public AdsDto fromAdsToAdsDto(Ads ads) {
        AdsDto adsDto = new AdsDto();
        if (ads.getId() != null) {
            adsDto.setPk(ads.getId());
        }
        if (ads.getTitle() != null) {
            adsDto.setTitle(ads.getTitle());
        }
        if (ads.getPrice() != null) {
            adsDto.setPrice(ads.getPrice());
        }
        if (ads.getImage() != null) {
            adsDto.setImage(ads.getImage());
        }
        if (ads.getAuthor() != null) {
            adsDto.setAuthor(ads.getAuthor());
        }
        return adsDto;
    }

    //    method converts from create ads dto to ads entity
    @Override
    public Ads fromCreateAdsDtoToAds(CreateAdsDto createAdsDto) {
        Ads ads = new Ads();
        ads.setTitle(createAdsDto.getTitle());
        ads.setDescription(createAdsDto.getDescription());
        ads.setPrice(createAdsDto.getPrice());
        if (createAdsDto.getImage() != null) {
            ads.setImage(createAdsDto.getImage());
        }
        return ads;
    }

    //    method converts from ads entity and site user details entity to full ads dto
    @Override
    public FullAdsDto mapToFullAdsDto(Ads ads, SiteUserDetails userDetails) {
        FullAdsDto result = new FullAdsDto();
        result.setPk(ads.getId());
        result.setPrice(ads.getPrice());
        result.setTitle(ads.getTitle());
        result.setDescription(ads.getDescription());
        result.setImage(ads.getImage());
        result.setAuthorFirstName(userDetails.getFirstName());
        result.setAuthorLastName(userDetails.getLastName());
        result.setPhone(userDetails.getPhone());
        result.setEmail(userDetails.getSiteUser().getUsername());
        return result;
    }
}
