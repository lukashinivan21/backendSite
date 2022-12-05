package backends.backendsite.service;

import backends.backendsite.entities.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Interface including methods for working with entity image
 */
public interface ImageService {

    /**
     * Method for uploading image of new ad
     *
     * @return String
     */
    String uploadImage(MultipartFile image, String email, Integer id) throws IOException;

    /**
     * Search image by id
     *
     * @return Image
     */
    Image getImageById(Integer id);

    /**
     * Method for updating image existed
     *
     * @return Image
     */
    Image updateImage(Integer id, MultipartFile image) throws IOException;


}
