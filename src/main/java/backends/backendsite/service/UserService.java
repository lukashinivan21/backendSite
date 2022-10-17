package backends.backendsite.service;

import backends.backendsite.dto.NewPasswordDto;
import backends.backendsite.dto.ResponseWrapperDto;
import backends.backendsite.dto.UserDto;
import backends.backendsite.entities.SiteUser;

public interface UserService {

    ResponseWrapperDto<UserDto> getUsers();

    UserDto updateUser(UserDto userDTO, String email);

    NewPasswordDto setPassword(NewPasswordDto password);

    UserDto getUser(Integer id);

    SiteUser findUserByEmail(String email);
}
