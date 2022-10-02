package backends.backendsite.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AdsCommentDto {

    private Integer author;
    private Integer pk;
    private String text;
    private LocalDateTime createdAt;
}
