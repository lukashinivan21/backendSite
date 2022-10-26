package backends.backendsite.controller;

import backends.backendsite.dto.*;
import backends.backendsite.entities.Image;
import backends.backendsite.exceptionsHandler.exceptions.BadInputDataException;
import backends.backendsite.exceptionsHandler.exceptions.NotAccessActionException;
import backends.backendsite.service.AdsService;
import backends.backendsite.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final ImageService imageService;

    public AdsController(AdsService adsService, ImageService imageService) {
        this.adsService = adsService;
        this.imageService = imageService;
    }

    //    there is in postman
    @Operation(summary = "Getting list of all ads existed in data base",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "All ads are got successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseWrapperDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "There aren't ads in data base"
                    )
            })
    @GetMapping
    public ResponseEntity<ResponseWrapperDto<AdsDto>> getAllAds() {
        return ResponseEntity.ok(adsService.getAllAds());
    }


    // there is in postman
    @Operation(summary = "Creating new ad",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "New ad is created successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdsDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Check your request"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "You haven't access to create ad"
                    )
            })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<AdsDto> createAds(@RequestPart("properties") @Valid @NotNull @NotBlank CreateAdsDto createAdsDto,
                                            @RequestPart("image") @Valid @NotNull @NotBlank MultipartFile image) throws IOException {
        if (createAdsDto.getTitle() == null || createAdsDto.getDescription() == null || createAdsDto.getPrice() == null) {
            throw new BadInputDataException();
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AdsDto result = adsService.addAds(createAdsDto, email);
        if (result == null) {
            throw new NotAccessActionException();
        } else {
            Integer id = result.getPk();
            String imageS = imageService.uploadImage(image, email, id);
            result.setImage(imageS);
            return ResponseEntity.ok(result);
        }
    }

    //    there is in postman
    @Operation(summary = "Getting all ads of one authorized user by his username,price in ads (if it's indicated) and part of ad's title (if it's indicated)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List ads of user is found successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseWrapperDto.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "User hasn't access to getting ads"
                    )
            })
    @GetMapping(value = "/me")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ResponseWrapperDto<AdsDto>> getAdsMe(
            @RequestParam(required = false) Integer details,
            @RequestParam(required = false) String principal) {
        ResponseWrapperDto<AdsDto> adsMe = adsService.getAdsMe(details, principal);
        return ResponseEntity.ok(adsMe);
    }

    //    there is in postman
    @Operation(summary = "Creating new comment for ad with fixed id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "New comment is created successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdsCommentDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Check your request"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Ad with this id doesn't exist"
                    )
            })
    @PostMapping("/{adPk}/comment")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<AdsCommentDto> addAdsComment(@PathVariable Integer adPk, @RequestBody AdsCommentDto adsCommentDto) {
        if (adPk == null || adsCommentDto.getText() == null) {
            throw new BadInputDataException();
        }
        AdsCommentDto result = adsService.addAdsComment(adPk, adsCommentDto.getText());
        return ResponseEntity.ok(result);
    }

    //    there is in postman
    @Operation(summary = "Getting all comments of ad with fixed id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of comments is found successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseWrapperDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Wrong id of ads"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Ad with this id hasn't comments"
                    )
            })
    @GetMapping("/{adPk}/comment")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ResponseWrapperDto<AdsCommentDto>> getAdsComments(@PathVariable Integer adPk) {
        ResponseWrapperDto<AdsCommentDto> result = adsService.getAdsComments(adPk);
        return ResponseEntity.ok(result);
    }

    //    there is in postman
    @Operation(summary = "Getting one comment by id of ad with fixed id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Comment is found successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdsCommentDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Wrong id of ads"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Comment with this id doesn't exist"
                    )
            })
    @GetMapping("/{adPk}/comment/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<AdsCommentDto> getAdsCommentOfOneAds(@PathVariable Integer adPk, @PathVariable Integer id) {
        AdsCommentDto result = adsService.getAdsComment(adPk, id);
        return ResponseEntity.ok(result);
    }


    //    there is in postman
    @Operation(summary = "Deleting ads by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ad is deleted successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = String.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "You haven't access for deleting this ad"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Ad with this id doesn't exist"
                    )
            })
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<String> removeAds(@Parameter(example = "1") @PathVariable Integer id) {
        String result = adsService.removeAds(id);
        return ResponseEntity.ok(result);
    }


    //    there is in postman
    @Operation(summary = "Deleting comment by id of ad with fixed id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Comment is deleted successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = String.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Wrong id of ad or wrong id of comment"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "You haven't access for deleting this comment"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Comment with this id doesn't exist"
                    )
            })
    @DeleteMapping("/{adPk}/comment/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<String> deleteAdsComment(@PathVariable Integer adPk, @PathVariable Integer id) {
        String result = adsService.deleteAdsComment(adPk, id);
        return ResponseEntity.ok(result);
    }


    //    there is in postman
    @Operation(summary = "Updating ads by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ad is updated successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdsDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "You haven't access for updating this ad"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Ad with this id doesn't exist"
                    )
            })
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<AdsDto> updateAds(@PathVariable Integer id, @RequestBody AdsDto adsDto) {
        AdsDto result = adsService.updateAds(id, adsDto);
        return ResponseEntity.ok(result);
    }


    //    there is in postman
    @Operation(summary = "Updating comment by id of ad with fixed id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Comment is updated successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdsCommentDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Wrong id of ad or wrong id of comment"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "You haven't access for updating this comment"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Comment with this id doesn't exist"
                    )
            })
    @PatchMapping("/{adPk}/comment/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<AdsCommentDto> updateAdsComment(@PathVariable Integer adPk, @PathVariable Integer id, @RequestBody AdsCommentDto adsCommentDto) {
        if (adsCommentDto.getText() == null) {
            throw new BadInputDataException();
        }
        AdsCommentDto result = adsService.updateAdsComment(adPk, id, adsCommentDto);
        return ResponseEntity.ok(result);
    }


    //    there is in postman
    @Operation(summary = "Getting full info about ads by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Full info is found successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = FullAdsDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "You haven't access for getting info"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Ad with this id doesn't exist"
                    )
            })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<FullAdsDto> getFullInfoAboutAds(@PathVariable Integer id) {
        FullAdsDto result = adsService.getAds(id);
        return ResponseEntity.ok(result);
    }


    //  there is in postman
    @Operation(summary = "Getting ads with title containing indicated text",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ads are found successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseWrapperDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Ads with this title don't exist"
                    )
            })
    @GetMapping("/adsTitle/{text}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ResponseWrapperDto<AdsDto>> getAdsWithTitleContainsText(@PathVariable String text) {
        ResponseWrapperDto<AdsDto> result = adsService.getAdsWithTitleContainsText(text);
        return ResponseEntity.ok(result);
    }

    //  there is in postman
    @Operation(summary = "Getting comments containing indicated text",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Comments are found successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseWrapperDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Comments with this text don't exist"
                    )
            })
    @GetMapping("/commentContains/{text}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ResponseWrapperDto<AdsCommentDto>> getAdsCommentsWithText(@PathVariable String text) {
        ResponseWrapperDto<AdsCommentDto> result = adsService.getCommentWithText(text);
        return ResponseEntity.ok(result);
    }
//    there is in postman


    @Operation(summary = "Getting image of one ad",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Image is found successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseWrapperDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Image isn't found"
                    )
            })
    @GetMapping(value = "/images/{image}", produces = {MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> getImage(@PathVariable Integer image) {
        Image imageById = imageService.getImageById(image);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(imageById.getMediaType()));
        headers.setContentLength(imageById.getData().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(imageById.getData());
    }


    @Operation(summary = "Request to change image of one ad",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Change is completed successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = FullAdsDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "You haven't access to this action"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Image with this id doesn't exist"
                    )
            })
    @PatchMapping(value = "/{id}/image", produces = {MediaType.IMAGE_PNG_VALUE})
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<byte[]> updateImage(@PathVariable Integer id,
                                              @RequestPart("image") @Valid @NotNull @NotBlank MultipartFile image) throws IOException {

        Image image1 = imageService.updateImage(id, image);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(image1.getMediaType()));
        headers.setContentLength(image1.getData().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(image1.getData());
    }
}
