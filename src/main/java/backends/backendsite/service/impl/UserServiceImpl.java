package backends.backendsite.service.impl;

import backends.backendsite.dto.CreateUserDto;
import backends.backendsite.dto.NewPasswordDto;
import backends.backendsite.dto.ResponseWrapperDto;
import backends.backendsite.dto.UserDto;
import backends.backendsite.entities.SiteUser;
import backends.backendsite.mappers.UserMapper;
import backends.backendsite.repositories.SiteUserRepository;
import backends.backendsite.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final SiteUserRepository siteUserRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(SiteUserRepository siteUserRepository, UserMapper userMapper) {
        this.siteUserRepository = siteUserRepository;
        this.userMapper = userMapper;
    }

    @Override
    public CreateUserDto addUser(CreateUserDto user) {
        logger.info("Request for creating user with firstName: {}; lastName: {}; phone: {}; email: {}", user.getFirstName(), user.getLastName(),
                user.getPhone(), user.getEmail());
        SiteUser siteUser = userMapper.fromCreateUserDtoToSiteUser(user);
        return userMapper.fromSiteUserToCreateUserDto(siteUserRepository.save(siteUser));
    }

    @Override
    public ResponseWrapperDto<UserDto> getUsers() {
        logger.info("Request for getting list of all users");
        List<SiteUser> siteUsers = siteUserRepository.findAll();
        List<UserDto> result = new ArrayList<>();
        for (SiteUser user : siteUsers) {
            result.add(userMapper.fromSiteUserToUserDto(user));
        }
        ResponseWrapperDto<UserDto> responseWrapperDto = new ResponseWrapperDto<>();
        responseWrapperDto.setList(result);
        responseWrapperDto.setCount(result.size());
        return responseWrapperDto;
    }

    @Override
    public UserDto updateUser(UserDto userDTO) {
        logger.info("Request for updating user with id: {}", userDTO.getId());
        Optional<SiteUser> siteUser = siteUserRepository.findById(userDTO.getId());
        if (siteUser.isEmpty()) {
            logger.info("There are not user with id {} in list of users", userDTO.getId());
            return null;
        } else {
            SiteUser user = userMapper.fromUserDtoToSiteUser(siteUser.get(), userDTO);
            user.setPassword(siteUser.get().getPassword());
            logger.info("Changes are finished");
            return userMapper.fromSiteUserToUserDto(siteUserRepository.save(user));
        }
    }

    @Override
    public NewPasswordDto setPassword(NewPasswordDto password) {
        Optional<SiteUser> userOptional = siteUserRepository.findSiteUserByPassword(password.getCurrentPassword());
        if (userOptional.isEmpty()) {
            return null;
        } else {
            SiteUser result = userOptional.get();
            logger.info("Request for change password of user with firstName: {}; lastName: {}", result.getFirstName(), result.getLastName());
            result.setPassword(password.getNewPassword());
            siteUserRepository.save(result);
            return password;
        }
    }

    @Override
    public UserDto getUser(Integer id) {
        logger.info("Request for getting information about user with id {}", id);
        Optional<SiteUser> siteUser = siteUserRepository.findById(id);
        return siteUser.map(userMapper::fromSiteUserToUserDto).orElse(null);
    }

    @Override
    public SiteUser findUserByEmail(String email) {
        logger.info("Request for searching user with firstName {}", email);
        Optional<SiteUser> siteUser = siteUserRepository.findSiteUserByEmail(email);
        return siteUser.orElse(null);
    }
}
