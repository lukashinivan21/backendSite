package backends.backendsite.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class AdsComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ads_comment_pk")
    private Integer pk;

    @Column(name = "comment_author")
    private Integer author;

    @Column(name = "comment_text")
    private String text;

    @Column(name = "crated_time")
    private LocalDateTime createdAt;

    @ManyToOne
    private Ads ads;

    @ManyToOne
    private SiteUser siteUser;


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
}
