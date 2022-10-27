package backends.backendsite.controller;

import backends.backendsite.dto.LoginReq;
import backends.backendsite.dto.RegisterReq;
import backends.backendsite.exceptionsHandler.exceptions.NotLoginException;
import backends.backendsite.exceptionsHandler.exceptions.UserAlreadyExistException;
import backends.backendsite.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static backends.backendsite.dto.Role.USER;

@RestController
@CrossOrigin(value = "http://localhost:3000")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @Operation(summary = "Method for authorization an access to site",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Access is authorized successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access is forbidden"
                    )
            })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReq req) {
        if (authService.login(req.getUsername(), req.getPassword())) {
            return ResponseEntity.ok().build();
        } else {
            throw new NotLoginException();
        }
    }

    @Operation(summary = "Method for registration new user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Registration is completed successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Check input data"
                    )
            })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterReq req) {
        if (authService.register(req, USER)) {
            return ResponseEntity.ok().build();
        } else {
            throw new UserAlreadyExistException();
        }
    }


}
