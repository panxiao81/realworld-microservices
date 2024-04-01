package top.ddupan.realworld.articles.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import top.ddupan.realworld.articles.domain.user.User;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Slf4j
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User author;

    @Column(length = 50, nullable = false)
    private String description;

    @Column(length = 50, unique = true, nullable = false)
    private String title;

    @Column(length = 50, unique = true, nullable = false)
    private String slug;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ArticleFavorite> favoriteUsers = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ArticleTag> includeTags = new HashSet<>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    private Article(Integer id, User author, String description, String title, String content) {
        this.id = id;
        this.author = author;
        this.description = description;
        this.title = title;
        this.content = content;

        this.slug = createSlugByTitle(title);
        this.favoriteUsers = new HashSet<>();
        this.includeTags = new HashSet<>();
        this.createdAt = LocalDateTime.now();
    }

    private String createSlugByTitle(@NotNull String title) {
        return title.toLowerCase().replaceAll("\\s+", "-");
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Article article = (Article) o;
        return getId() != null && Objects.equals(getId(), article.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    public void updateTitle(String title) {
        if (title == null || title.isBlank()) {
            return;
        }
        this.title = title;
        this.slug = createSlugByTitle(title);
    }

    public void updateDescription(@NotBlank String description) {
        if (description == null || description.isBlank()) {
            return;
        }
        this.description = description;
    }

    public void updateContent(@NotBlank String content) {
        if (content == null || content.isBlank()) {
            return;
        }
        this.content = content;
    }

    public boolean isWrittenByUser(@NotNull UUID userId) {
        return this.author.getId().equals(userId);
    }

    public int numberOfLikes() {
        return this.favoriteUsers.size();
    }

    public void addTag(@NotNull Tag tag) {
        ArticleTag articleTag = new ArticleTag(this, tag);
        if (this.includeTags.stream().anyMatch(articleTag::equals)) {
            return;
        }

        this.includeTags.add(articleTag);
    }

    public List<Tag> getTags() {
        return this.includeTags.stream().map(ArticleTag::getTag).toList();
    }

    public List<String> getTagNames() {
        return this.getTags().stream().map(Tag::getName).sorted().toList();
    }
}
