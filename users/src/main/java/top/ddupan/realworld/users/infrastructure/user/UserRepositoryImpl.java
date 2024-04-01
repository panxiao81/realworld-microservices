package top.ddupan.realworld.users.infrastructure.user;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.ddupan.realworld.users.domain.User;
import top.ddupan.realworld.users.domain.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
class UserRepositoryImpl implements UserRepository {
    private final UserInfoService userInfoService;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public Optional<User> findById(UUID id) {
        User user = entityManager.find(User.class, id);
        if (user == null) {
            user = new User(id);
            entityManager.persist(user);
        }
        return Optional.of(user);
    }

    @Override
    @Transactional
    public void saveAndFlush(User user) {
        entityManager.merge(user);
        entityManager.flush();
    }

    @Override
    @Transactional
    public Optional<User> findByUsername(String username) {
        UserinfoVO userinfo = userInfoService.getUserinfoByUsername(username);
        return this.findById(userinfo.id());
    }

    @Override
    @Transactional
    public void save(User user) {
        entityManager.merge(user);
    }
}
