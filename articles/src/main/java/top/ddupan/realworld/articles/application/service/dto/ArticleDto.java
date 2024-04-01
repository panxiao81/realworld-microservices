package top.ddupan.realworld.articles.application.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import top.ddupan.realworld.articles.domain.Article;
import top.ddupan.realworld.articles.domain.user.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link top.ddupan.realworld.articles.domain.Article}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ArticleDto(
        String slug,
        String title,
        String description,
        String body,
        List<String> tagList,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean favorited,
        int favoritesCount
) implements Serializable {
    public ArticleDto(User me, Article article) {
        this(
                article.getSlug(),
                article.getTitle(),
                article.getDescription(),
                article.getContent(),
                article.getTagNames(),
                article.getCreatedAt(),
                article.getUpdatedAt(),
                me != null && me.isAlreadyFavorite(article),
                article.numberOfLikes()
        );
    }
}