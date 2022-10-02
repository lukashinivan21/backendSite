package backends.backendsite.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseWrapperDto<T> {

    private Integer count;
    private List<T> list;
}
