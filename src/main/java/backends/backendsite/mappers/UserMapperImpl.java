package backends.backendsite.mappers;

import backends.backendsite.dto.UserDto;
import backends.backendsite.entities.SiteUserDetails;
import backends.backendsite.entities.SiteUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserMapperImpl implements UserMapper{

    Logger logger = LoggerFactory.getLogger(UserMapperImpl.class);

    @Override
    public SiteUser fromUserDtoToSiteUser(SiteUser siteUser, UserDto userDto) {
        if (!userDto.getFirstName().equals("")) {
            logger.info("Change firstName from {} to {}", siteUser.getSiteUserDetails().getFirstName(), userDto.getFirstName());
            siteUser.getSiteUserDetails().setFirstName(userDto.getFirstName());
        }
        if (!userDto.getLastName().equals("")) {
            logger.info("Change lastName from {} to {}", siteUser.getSiteUserDetails().getLastName(), userDto.getLastName());
            siteUser.getSiteUserDetails().setLastName(userDto.getLastName());
        }
        if (!userDto.getPhone().equals("")) {
            logger.info("Change phone from {} to {}", siteUser.getSiteUserDetails().getPhone(), userDto.getPhone());
            siteUser.getSiteUserDetails().setPhone(userDto.getPhone());
        }
        if (!userDto.getEmail().equals("")) {
            logger.info("Change email from {} to {}", siteUser.getUsername(), userDto.getEmail());
            siteUser.setUsername(userDto.getEmail());
        }
        return siteUser;
    }

    @Override
    public UserDto fromSiteUserToUserDto(SiteUserDetails user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getSiteUser().getUsername());
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setPhone(user.getPhone());
        return userDto;
    }
}
