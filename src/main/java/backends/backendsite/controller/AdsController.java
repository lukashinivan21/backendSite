package backends.backendsite.controller;

import backends.backendsite.dto.*;
import backends.backendsite.entities.SiteUser;
import backends.backendsite.repositories.AdsRepository;
import backends.backendsite.service.AdsService;
import backends.backendsite.service.ImageService;
import backends.backendsite.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@RequestMapping("/ads")
@CrossOrigin(value = "http://localhost:3000")
public class AdsController {

    private final AdsService adsService;
    private final UserService userService;
    private final ImageService imageService;
    private final AdsRepository adsRepository;

    public AdsController(AdsService adsService, UserService userService, ImageService imageService, AdsRepository adsRepository) {
        this.adsService = adsService;
        this.userService = userService;
        this.imageService = imageService;
        this.adsRepository = adsRepository;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapperDto<AdsDto>> getAllAds() {
        return ResponseEntity.ok(adsService.getAllAds());
    }
//(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PostMapping
    public ResponseEntity<AdsDto> createAds(
            @RequestPart("properties") @Valid @NotNull @NotBlank CreateAdsDto createAdsDto,
            @RequestPart("image") @Valid @NotNull @NotBlank MultipartFile image) throws IOException {
        if (createAdsDto.getTitle() == null || createAdsDto.getDescription() == null || createAdsDto.getPrice() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        AdsDto result = adsService.addAds(createAdsDto, email);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            Integer id = result.getPk();
            imageService.uploadImage(image, email, id);
            result.setImage(adsRepository.findAdsByPk(id).getImage());
            return ResponseEntity.ok(result);
        }
    }

    //    <-----*****----->
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Список объявлений пользователя",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseWrapperDto.class)
                    )
            )
    })
    @GetMapping(value = "/me")
    public ResponseEntity getAdsMe(@RequestParam boolean authenticated,
                                   @RequestParam String authorities,
                                   @RequestParam Role credentials,
                                   @RequestParam Integer details,
                                   @RequestParam String principal) {
        if (!authenticated) {
            return ResponseEntity.status(401)
                    .body("Unauthorized");
        } else if (credentials != Role.ADMIN || credentials != Role.USER) {
            return ResponseEntity.status(403)
                    .body("forbidden");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SiteUser user = userService.findUserByEmail(authentication.getName());
        ResponseWrapperDto<AdsDto> adsMe = adsService.getAdsMe(details, principal, user);
        if (adsMe == null) {
            return ResponseEntity.status(404)
                    .body("Not Found");
        }
        return ResponseEntity.ok(adsMe);
    }
//    <-----*****----->

    @GetMapping("/{ad_pk}/comment")
    public ResponseEntity<ResponseWrapperDto<AdsCommentDto>> getAdsComments(@PathVariable Integer ad_pk) {
        ResponseWrapperDto<AdsCommentDto> result = adsService.getAdsComments(ad_pk);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{ad_pk}/comment")
    public ResponseEntity<AdsCommentDto> addAdsComment(@PathVariable Integer ad_pk,
                                                       @RequestBody AdsCommentDto adsCommentDto) {
        if (ad_pk == null || adsCommentDto.getText().equals("")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(adsService.addAdsComment(ad_pk, adsCommentDto.getText()));
    }

    @DeleteMapping("/{ad_pk}/comment/{id}")
    public ResponseEntity<String> deleteAdsComment(@PathVariable Integer ad_pk,
                                                   @PathVariable Integer id) {
        String result = adsService.deleteAdsComment(ad_pk, id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{ad_pk}/comment/{id}")
    public ResponseEntity<AdsCommentDto> getAdsCommentOfOneAds(@PathVariable Integer ad_pk,
                                                               @PathVariable Integer id) {
        AdsCommentDto result = adsService.getAdsComment(ad_pk, id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{ad_pk}/comment/{id}")
    public ResponseEntity<AdsCommentDto> updateAdsComment(@PathVariable Integer ad_pk,
                                                          @PathVariable Integer id,
                                                          @RequestBody AdsCommentDto adsCommentDto) {
        if (adsCommentDto.getText().equals("")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        AdsCommentDto result = adsService.updateAdsComment(ad_pk, id, adsCommentDto);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Удаление объявления по id",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = String.class)
                    )
            )
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> removeAds(@Parameter(example = "1") @PathVariable Integer id) {
        return ResponseEntity.ok(adsService.removeAds(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FullAdsDto> getFullInfoAboutAds(@PathVariable Integer id) {
        FullAdsDto result = adsService.getAds(id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AdsDto> updateAds(@PathVariable Integer id,
                                            @RequestBody AdsDto adsDto) {
        AdsDto result = adsService.updateAds(id, adsDto);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/adsTitle{text}")
    public ResponseEntity<ResponseWrapperDto<AdsDto>> getAdsWithTitleContainsText(@PathVariable String text) {
        ResponseWrapperDto<AdsDto> result = adsService.getAdsWithTitleContainsText(text);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/commentContains{text}")
    public ResponseEntity<ResponseWrapperDto<AdsCommentDto>> getAdsCommentsWithText(@PathVariable String text) {
        ResponseWrapperDto<AdsCommentDto> result = adsService.getCommentWithText(text);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }


}
