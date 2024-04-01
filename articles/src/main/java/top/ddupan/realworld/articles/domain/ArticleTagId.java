package top.ddupan.realworld.articles.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class ArticleTagId implements Serializable {
    private Integer articleId;
    private Integer tagId;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ArticleTagId that = (ArticleTagId) o;
        return getArticleId() != null && Objects.equals(getArticleId(), that.getArticleId())
                && getTagId() != null && Objects.equals(getTagId(), that.getTagId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(articleId, tagId);
    }
}
