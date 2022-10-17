package backends.backendsite.repositories;

import backends.backendsite.entities.SiteUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsRepository extends JpaRepository<SiteUserDetails, Integer> {

    SiteUserDetails findSiteUserDetailsByFirstName(String firstName);

    SiteUserDetails findSiteUserDetailsBySiteUserPassword(String password);




}
