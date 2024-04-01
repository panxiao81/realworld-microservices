package top.ddupan.realworld.articles.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import top.ddupan.realworld.articles.domain.user.User;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Builder
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleFavorite {
    @EmbeddedId
    private ArticleFavoriteId id;

    @MapsId("articleId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;

    @MapsId("authorId")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @CreatedDate
    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public ArticleFavorite(User user, Article article) {
        this.id = new ArticleFavoriteId(user.getId(), article.getId());
        this.article = article;
        this.user = user;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ArticleFavorite that = (ArticleFavorite) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }
}
