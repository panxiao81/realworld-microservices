package top.ddupan.realworld.articles.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByArticleOrderByCreatedAtDesc(Article article);
}
