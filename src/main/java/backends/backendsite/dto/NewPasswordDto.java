package backends.backendsite.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewPasswordDto {

    private String currentPassword;
    private String newPassword;

}
