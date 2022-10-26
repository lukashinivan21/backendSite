package backends.backendsite.entities;

import javax.persistence.*;

/**
 * Class contains entity of user
 */
@Entity
@Table(name = "users")
public class SiteUser {

    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    private boolean enabled;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "site_user_details_id")
    private SiteUserDetails siteUserDetails;

    public SiteUser() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String email) {
        this.username = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public SiteUserDetails getSiteUserDetails() {
        return siteUserDetails;
    }

    public void setSiteUserDetails(SiteUserDetails userDetails) {
        this.siteUserDetails = userDetails;
    }

}
