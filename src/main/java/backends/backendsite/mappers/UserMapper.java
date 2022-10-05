package backends.backendsite.mappers;

import backends.backendsite.dto.CreateUserDto;
import backends.backendsite.dto.UserDto;
import backends.backendsite.entities.SiteUser;

public interface UserMapper {

    SiteUser fromCreateUserDtoToSiteUser(CreateUserDto userDto);

    CreateUserDto fromSiteUserToCreateUserDto(SiteUser user);

    SiteUser fromUserDtoToSiteUser(SiteUser siteUser, UserDto userDto);

    UserDto fromSiteUserToUserDto(SiteUser user);


}
