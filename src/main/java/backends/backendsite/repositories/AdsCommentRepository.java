package backends.backendsite.repositories;

import backends.backendsite.entities.AdsComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for interaction with database and table stored data about comments
 */
@Repository
public interface AdsCommentRepository extends JpaRepository<AdsComment, Integer> {

    /**
     * method for getting list comments by id their ad
     *
     * @return List<AdsComment></AdsComment>
     */
    List<AdsComment> findAdsCommentsByAds_Id(Integer pk);

    /**
     * method for getting list comments by text's part of comment
     *
     * @return List<AdsComment></AdsComment>
     */
    List<AdsComment> findAdsCommentsByTextContains(String text);

}
