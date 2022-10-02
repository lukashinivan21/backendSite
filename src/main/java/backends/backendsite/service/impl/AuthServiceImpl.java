package backends.backendsite.service.impl;

import backends.backendsite.dto.RegisterReq;
import backends.backendsite.dto.Role;
import backends.backendsite.service.AuthService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {


    private final UserDetailsManager manager;

    private final PasswordEncoder encoder;

    public AuthServiceImpl(UserDetailsManager detailsManager) {
        this.manager = detailsManager;
        this.encoder = new BCryptPasswordEncoder();
    }


    @Override
    public boolean login(String username, String password) {
        if (!manager.userExists(username)) {
            return false;
        }
        UserDetails userDetails = manager.loadUserByUsername(username);
        String encryptedPassword = userDetails.getPassword();
        String encryptedPasswordWithoutEncryptionType = encryptedPassword.substring(8);
        return encoder.matches(password, encryptedPasswordWithoutEncryptionType);
    }

    @Override
    public boolean register(RegisterReq registerReq, Role role) {
        if (manager.userExists(registerReq.getUsername())) {
            return false;
        }
        manager.createUser(
                User.withDefaultPasswordEncoder()
                        .password(registerReq.getPassword())
                        .username(registerReq.getUsername())
                        .roles(role.name())
                        .build()
        );
        return true;
    }

}
