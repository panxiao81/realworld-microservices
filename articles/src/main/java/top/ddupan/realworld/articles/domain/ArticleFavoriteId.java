package top.ddupan.realworld.articles.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleFavoriteId implements Serializable {
    private UUID authorId;
    private Integer articleId;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ArticleFavoriteId that = (ArticleFavoriteId) o;
        return getAuthorId() != null && Objects.equals(getAuthorId(), that.getAuthorId())
                && getArticleId() != null && Objects.equals(getArticleId(), that.getArticleId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(authorId, articleId);
    }
}
