package backends.backendsite.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    void uploadImage(MultipartFile image, String email, Integer id) throws IOException;


}
