package top.ddupan.realworld.articles.application.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import top.ddupan.realworld.articles.domain.Comment;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link top.ddupan.realworld.articles.domain.Comment}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CommentDto(Integer id, String body, LocalDateTime createdAt,
                         LocalDateTime updatedAt) implements Serializable {
    public CommentDto(Comment comment) {
        this(comment.getId(), comment.getContent(), comment.getCreatedAt(), comment.getUpdatedAt());
    }
}