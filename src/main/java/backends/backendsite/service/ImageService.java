package backends.backendsite.service;

import backends.backendsite.entities.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    void uploadImage(MultipartFile image, String email, Integer id) throws IOException;

    Image getImageById(Integer id);


}
