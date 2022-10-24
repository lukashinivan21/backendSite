package backends.backendsite.service;

import backends.backendsite.dto.NewPasswordDto;
import backends.backendsite.dto.ResponseWrapperDto;
import backends.backendsite.dto.UserDto;

public interface UserService {

    ResponseWrapperDto<UserDto> getUsers();

    UserDto updateUser(UserDto userDTO, String email);

    NewPasswordDto setPassword(NewPasswordDto password, String email);

    UserDto getUser(Integer id);

}
