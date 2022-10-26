package backends.backendsite.service.impl;

import backends.backendsite.dto.NewPasswordDto;
import backends.backendsite.dto.ResponseWrapperDto;
import backends.backendsite.dto.UserDto;
import backends.backendsite.entities.SiteUser;
import backends.backendsite.entities.SiteUserDetails;
import backends.backendsite.exceptionsHandler.exceptions.IncorrectPasswordException;
import backends.backendsite.exceptionsHandler.exceptions.NotAccessException;
import backends.backendsite.exceptionsHandler.exceptions.UserNotFoundException;
import backends.backendsite.mappers.UserMapper;
import backends.backendsite.repositories.AuthorityRepository;
import backends.backendsite.repositories.SiteUserRepository;
import backends.backendsite.repositories.UserDetailsRepository;
import backends.backendsite.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static backends.backendsite.service.StringConstants.*;

/**
 * Class implements methods for working with entity site user and site user details
 */
@Service
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final SiteUserRepository siteUserRepository;
    private final UserDetailsRepository detailsRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;

    public UserServiceImpl(SiteUserRepository siteUserRepository, UserDetailsRepository detailsRepository,
                           UserMapper userMapper, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        this.siteUserRepository = siteUserRepository;
        this.detailsRepository = detailsRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

    //    Method for getting list of users
    @Override
    public ResponseWrapperDto<UserDto> getUsers() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = authorityRepository.findAuthorityByUsername(email).getAuthority();
        logger.info("Request for getting list of all users from userName: {}, with role: {}", email, role);
        List<UserDto> siteUsers = detailsRepository.findAll().stream().map(userMapper::fromSiteUserToUserDto).collect(Collectors.toList());
        if (role.equals(USER)) {
            siteUsers = detailsRepository.findSiteUserDetailsBySiteUserUsername(email).stream().map(userMapper::fromSiteUserToUserDto).collect(Collectors.toList());
        }
        ResponseWrapperDto<UserDto> responseWrapperDto = new ResponseWrapperDto<>();
        responseWrapperDto.setResults(siteUsers);
        responseWrapperDto.setCount(siteUsers.size());
        return responseWrapperDto;
    }

    //    Method for updating data of one authorized user
    @Override
    public UserDto updateUser(UserDto userDTO, String email) {
        String role = authorityRepository.findAuthorityByUsername(email).getAuthority();
        logger.info("Request for updating user with username: {}, with role: {}", email, role);
        SiteUser siteUser = siteUserRepository.findByUsername(email);
        SiteUser user = userMapper.fromUserDtoToSiteUser(siteUser, userDTO);
        logger.info("Changes are finished");
        SiteUser result = siteUserRepository.save(user);
        return userMapper.fromSiteUserToUserDto(result.getSiteUserDetails());
    }

    //    Method for change password of one authorized user
    @Override
    public NewPasswordDto setPassword(NewPasswordDto password, String email) {
        SiteUser result = siteUserRepository.findByUsername(email);
        logger.info("Request for change password of user: \"{}\"", email);
        if (!passwordEncoder.matches(password.getCurrentPassword(), result.getPassword())) {
            logger.info("Введеный пароль: {} не соответствует текущему паролю: {}. Изменение пароля запрещено", password.getCurrentPassword(), result.getPassword());
            throw new IncorrectPasswordException();
        } else {
            logger.info("Введеный пароль и текущий пароль совпадают. Изменение пароля допускается.");
            result.setPassword(passwordEncoder.encode(password.getNewPassword()));
            siteUserRepository.save(result);
            return password;
        }
    }

    //    Method for getting info about one user by id
    @Override
    public UserDto getUser(Integer id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = authorityRepository.findAuthorityByUsername(email).getAuthority();
        logger.info("Request for getting information about user with id {} from userName: {}", id, email);
        Optional<SiteUserDetails> siteUser = detailsRepository.findById(id);
        if (siteUser.isEmpty()) {
            throw new UserNotFoundException();
        } else {
            if (!siteUser.get().getSiteUser().getUsername().equals(email) && role.equals(USER)) {
                throw new NotAccessException();
            } else {
                return userMapper.fromSiteUserToUserDto(siteUser.get());
            }
        }
    }


}
