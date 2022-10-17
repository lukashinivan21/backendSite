package backends.backendsite.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ads_comment")
public class AdsComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ads_comment_pk")
    private Integer pk;

    @Column(name = "comment_author")
    private Integer author;

    @Column(name = "comment_text")
    private String text;

    @Column(name = "created_time")
    private LocalDateTime createdAt;

    @ManyToOne(cascade = {
            CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "ads_pk")
    private Ads ads;

    @ManyToOne(cascade = {
            CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
//    @JoinColumn(name = "site_user_details_id")
    private SiteUserDetails siteUserDetails;

    public AdsComment() {

    }

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public Integer getAuthor() {
        return author;
    }

    public void setAuthor(Integer author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Ads getAds() {
        return ads;
    }

    public void setAds(Ads ads) {
        this.ads = ads;
    }

    public SiteUserDetails getSiteUserDetails() {
        return siteUserDetails;
    }

}
