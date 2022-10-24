package backends.backendsite.repositories;

import backends.backendsite.entities.AdsComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdsCommentRepository extends JpaRepository<AdsComment, Integer> {

    List<AdsComment> findAdsCommentsByAds_Id(Integer pk);

    List<AdsComment> findAdsCommentsByTextContains(String text);

}
