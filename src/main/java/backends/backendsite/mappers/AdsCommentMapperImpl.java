package backends.backendsite.mappers;

import backends.backendsite.dto.AdsCommentDto;
import backends.backendsite.entities.AdsComment;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Class implements methods for converting ads comment entity to ads comment dto and back again
 */
@Service
public class AdsCommentMapperImpl implements AdsCommentMapper{

//    method converts from ads comment entity to ads comment dto
    @Override
    public AdsCommentDto fromAdsCommentToAdsCommentDto(AdsComment adsComment) {
        AdsCommentDto adsCommentDto = new AdsCommentDto();
        adsCommentDto.setPk(adsComment.getId());
        adsCommentDto.setAuthor(adsComment.getAuthor());
        adsCommentDto.setText(adsComment.getText());
        adsCommentDto.setCreatedAt(adsComment.getCreatedAt());
        return adsCommentDto;
    }

//    method converts from ads comment dto to ads comment entity
    @Override
    public AdsComment fromAdsCommentDtoToAdsComment(AdsCommentDto dto, AdsComment comment) {
        if (dto.getText() != null) {
            comment.setText(dto.getText());
            comment.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        }
        return comment;
    }
}
