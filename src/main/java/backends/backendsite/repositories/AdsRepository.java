package backends.backendsite.repositories;

import backends.backendsite.entities.Ads;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdsRepository extends JpaRepository<Ads, Integer> {

    List<Ads> findByAuthorAndPriceAndTitle(Integer author, Integer price, String title);

    List<Ads> findAdsBySiteUserId(Integer siteUserId);

    List<Ads> findAdsByTitleContains(String text);

}
