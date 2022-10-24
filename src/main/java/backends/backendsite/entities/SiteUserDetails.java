package backends.backendsite.entities;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Class contains entity of user's info
 */
@Entity
@Table(name = "user_details")
public class SiteUserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "user_first_name")
    private String firstName;

    @Column(name = "user_last_name")
    private String lastName;

    @Column(name = "user_phone")
    private String phone;

    @OneToOne(mappedBy = "siteUserDetails", cascade = CascadeType.ALL)
//    @JoinColumn(name = "username_of_user")
    private SiteUser siteUser;

    @OneToMany(mappedBy = "siteUserDetails", cascade = CascadeType.ALL)
    private Set<Ads> siteUserAds;

    @OneToMany(mappedBy = "siteUserDetails", cascade = CascadeType.ALL)
    private List<AdsComment> usersComments;

    public SiteUserDetails() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public SiteUser getSiteUser() {
        return siteUser;
    }

    public void setSiteUser(SiteUser siteUser) {
        this.siteUser = siteUser;
    }

    public Set<Ads> getSiteUserAds() {
        return siteUserAds;
    }

    public List<AdsComment> getUsersComments() {
        return usersComments;
    }
}
