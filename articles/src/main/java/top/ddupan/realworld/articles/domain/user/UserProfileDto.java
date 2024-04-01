package top.ddupan.realworld.articles.domain.user;

import lombok.Getter;
import lombok.Setter;
import top.ddupan.realworld.articles.domain.ArticleFavorite;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class UserProfileDto extends User {
    public UserProfileDto(UUID id, String username, Set<ArticleFavorite> articleFavorites, List<UserProfileDto> followings) {
        super(id, username, articleFavorites);
        this.followings = followings;
    }

    private final List<UserProfileDto> followings;
}
