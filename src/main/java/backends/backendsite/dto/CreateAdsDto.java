package backends.backendsite.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAdsDto {

    private Integer pk;
    private Integer price;
    private String title;
    private String description;
    private String image;
}
