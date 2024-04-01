package top.ddupan.realworld.articles.application.service;

import com.querydsl.core.types.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import top.ddupan.realworld.articles.application.rest.vo.CreateArticleRequest;
import top.ddupan.realworld.articles.application.rest.vo.CreateCommentRequest;
import top.ddupan.realworld.articles.application.rest.vo.UpdateArticleRequest;
import top.ddupan.realworld.articles.application.service.dto.ArticleDto;
import top.ddupan.realworld.articles.application.service.dto.CommentDto;
import top.ddupan.realworld.articles.domain.*;
import top.ddupan.realworld.articles.domain.user.User;
import top.ddupan.realworld.articles.domain.user.UserProfileDto;
import top.ddupan.realworld.articles.domain.user.UserProfileRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final TagRepository tagRepository;
    private final ArticleRepository articleRepository;
    private final UserProfileRepository userProfileRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public ArticleDto createArticle(User me, CreateArticleRequest request) {
        Article newArticle = Article.builder()
                .author(me)
                .title(request.description())
                .content(request.body())
                .build();

        for (final Tag invalidTag : request.tags()) {
            final Tag validTag = tagRepository.findByName(invalidTag.getName())
                    .orElseGet(() -> tagRepository.save(invalidTag));
            validTag.tagging(newArticle);
        }

        newArticle = articleRepository.save(newArticle);
        return new ArticleDto(me, newArticle);
    }

    public Page<ArticleDto> getArticles(User me, ArticleFacets facets) {
        Predicate predicate = new ArticleSpecsFactory(facets, userProfileRepository).facets();
        Page<Article> articles = articleRepository.findAll(predicate, facets.getPageable(Sort.by("createdAt")));
        return articles.map(article -> new ArticleDto(me, article));
    }


    public ArticleDto getSingleArticle(User me, String slug) {
        Article article = findBySlug(slug);
        return new ArticleDto(me, article);
    }

    private Article findBySlug(String slug) {
        return articleRepository.findBySlug(slug)
                .orElseThrow(() -> new NoSuchElementException("Article not found by slug: %s".formatted(slug)));
    }

    @Transactional
    public ArticleDto updateArticle(User user, String slug, UpdateArticleRequest request) {
        Article article = findBySlug(slug);

        if (!article.isWrittenByUser(user.getId())) {
            throw new IllegalArgumentException("You can't edit articles written by others.");
        }

        article.updateTitle(request.title());
        article.updateDescription(request.description());
        article.updateContent(request.body());

        articleRepository.save(article);
        return new ArticleDto(user, article);
    }

    @Transactional
    public void deleteArticle(User user, String slug) {
        Article article = findBySlug(slug);

        if (!article.isWrittenByUser(user.getId())) {
            throw new IllegalArgumentException("You can't delete articles written by others.");
        }

        articleRepository.delete(article);
    }

    public Page<ArticleDto> getFeedArticles(User user, Pageable pageable) {
        List<UserProfileDto> followings = userProfileRepository.findById(user.getId())
                .map(UserProfileDto::getFollowings).orElseThrow();

        Set<User> collect = followings.stream().map(userProfileDto -> new User(userProfileDto.getId())).collect(Collectors.toSet());
        return articleRepository.findByAuthorInOrderByCreatedAtDesc(collect, pageable)
                .map(article -> new ArticleDto(user, article));
    }

    public CommentDto createComment(User user, String slug, CreateCommentRequest request) {
        Comment comment = Comment.builder()
                .article(findBySlug(slug))
                .authorId(user.getId())
                .content(request.body())
                .build();

        commentRepository.save(comment);
        return new CommentDto(comment);
    }

    @Transactional
    public List<CommentDto> getArticleComments(User user, String slug) {
        Article article = findBySlug(slug);
        List<Comment> comments = commentRepository.findByArticleOrderByCreatedAtDesc(article);

        return comments.stream()
                .map(CommentDto::new).collect(Collectors.toList());
    }

    public void deleteComment(User user, int id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Comment not found by id: %d".formatted(id)));

        if (!comment.isWritten(user.getId())) {
            throw new IllegalArgumentException("You can't delete comments written by others");
        }

        commentRepository.delete(comment);
    }

    @Transactional
    public ArticleDto favoriteArticle(User user, String slug) {
        Article article = findBySlug(slug);
        user.favorite(article);
        articleRepository.save(article);

        return new ArticleDto(user, article);
    }

    public ArticleDto unfavoriteArticle(User user, String slug) {
        Article article = findBySlug(slug);
        user.unfavorite(article);
        articleRepository.save(article);

        return new ArticleDto(user, article);
    }
}
