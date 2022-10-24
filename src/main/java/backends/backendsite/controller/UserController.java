package backends.backendsite.controller;

import backends.backendsite.dto.NewPasswordDto;
import backends.backendsite.dto.ResponseWrapperDto;
import backends.backendsite.dto.UserDto;
import backends.backendsite.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static backends.backendsite.service.StringConstants.NOT_ACCESS;

@RestController
@RequestMapping("/users")
@CrossOrigin(value = "http://localhost:3000")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Getting list with info about all users (if role is admin) or info about one user himself (if role is user)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Info is found successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseWrapperDto.class))),
            })
    @GetMapping("/me")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<ResponseWrapperDto<UserDto>> getUsers() {
        ResponseWrapperDto<UserDto> result = userService.getUsers();
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Updating one existed user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Updating is completed successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserDto.class))),
            })
    @PatchMapping("/me")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto user) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDto result = userService.updateUser(user, email);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Change password of one existed user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Change password is completed successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = NewPasswordDto.class))),
            })
    @PostMapping("/set_password")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<NewPasswordDto> setPassword(@RequestBody NewPasswordDto password) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        NewPasswordDto result = userService.setPassword(password, email);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Getting info about one user by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Info is found successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "You haven't access to info about this user"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Info about user isn't found"
                    )
            })
    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<UserDto> getUser(@PathVariable Integer id) {
        UserDto result = userService.getUser(id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (result.getFirstName().equals(NOT_ACCESS)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(result);
    }


}
