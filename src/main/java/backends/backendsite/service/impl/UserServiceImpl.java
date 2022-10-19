package backends.backendsite.service.impl;

import backends.backendsite.dto.NewPasswordDto;
import backends.backendsite.dto.ResponseWrapperDto;
import backends.backendsite.dto.UserDto;
import backends.backendsite.entities.SiteUser;
import backends.backendsite.entities.SiteUserDetails;
import backends.backendsite.mappers.UserMapper;
import backends.backendsite.repositories.SiteUserRepository;
import backends.backendsite.repositories.UserDetailsRepository;
import backends.backendsite.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final SiteUserRepository siteUserRepository;
    private final UserDetailsRepository detailsRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(SiteUserRepository siteUserRepository, UserDetailsRepository detailsRepository,
                           UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.siteUserRepository = siteUserRepository;
        this.detailsRepository = detailsRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseWrapperDto<UserDto> getUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Request for getting list of all users from userName: {}", authentication.getName());
        List<SiteUserDetails> siteUsers = detailsRepository.findAll();
        List<UserDto> result = new ArrayList<>();
        for (SiteUserDetails user : siteUsers) {
            result.add(userMapper.fromSiteUserToUserDto(user));
        }
        ResponseWrapperDto<UserDto> responseWrapperDto = new ResponseWrapperDto<>();
        responseWrapperDto.setList(result);
        responseWrapperDto.setCount(result.size());
        return responseWrapperDto;
    }

    @Override
    public UserDto updateUser(UserDto userDTO, String email) {
        logger.info("Request for updating user with username: {}", email);
        Optional<SiteUser> userOptional = siteUserRepository.findSiteUserByUsername(email);
        if (userOptional.isEmpty()) {
            logger.info("There are not user with username {} in list of users", email);
            return null;
        } else {
            SiteUser siteUser = userOptional.get();
            SiteUser user = userMapper.fromUserDtoToSiteUser(siteUser, userDTO);
            logger.info("Changes are finished");
            SiteUser result = siteUserRepository.save(user);
            return userMapper.fromSiteUserToUserDto(result.getSiteUserDetails());
        }
    }

    @Override
    public NewPasswordDto setPassword(NewPasswordDto password, String email) {
        Optional<SiteUser> userOptional = siteUserRepository.findSiteUserByUsername(email);
        if (userOptional.isEmpty()) {
            logger.info("Пользователя с таким email: *** \"{}\" *** нет.", email);
            return null;
        } else {
            SiteUser result = userOptional.get();
            logger.info("Request for change password of user: \"{}\" from email: {}", result.getUsername(), email);
            if (!passwordEncoder.matches(password.getCurrentPassword(), result.getPassword())) {
                logger.info("Введеный пароль: {} не соответствует текущему паролю: {}. Изменение пароля запрещено", password.getCurrentPassword(), result.getPassword());
                return null;
            } else {
                logger.info("Введеный пароль и текущий пароль совпадают. Изменение пароля допускается.");
                result.setPassword(passwordEncoder.encode(password.getNewPassword()));
                siteUserRepository.save(result);
                return password;
            }
        }
    }

    @Override
    public UserDto getUser(Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Request for getting information about user with id {} from userName: {}", id, authentication.getName());
        Optional<SiteUserDetails> siteUser = detailsRepository.findById(id);
        if (siteUser.isEmpty()) {
            return null;
        } else {
            SiteUserDetails user = siteUser.get();
            return userMapper.fromSiteUserToUserDto(user);
        }
    }

    @Override
    public SiteUser findUserByEmail(String email) {
        logger.info("Request for searching user with firstName {}", email);
        Optional<SiteUser> siteUser = siteUserRepository.findSiteUserByUsername(email);
        return siteUser.orElse(null);
    }
}
