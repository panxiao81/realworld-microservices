package top.ddupan.realworld.users.domain;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findByUsername(String username);

    Optional<User> findById(UUID uuid);

    void saveAndFlush(User user);

    void save(User me);
}