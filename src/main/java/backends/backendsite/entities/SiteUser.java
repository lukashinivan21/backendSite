package backends.backendsite.entities;

import javax.persistence.*;

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
    @JoinColumn(name = "authority_username")
    private Authority authority;

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

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public SiteUserDetails getSiteUserDetails() {
        return siteUserDetails;
    }

    public void setSiteUserDetails(SiteUserDetails userDetails) {
        this.siteUserDetails = userDetails;
    }

    public Authority getAuthority() {
        return authority;
    }
}
