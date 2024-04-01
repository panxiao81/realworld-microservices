package top.ddupan.realworld.articles.domain.user;

import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository {
    Optional<UserProfileDto> findById(UUID id);
}
