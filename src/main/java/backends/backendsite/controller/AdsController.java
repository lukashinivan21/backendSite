package backends.backendsite.controller;

import backends.backendsite.dto.*;
import backends.backendsite.entities.Image;
import backends.backendsite.entities.SiteUser;
import backends.backendsite.service.AdsService;
import backends.backendsite.service.ImageService;
import backends.backendsite.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;

import static backends.backendsite.service.StringConstants.HAVE_NOT;
import static backends.backendsite.service.StringConstants.NOT_FOUND;

@RestController
@RequestMapping("/ads")
@CrossOrigin(value = "http://localhost:3000")
public class AdsController {

    private final AdsService adsService;
    private final UserService userService;
    private final ImageService imageService;

    public AdsController(AdsService adsService, UserService userService, ImageService imageService) {
        this.adsService = adsService;
        this.userService = userService;
        this.imageService = imageService;
    }

    //    there is in postman
    @GetMapping
    public ResponseEntity<ResponseWrapperDto<AdsDto>> getAllAds() {
        return ResponseEntity.ok(adsService.getAllAds());
    }


    //(consumes = MediaType.MULTIPART_FORM_DATA_VALUE) there is in postman
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<AdsDto> createAds(@RequestPart("properties") @Valid @NotNull @NotBlank CreateAdsDto createAdsDto, @RequestPart("image") @Valid @NotNull @NotBlank MultipartFile image) throws IOException {
        if (createAdsDto.getTitle() == null || createAdsDto.getDescription() == null || createAdsDto.getPrice() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AdsDto result = adsService.addAds(createAdsDto, email);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            Integer id = result.getPk();
            imageService.uploadImage(image, email, id);
            result.setImage(adsService.getAdsByPk(id).getImage());
            return ResponseEntity.ok(result);
        }
    }

    //    there is in postman, but don't work
    //    <-----*****----->
    @ApiResponses({@ApiResponse
            (responseCode = "200",
                    description = "Список объявлений пользователя",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseWrapperDto.class)))})
    @GetMapping(value = "/me")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity getAdsMe(@RequestParam boolean authenticated,
                                   @RequestParam String authorities,
                                   @RequestParam Role credentials,
                                   @RequestParam Integer details,
                                   @RequestParam String principal) {
        if (!authenticated) {
            return ResponseEntity.status(401).body("Unauthorized");
        } else if (credentials != Role.ADMIN || credentials != Role.USER) {
            return ResponseEntity.status(403).body("forbidden");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SiteUser user = userService.findUserByEmail(authentication.getName());
        ResponseWrapperDto<AdsDto> adsMe = adsService.getAdsMe(details, principal, user);
        if (adsMe == null) {
            return ResponseEntity.status(404).body("Not Found");
        }
        return ResponseEntity.ok(adsMe);
    }
//    <-----*****----->


    //    there is in postman
    @PostMapping("/{ad_pk}/comment")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<AdsCommentDto> addAdsComment(@PathVariable Integer ad_pk, @RequestBody AdsCommentDto adsCommentDto) {
        if (ad_pk == null || adsCommentDto.getText() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(adsService.addAdsComment(ad_pk, adsCommentDto.getText()));
    }

    //    there is in postman
    @GetMapping("/{ad_pk}/comment")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ResponseWrapperDto<AdsCommentDto>> getAdsComments(@PathVariable Integer ad_pk) {
        ResponseWrapperDto<AdsCommentDto> result = adsService.getAdsComments(ad_pk);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    //    there is in postman
    @GetMapping("/{ad_pk}/comment/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<AdsCommentDto> getAdsCommentOfOneAds(@PathVariable Integer ad_pk, @PathVariable Integer id) {
        AdsCommentDto result = adsService.getAdsComment(ad_pk, id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }


    //    there is in postman
    @DeleteMapping("/{ad_pk}/comment/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<String> deleteAdsComment(@PathVariable Integer ad_pk, @PathVariable Integer id) {
        String result = adsService.deleteAdsComment(ad_pk, id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (result.equals(HAVE_NOT)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(result);
    }

    //    there is in postman
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Удаление объявления по id", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class)))})
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<String> removeAds(@Parameter(example = "1") @PathVariable Integer id) {
        String result = adsService.removeAds(id);
        if (result.equals(NOT_FOUND)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (result.equals(HAVE_NOT)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(adsService.removeAds(id));
    }

    //    there is in postman
    @PatchMapping("/{ad_pk}/comment/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<AdsCommentDto> updateAdsComment(@PathVariable Integer ad_pk, @PathVariable Integer id, @RequestBody AdsCommentDto adsCommentDto) {
        if (adsCommentDto.getText() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        AdsCommentDto result = adsService.updateAdsComment(ad_pk, id, adsCommentDto);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (result.getText().equals(HAVE_NOT)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(result);
    }


    //    there is in postman
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<AdsDto> updateAds(@PathVariable Integer id, @RequestBody AdsDto adsDto) {
        AdsDto result = adsService.updateAds(id, adsDto);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (result.getTitle().equals(HAVE_NOT)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(result);
    }


    //    there is in postman
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<FullAdsDto> getFullInfoAboutAds(@PathVariable Integer id) {
        FullAdsDto result = adsService.getAds(id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (result.getTitle().equals(HAVE_NOT)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(result);
    }


    //    there is in postman
    @GetMapping(value = "/images/{id}", produces = {MediaType.IMAGE_PNG_VALUE})
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<byte[]> getImage(@PathVariable Integer id) {
        Image image = imageService.getImageById(id);
        if (image == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(image.getMediaType()));
        headers.setContentLength(image.getData().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(image.getData());
    }


    @GetMapping("/adsTitle/{text}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ResponseWrapperDto<AdsDto>> getAdsWithTitleContainsText(@PathVariable String text) {
        ResponseWrapperDto<AdsDto> result = adsService.getAdsWithTitleContainsText(text);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }


    @GetMapping("/commentContains/{text}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ResponseWrapperDto<AdsCommentDto>> getAdsCommentsWithText(@PathVariable String text) {
        ResponseWrapperDto<AdsCommentDto> result = adsService.getCommentWithText(text);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }


}
