package backends.backendsite.service;

import backends.backendsite.dto.RegisterReq;
import backends.backendsite.dto.Role;

/**
 * Interface including methods for user's registration and authorization of access
 */
public interface AuthService {

    /**
     * Method for authorization of access
     * @return boolean
     */
    boolean login(String username, String password);

    /**
     * Method for registration new user
     * @return boolean
     */
    boolean register(RegisterReq registerReq, Role role);

}
