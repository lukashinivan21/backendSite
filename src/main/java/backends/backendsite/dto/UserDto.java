package backends.backendsite.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private Integer id;

}
