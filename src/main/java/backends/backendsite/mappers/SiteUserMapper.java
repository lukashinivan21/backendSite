package backends.backendsite.mappers;

import backends.backendsite.dto.CreateUserDto;
import backends.backendsite.dto.UserDto;
import backends.backendsite.entities.SiteUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SiteUserMapper {

    SiteUser fromCreateUserDtoToSiteUser(CreateUserDto userDto);

    CreateUserDto fromSiteUserToCreateUserDto(SiteUser siteUser);

    SiteUser fromUserDtoToSiteUser(UserDto user);

    UserDto fromSiteUserToUserDto(SiteUser siteUser);
}
