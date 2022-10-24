package backends.backendsite.repositories;

import backends.backendsite.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for interaction with database and table stored data about images
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {


}
