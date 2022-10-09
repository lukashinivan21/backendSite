package backends.backendsite.mappers;

import backends.backendsite.dto.AdsCommentDto;
import backends.backendsite.entities.AdsComment;

public interface AdsCommentMapper {

    AdsCommentDto fromAdsCommentToAdsCommentDto(AdsComment adsComment);

    AdsComment fromAdsCommentDtoToAdsComment(AdsCommentDto dto, AdsComment comment);
}
