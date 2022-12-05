package backends.backendsite.mappers;

import backends.backendsite.dto.AdsCommentDto;
import backends.backendsite.entities.AdsComment;

/**
 * Interface included methods for converting entity ads comment to dto and back again
 */
public interface AdsCommentMapper {

    AdsCommentDto fromAdsCommentToAdsCommentDto(AdsComment adsComment);

    AdsComment fromAdsCommentDtoToAdsComment(AdsCommentDto dto, AdsComment comment);
}
