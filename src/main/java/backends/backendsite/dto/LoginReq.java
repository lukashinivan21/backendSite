package backends.backendsite.dto;

import lombok.Data;

@Data
public class LoginReq {

    private String password;
    private String username;

}
