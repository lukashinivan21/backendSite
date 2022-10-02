package backends.backendsite.service;

import backends.backendsite.dto.RegisterReq;
import backends.backendsite.dto.Role;

public interface AuthService {

    boolean login(String username, String password);

    boolean register(RegisterReq registerReq, Role role);

}
