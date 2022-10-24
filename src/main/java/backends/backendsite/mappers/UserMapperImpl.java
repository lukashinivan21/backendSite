package backends.backendsite.mappers;

import backends.backendsite.dto.UserDto;
import backends.backendsite.entities.SiteUserDetails;
import backends.backendsite.entities.SiteUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Class implements methods for converting site user entity to user dto and back again
 */
@Service
public class UserMapperImpl implements UserMapper {

    Logger logger = LoggerFactory.getLogger(UserMapperImpl.class);

//    method converts from user dto to site user entity and site user details entity
    @Override
    public SiteUser fromUserDtoToSiteUser(SiteUser siteUser, UserDto userDto) {
        SiteUserDetails userDetails = siteUser.getSiteUserDetails();
        if (userDto.getFirstName() != null) {
            logger.info("Change firstName from {} to {}", siteUser.getSiteUserDetails().getFirstName(), userDto.getFirstName());
            userDetails.setFirstName(userDto.getFirstName());
        }
        if (userDto.getLastName() != null) {
            logger.info("Change lastName from {} to {}", siteUser.getSiteUserDetails().getLastName(), userDto.getLastName());
            userDetails.setLastName(userDto.getLastName());
        }
        if (userDto.getPhone() != null) {
            logger.info("Change phone from {} to {}", siteUser.getSiteUserDetails().getPhone(), userDto.getPhone());
            userDetails.setPhone(userDto.getPhone());
        }
        siteUser.setSiteUserDetails(userDetails);
        return siteUser;
    }

//    method converts from site user details entity to user dto
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
