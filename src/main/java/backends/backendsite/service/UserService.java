package backends.backendsite.service;

import backends.backendsite.dto.CreateUserDto;
import backends.backendsite.dto.NewPasswordDto;
import backends.backendsite.dto.ResponseWrapperDto;
import backends.backendsite.dto.UserDto;

public interface UserService {

    CreateUserDto addUser(CreateUserDto user);

    ResponseWrapperDto<UserDto> getUsers();

    UserDto updateUser(UserDto userDTO);

    NewPasswordDto setPassword(NewPasswordDto password);

    UserDto getUser(Integer id);
}
