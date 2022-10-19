package backends.backendsite.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ads")
public class Ads {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ads_pk")
    private Integer pk;

    @Column(name = "ads_author")
    private Integer author;

    @Column(name = "ads_image")
    private String image;

    @Column(name = "ads_price")
    private Integer price;

    @Column(name = "ads_title")
    private String title;

    @Column(name = "ads_description")
    private String description;

    @ManyToOne(cascade = {
            CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
//    @JoinColumn(name = "site_user_details_id")
    private SiteUserDetails siteUserDetails;

    @OneToMany(mappedBy = "ads", cascade = CascadeType.ALL)
    private List<AdsComment> comments;

    @OneToMany(mappedBy = "ads", cascade = CascadeType.ALL)
    private List<Image> images;

    public Ads() {

    }

    public Integer getAuthor() {
        return author;
    }

    public void setAuthor(Integer author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SiteUserDetails getSiteUserDetails() {
        return siteUserDetails;
    }

    public void setSiteUserDetails(SiteUserDetails siteUserDetails) {
        this.siteUserDetails = siteUserDetails;
    }

    public List<AdsComment> getComments() {
        return comments;
    }

    public List<Image> getImages() {
        return images;
    }
}
