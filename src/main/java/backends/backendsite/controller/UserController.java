package backends.backendsite.controller;

import backends.backendsite.dto.NewPasswordDto;
import backends.backendsite.dto.ResponseWrapperDto;
import backends.backendsite.dto.UserDto;
import backends.backendsite.service.UserService;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/me")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<ResponseWrapperDto<UserDto>> getUsers() {
        ResponseWrapperDto<UserDto> result = userService.getUsers();
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto user) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDto result = userService.updateUser(user, email);
//        if (result == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/set_password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<NewPasswordDto> setPassword(@RequestBody NewPasswordDto password) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        NewPasswordDto result = userService.setPassword(password, email);
//        if (result == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
        return ResponseEntity.ok(result);
    }

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
