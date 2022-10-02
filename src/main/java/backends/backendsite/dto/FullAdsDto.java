package backends.backendsite.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FullAdsDto {

    private Integer pk;
    private Integer price;
    private String title;
    private String description;
    private String phone;
    private String email;
    private String image;
    private String authorFirstName;
    private String authorLastName;

}
