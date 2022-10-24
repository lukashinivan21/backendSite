package backends.backendsite.service.impl;

import backends.backendsite.dto.RegisterReq;
import backends.backendsite.dto.Role;
import backends.backendsite.entities.SiteUser;
import backends.backendsite.entities.SiteUserDetails;
import backends.backendsite.repositories.SiteUserRepository;
import backends.backendsite.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);


    private final UserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;
    private final SiteUserRepository siteUserRepository;

    public AuthServiceImpl(UserDetailsManager detailsManager, PasswordEncoder passwordEncoder, SiteUserRepository siteUserRepository) {
        this.manager = detailsManager;
        this.passwordEncoder = passwordEncoder;
        this.siteUserRepository = siteUserRepository;
    }

    @Override
    public boolean login(String username, String password) {
        if (!manager.userExists(username)) {
            logger.info("User with email: {} is not exist", username);
            return false;
        }
        logger.info("Request for login user with email: {} and password: {}", username, password);
        UserDetails userDetails = manager.loadUserByUsername(username);
        String encryptedPassword = userDetails.getPassword();
        logger.info("Encrypted password: {}", encryptedPassword);
        return passwordEncoder.matches(password, encryptedPassword);
    }

    @Override
    public boolean register(RegisterReq registerReq, Role role) {
        if (manager.userExists(registerReq.getUsername())) {
            logger.info("User with username: {} already exists", registerReq.getUsername());
            return false;
        } else {
            logger.info("Creating user with username: {}; firstName: {}; lastName: {}; phone: {}", registerReq.getUsername(),
                    registerReq.getFirstName(), registerReq.getLastName(), registerReq.getPhone());
            manager.createUser(
                    User.withUsername(registerReq.getUsername())
                            .password(passwordEncoder.encode(registerReq.getPassword()))
                            .roles(role.toString())
                            .build()
            );
            SiteUser siteUser = siteUserRepository.findById(registerReq.getUsername()).orElseThrow();
            SiteUserDetails userDetails = new SiteUserDetails();
            userDetails.setFirstName(registerReq.getFirstName());
            userDetails.setLastName(registerReq.getLastName());
            userDetails.setPhone(registerReq.getPhone());
            siteUser.setSiteUserDetails(userDetails);

            siteUserRepository.save(siteUser);

            return true;
        }
    }


    //                    User.withDefaultPasswordEncoder()
//                            .password(registerReq.getPassword())
//                            .username(registerReq.getUsername())
//                            .roles(role.name())
//                            .build()

}
