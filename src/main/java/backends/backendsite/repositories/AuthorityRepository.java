package backends.backendsite.repositories;

import backends.backendsite.entities.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for interaction with database and table stored data about user's authorities
 */
@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {

    /**
     * search authority by user's email
     *
     * @return Authority
     */
    Authority findAuthorityByUsername(String username);

}
