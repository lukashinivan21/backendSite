package backends.backendsite.repositories;

import backends.backendsite.entities.Ads;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdsRepository extends JpaRepository<Ads, Integer> {

    /**
     * search one ad by id
     * @return Ads
     */
    Ads findAdsById(Integer id);

    /**
     * search list ads by id their owner
     * @return List<Ads></Ads>
     */
    List<Ads> findAdsBySiteUserDetailsId(Integer siteUserId);

    /**
     * search list ads by id their owner and price is indicated in ad
     * @return List<Ads></Ads>
     */
    List<Ads> findAllBySiteUserDetailsIdAndPrice(Integer id, Integer price);

    /**
     * search list ads by id their owner and part of ad title
     * @return List<Ads></Ads>
     */
    List<Ads> findAllBySiteUserDetailsIdAndTitleContains(Integer id, String text);

    /**
     * search list ads by id their owner, price is indicated in ad and part of ad title
     * @return List<Ads></Ads>
     */
    List<Ads> findAllBySiteUserDetailsIdAndPriceAndTitleContains(Integer id, Integer price, String title);

    /**
     * search list ads by part of ad title
     * @return List<Ads></Ads>
     */
    List<Ads> findAdsByTitleContains(String text);


}
