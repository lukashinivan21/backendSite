package backends.backendsite.repositories;

import backends.backendsite.entities.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteUserRepository extends JpaRepository<SiteUser, Integer> {


}
