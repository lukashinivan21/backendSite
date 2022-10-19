package backends.backendsite.mappers;

import backends.backendsite.dto.UserDto;
import backends.backendsite.entities.SiteUserDetails;
import backends.backendsite.entities.SiteUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class UserMapperImpl implements UserMapper {

    Logger logger = LoggerFactory.getLogger(UserMapperImpl.class);

//    private final UserDetailsManager manager;
//
//    public UserMapperImpl(UserDetailsManager manager) {
//        this.manager = manager;
//    }

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
//        if (userDto.getEmail() != null) {
//            logger.info("Change email from {} to {}", siteUser.getUsername(), userDto.getEmail());
//            siteUser.getAuthority().setUsername(userDto.getEmail());
//            UserDetails oldDetails = manager.loadUserByUsername(siteUser.getUsername());
//            UserDetails newDetails = new User(userDto.getEmail(), oldDetails.getPassword(), oldDetails.getAuthorities());
//            manager.updateUser(newDetails);
//            siteUser.setUsername(userDto.getEmail());
//        }
        siteUser.setSiteUserDetails(userDetails);
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
