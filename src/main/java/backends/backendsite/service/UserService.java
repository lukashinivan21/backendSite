package backends.backendsite.service;

import backends.backendsite.dto.NewPasswordDto;
import backends.backendsite.dto.ResponseWrapperDto;
import backends.backendsite.dto.UserDto;

/**
 * Interface including methods for working with entity site user and site user details
 */
public interface UserService {

    /**
     * Method for getting list of users
     *
     * @return ResponseWrapperDto<UserDto></UserDto>
     */
    ResponseWrapperDto<UserDto> getUsers();

    /**
     * Method for updating data of one authorized user
     *
     * @return UserDto
     */
    UserDto updateUser(UserDto userDTO, String email);

    /**
     * Method for change password of one authorized user
     *
     * @return NewPasswordDto
     */
    NewPasswordDto setPassword(NewPasswordDto password, String email);

    /**
     * Method for getting info about one user by id
     *
     * @return UserDto
     */
    UserDto getUser(Integer id);


}
