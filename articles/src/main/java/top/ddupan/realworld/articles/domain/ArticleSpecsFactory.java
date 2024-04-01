package top.ddupan.realworld.articles.domain;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import top.ddupan.realworld.articles.domain.user.UserProfileDto;
import top.ddupan.realworld.articles.domain.user.UserProfileRepository;

import java.util.Optional;
import java.util.UUID;

public class ArticleSpecsFactory {
    private final ArticleFacets facets;
    private final UserProfileRepository userProfileRepository;
    private Optional<UserProfileDto> author;
    private Optional<UserProfileDto> favorited;

    public Predicate facets() {
        QArticle article = QArticle.article;

        return Optional.ofNullable(facets.tag())
                .map(article.includeTags.any().tag.name::eq)
                .orElse(Expressions.TRUE)
                .and(this.author.map(UserProfileDto::getId)
                        .map(article.author.id::eq)
                        .orElse(Expressions.TRUE))
                .and(this.favorited.map(UserProfileDto::getId)
                        .map(article.favoriteUsers.any().user.id::eq)
                        .orElse(Expressions.TRUE));
    }

    public ArticleSpecsFactory(ArticleFacets facets, UserProfileRepository userProfileRepository) {
        this.facets = facets;
        this.userProfileRepository = userProfileRepository;
        findUserProfile();
    }

    private void findUserProfile() {
        this.author = Optional.ofNullable(facets.author())
                .map(UUID::fromString)
                .map(uuid -> userProfileRepository.findById(uuid).orElseThrow());
        this.favorited = Optional.ofNullable(facets.favorited())
                .map(UUID::fromString)
                .map(uuid -> userProfileRepository.findById(uuid).orElseThrow());
    }
}
