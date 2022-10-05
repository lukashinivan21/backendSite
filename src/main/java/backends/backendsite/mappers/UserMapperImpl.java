package backends.backendsite.mappers;

import backends.backendsite.dto.CreateUserDto;
import backends.backendsite.dto.UserDto;
import backends.backendsite.entities.SiteUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserMapperImpl implements UserMapper{

    Logger logger = LoggerFactory.getLogger(UserMapperImpl.class);

    @Override
    public SiteUser fromCreateUserDtoToSiteUser(CreateUserDto userDto) {
        SiteUser siteUser = new SiteUser();
        siteUser.setFirstName(userDto.getFirstName());
        siteUser.setLastName(userDto.getLastName());
        siteUser.setPhone(userDto.getPhone());
        siteUser.setEmail(userDto.getEmail());
        siteUser.setPassword(userDto.getPassword());
        return siteUser;
    }

    @Override
    public CreateUserDto fromSiteUserToCreateUserDto(SiteUser user) {
        CreateUserDto userDto = new CreateUserDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setPhone(user.getPhone());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        return userDto;
    }

    @Override
    public SiteUser fromUserDtoToSiteUser(SiteUser siteUser, UserDto userDto) {
        if (!userDto.getFirstName().equals("")) {
            logger.info("Change firstName from {} to {}", siteUser.getFirstName(), userDto.getFirstName());
            siteUser.setFirstName(userDto.getFirstName());
        }
        if (!userDto.getLastName().equals("")) {
            logger.info("Change lastName from {} to {}", siteUser.getLastName(), userDto.getLastName());
            siteUser.setLastName(userDto.getLastName());
        }
        if (!userDto.getPhone().equals("")) {
            logger.info("Change phone from {} to {}", siteUser.getPhone(), userDto.getPhone());
            siteUser.setPhone(userDto.getPhone());
        }
        if (!userDto.getEmail().equals("")) {
            logger.info("Change email from {} to {}", siteUser.getEmail(), userDto.getEmail());
            siteUser.setEmail(userDto.getEmail());
        }
        return siteUser;
    }

    @Override
    public UserDto fromSiteUserToUserDto(SiteUser user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setPhone(user.getPhone());
        userDto.setEmail(user.getEmail());
        return userDto;
    }
}
