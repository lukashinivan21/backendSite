package backends.backendsite.service.impl;

import backends.backendsite.dto.CreateUserDto;
import backends.backendsite.dto.NewPasswordDto;
import backends.backendsite.dto.ResponseWrapperDto;
import backends.backendsite.dto.UserDto;
import backends.backendsite.mappers.SiteUserMapper;
import backends.backendsite.repositories.SiteUserRepository;
import backends.backendsite.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public CreateUserDto addUser(CreateUserDto user) {

        return null;
    }

    @Override
    public ResponseWrapperDto<UserDto> getUsers() {
        return null;
    }

    @Override
    public UserDto updateUser(UserDto userDTO) {
        return null;
    }

    @Override
    public NewPasswordDto setPassword(NewPasswordDto password) {
        return null;
    }

    @Override
    public UserDto getUser(Integer id) {
        return null;
    }
}
