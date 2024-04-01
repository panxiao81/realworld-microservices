package top.ddupan.realworld.articles.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import top.ddupan.realworld.articles.domain.user.User;

import java.util.Collection;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Integer>, QuerydslPredicateExecutor<Article> {
    Optional<Article> findBySlug(String slug);

    Page<Article> findByAuthorInOrderByCreatedAtDesc(Collection<User> authors, Pageable pageable);
}