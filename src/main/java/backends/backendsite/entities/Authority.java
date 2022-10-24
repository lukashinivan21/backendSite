package backends.backendsite.entities;

import javax.persistence.*;

/**
 * Class contains entity of authority
 */
@Entity
@Table(name = "authorities")
public class Authority {

    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "authority")
    private String authority;

    @OneToOne(mappedBy = "authority", cascade = CascadeType.ALL)
    private SiteUser user;

    public Authority() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public SiteUser getUser() {
        return user;
    }
}
