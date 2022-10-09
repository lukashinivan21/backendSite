package backends.backendsite.controller;

import backends.backendsite.dto.AdsDto;
import backends.backendsite.dto.ResponseWrapperDto;
import backends.backendsite.dto.Role;
import backends.backendsite.entities.SiteUser;
import backends.backendsite.service.AdsService;
import backends.backendsite.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ads")
@CrossOrigin(value = "http://localhost:3000")
public class AdsController {

    private final AdsService adsService;
    private final UserService userService;


    public AdsController(AdsService adsService, UserService userService) {
        this.adsService = adsService;
        this.userService = userService;
    }




    @GetMapping(value = "/me")
    public ResponseEntity getAdsMe(@RequestParam boolean authenticated,
                                   @RequestParam String authorities,
                                   @RequestParam Role credentials,
                                   @RequestParam Integer details,
                                   @RequestParam String principal){
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







}
