package backends.backendsite.mappers;

import backends.backendsite.dto.UserDto;
import backends.backendsite.entities.SiteUserDetails;
import backends.backendsite.entities.SiteUser;

public interface UserMapper {

    SiteUser fromUserDtoToSiteUser(SiteUser siteUser, UserDto userDto);

    UserDto fromSiteUserToUserDto(SiteUserDetails user);


}
