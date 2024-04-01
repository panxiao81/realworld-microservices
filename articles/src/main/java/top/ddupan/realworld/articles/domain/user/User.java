package top.ddupan.realworld.articles.domain.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.security.oauth2.jwt.Jwt;
import top.ddupan.realworld.articles.domain.Article;
import top.ddupan.realworld.articles.domain.ArticleFavorite;

import java.util.*;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Builder
public class User {
    @Id
    private UUID id;

    @Column(length = 30, nullable = false, unique = true)
    private String username;

    @Transient
    private Jwt token;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @Builder.Default
    private Set<ArticleFavorite> articleFavorites = new HashSet<>();

    public User(UUID id) {
        this.id = id;
        this.articleFavorites = new HashSet<>();
    }

    public User(String id) {
        this(UUID.fromString(id));
    }

    public User(UUID id, String username, Set<ArticleFavorite> articleFavorites) {
        this(id, username, null, articleFavorites);
    }

    public User processToken(Jwt jwt) {
        this.token = jwt;
        return this;
    }

    public boolean isAlreadyFavorite(Article article) {
        ArticleFavorite articleFavorite = new ArticleFavorite(this, article);
        return this.articleFavorites.stream().anyMatch(articleFavorite::equals);
    }

    public void favorite(@NotNull Article article) {
        if (isAlreadyFavorite(article)) {
            return;
        }

        ArticleFavorite articleFavorite = new ArticleFavorite(this, article);
        // Add favorite articles
        this.articleFavorites.add(articleFavorite);
        // Add this author to favorite
        articleFavorite.getArticle().getFavoriteUsers().add(articleFavorite);
    }

    public void unfavorite(@NotNull Article article) {
        findArticleFavorite(article).ifPresent(articleFavorite -> {
            // remove favorite article
            this.articleFavorites.remove(articleFavorite);
            // remove author from article
            articleFavorite.getArticle().getFavoriteUsers().remove(articleFavorite);
        });
    }

    private Optional<ArticleFavorite> findArticleFavorite(@NotNull Article article) {
        return this.articleFavorites.stream().filter(articleFavorite -> articleFavorite.getArticle().equals(article)).findFirst();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }
}
