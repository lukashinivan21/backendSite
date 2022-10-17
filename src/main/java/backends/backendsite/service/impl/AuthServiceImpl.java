package backends.backendsite.service.impl;

import backends.backendsite.dto.RegisterReq;
import backends.backendsite.dto.Role;
import backends.backendsite.entities.SiteUser;
import backends.backendsite.entities.SiteUserDetails;
import backends.backendsite.repositories.SiteUserRepository;
import backends.backendsite.repositories.UserDetailsRepository;
import backends.backendsite.service.AuthService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {


    private final UserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsRepository userDetailsRepository;
    private final SiteUserRepository siteUserRepository;

    public AuthServiceImpl(UserDetailsManager detailsManager, PasswordEncoder passwordEncoder,
                           UserDetailsRepository userDetailsRepository, SiteUserRepository siteUserRepository) {
        this.manager = detailsManager;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsRepository = userDetailsRepository;
        this.siteUserRepository = siteUserRepository;
    }


    @Override
    public boolean login(String username, String password) {
        if (!manager.userExists(username)) {
            return false;
        }
        UserDetails userDetails = manager.loadUserByUsername(username);
        String encryptedPassword = userDetails.getPassword();
        return passwordEncoder.matches(password, encryptedPassword);
    }

    @Override
    public boolean register(RegisterReq registerReq, Role role) {
        if (manager.userExists(registerReq.getUsername())) {
            return false;
        } else {
            manager.createUser(
                    User.withUsername(registerReq.getUsername())
                            .password(passwordEncoder.encode(registerReq.getPassword()))
                            .roles(role.toString())
                            .build()
            );

            SiteUser siteUser = siteUserRepository.findSiteUserByUsername(registerReq.getUsername()).orElseThrow();
            SiteUserDetails userDetails = new SiteUserDetails();
            userDetails.setFirstName(registerReq.getFirstName());
            userDetails.setLastName(registerReq.getLastName());
            userDetails.setPhone(registerReq.getPhone());
            siteUser.setSiteUserDetails(userDetails);
            siteUserRepository.save(siteUser);
//            userDetailsRepository.save(userDetails);
            return true;
        }
    }


    //                    User.withDefaultPasswordEncoder()
//                            .password(registerReq.getPassword())
//                            .username(registerReq.getUsername())
//                            .roles(role.name())
//                            .build()

}
