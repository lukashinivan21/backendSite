package backends.backendsite.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class RegisterReq {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;

}