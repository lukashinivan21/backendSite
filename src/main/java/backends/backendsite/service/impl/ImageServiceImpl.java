package backends.backendsite.service.impl;

import backends.backendsite.entities.Ads;
import backends.backendsite.entities.Image;
import backends.backendsite.repositories.AdsRepository;
import backends.backendsite.repositories.ImageRepository;
import backends.backendsite.service.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class ImageServiceImpl implements ImageService {

    private final AdsRepository adsRepository;
    private final ImageRepository imageRepository;

    @Value("${path.to.images.folder}")
    private String imagesDir;

    public ImageServiceImpl(AdsRepository adsRepository, ImageRepository imageRepository) {
        this.adsRepository = adsRepository;
        this.imageRepository = imageRepository;
    }

    @Override
    public void uploadImage(MultipartFile image, String email, Integer id) throws IOException {

        Optional<Ads> optionalAds = adsRepository.findById(id);

        if (optionalAds.isPresent()) {

            Ads ads = optionalAds.get();
            String title = ads.getTitle();
            Path filePath = Path.of(imagesDir + "/" + email + "/" + title, title + "." + getExtension(Objects.requireNonNull(image.getOriginalFilename())));
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);

            try (InputStream is = image.getInputStream();
                 OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                 BufferedInputStream bis = new BufferedInputStream(is, 1024);
                 BufferedOutputStream bos = new BufferedOutputStream(os, 1024)) {
                bis.transferTo(bos);
            }

            ads.setImage(filePath.toString());
            adsRepository.save(ads);

            Image newImage = new Image();
            newImage.setAds(ads);
            newImage.setFilePath(filePath.toString());
            newImage.setFileSize(image.getSize());
            newImage.setMediaType(image.getContentType());
            newImage.setData(image.getBytes());

            imageRepository.save(newImage);
        }


    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }


}
