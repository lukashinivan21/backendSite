package backends.backendsite.mappers;

import backends.backendsite.dto.AdsCommentDto;
import backends.backendsite.entities.AdsComment;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AdsCommentMapperImpl implements AdsCommentMapper{

    @Override
    public AdsCommentDto fromAdsCommentToAdsCommentDto(AdsComment adsComment) {
        AdsCommentDto adsCommentDto = new AdsCommentDto();
        adsCommentDto.setPk(adsComment.getPk());
        adsCommentDto.setAuthor(adsComment.getAuthor());
        adsCommentDto.setText(adsComment.getText());
        adsCommentDto.setCreatedAt(adsComment.getCreatedAt());
        return adsCommentDto;
    }

    @Override
    public AdsComment fromAdsCommentDtoToAdsComment(AdsCommentDto dto, AdsComment comment) {
        if (!dto.getText().equals("")) {
            comment.setText(dto.getText());
            comment.setCreatedAt(LocalDateTime.now());
        }
        return comment;
    }
}
