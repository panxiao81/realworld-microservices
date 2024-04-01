package top.ddupan.realworld.articles.application.rest;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import top.ddupan.realworld.articles.application.rest.vo.*;
import top.ddupan.realworld.articles.application.service.ArticleService;
import top.ddupan.realworld.articles.application.service.dto.ArticleDto;
import top.ddupan.realworld.articles.application.service.dto.CommentDto;
import top.ddupan.realworld.articles.domain.ArticleFacets;
import top.ddupan.realworld.articles.domain.user.User;
import top.ddupan.realworld.articles.domain.user.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
public class ArticleController {
    private final ArticleService articleService;
    private final UserRepository userRepository;

    public ArticleController(ArticleService articleService, UserRepository userRepository) {
        this.articleService = articleService;
        this.userRepository = userRepository;
    }

    private User getUser(Jwt me) {
        return userRepository.getReferenceById(UUID.fromString(me.getId()));
    }

    @PostMapping("/api/articles")
    public SingleArticleResponse createArticle(
            @AuthenticationPrincipal Jwt me,
            @RequestBody CreateArticleRequest request
    ) {
        User user = getUser(me);
        ArticleDto article = articleService.createArticle(user, request);
        return new SingleArticleResponse(article);
    }


    @GetMapping("/api/articles")
    public MultipleArticlesResponse getArticles(
            @AuthenticationPrincipal Jwt me,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "favorited", required = false) String favorited,
            @RequestParam(value = "limit", required = false, defaultValue = "20") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset
    ) {
        User user = getUser(me);
        ArticleFacets facets = new ArticleFacets(tag, author, favorited, offset, limit);
        Page<ArticleDto> articles = articleService.getArticles(user, facets);
        return new MultipleArticlesResponse(articles.getContent(), articles.getNumberOfElements());
    }

    @GetMapping("/api/articles/{slug}")
    public SingleArticleResponse getSingleArticle(@AuthenticationPrincipal Jwt me, @PathVariable String slug) {
        User user = getUser(me);
        ArticleDto articleDto = articleService.getSingleArticle(user, slug);
        return new SingleArticleResponse(articleDto);
    }

    @PutMapping("/api/articles/{slug}")
    public SingleArticleResponse updateArticle(
            @AuthenticationPrincipal Jwt me,
            @PathVariable String slug,
            @RequestBody UpdateArticleRequest request
    ) {
        User user = getUser(me);
        ArticleDto article = articleService.updateArticle(user, slug, request);
        return new SingleArticleResponse(article);
    }

    @DeleteMapping("/api/articles/{slug}")
    public void deleteArticle(
            @AuthenticationPrincipal Jwt me,
            @PathVariable String slug
    ) {
        User user = getUser(me);
        articleService.deleteArticle(user, slug);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/articles/feed")
    public MultipleArticlesResponse getFeedArticles(
            @AuthenticationPrincipal Jwt me,
            @RequestParam(value = "limit", required = false, defaultValue = "20") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset
    ) {
        User user = getUser(me);
        ArticleFacets facets = new ArticleFacets(null, null, null, offset, limit);
        Page<ArticleDto> articleDtos = articleService.getFeedArticles(user, facets.getPageable());
        return new MultipleArticlesResponse(articleDtos.getContent(), articleDtos.getNumberOfElements());
    }

    @PostMapping("/api/articles/{slug}/comments")
    public SingleCommentResponse createComment(
            @AuthenticationPrincipal Jwt me,
            @PathVariable String slug,
            @RequestBody CreateCommentRequest request
    ) {
        User user = getUser(me);
        CommentDto commentDto = articleService.createComment(user, slug, request);
        return new SingleCommentResponse(commentDto);
    }

    @GetMapping("/api/articles/{slug}/comments")
    public MultipleCommentsResponse getComments(
            @AuthenticationPrincipal Jwt me,
            @PathVariable String slug
    ) {
        User user = getUser(me);
        List<CommentDto> comments = articleService.getArticleComments(user, slug);
        return new MultipleCommentsResponse(comments);
    }

    @DeleteMapping("/api/articles/{slug}/comments/{id}")
    public void deleteComment(
            @AuthenticationPrincipal Jwt me,
            @PathVariable String slug,
            @PathVariable int id
    ) {
        User user = getUser(me);
        articleService.deleteComment(user, id);
    }

    @PostMapping("/api/articles/{slug}/favorite")
    public SingleArticleResponse favoriteArticle(
            @AuthenticationPrincipal Jwt me,
            @PathVariable String slug
    ) {
        User user = getUser(me);
        ArticleDto article = articleService.favoriteArticle(user, slug);
        return new SingleArticleResponse(article);
    }

    @DeleteMapping("/api/articles/{slug}/favorite")
    public SingleArticleResponse unfavoriteArticle(
            @AuthenticationPrincipal Jwt me,
            @PathVariable String slug
    ) {
        User user = getUser(me);
        ArticleDto article = articleService.unfavoriteArticle(user, slug);
        return new SingleArticleResponse(article);
    }

    public ArticleService articleService() {
        return articleService;
    }

    public UserRepository userRepository() {
        return userRepository;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ArticleController) obj;
        return Objects.equals(this.articleService, that.articleService) &&
                Objects.equals(this.userRepository, that.userRepository);
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleService, userRepository);
    }

    @Override
    public String toString() {
        return "ArticleController[" +
                "articleService=" + articleService + ", " +
                "userRepository=" + userRepository + ']';
    }

}
