package backends.backendsite.repositories;

import backends.backendsite.entities.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for interaction with database and table stored data about site's users (email, password)
 */
@Repository
public interface SiteUserRepository extends JpaRepository<SiteUser, String> {

    /**
     * search user by his email
     * @return Optional<SiteUser></SiteUser>
     */
    Optional<SiteUser> findSiteUserByUsername(String email);


    /**
     * search user by his email
     * @return SiteUser
     */
    SiteUser findByUsername(String email);

}
