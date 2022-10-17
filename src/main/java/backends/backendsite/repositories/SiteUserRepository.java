package backends.backendsite.repositories;

import backends.backendsite.entities.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SiteUserRepository extends JpaRepository<SiteUser, String> {

    Optional<SiteUser> findSiteUserByPassword(String password);

    Optional<SiteUser> findSiteUserByUsername(String email);

    boolean existsSiteUserByUsername(String email);

}
