package top.ddupan.realworld.users.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ddupan.realworld.users.domain.User;
import top.ddupan.realworld.users.domain.UserRepository;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserDto getProfile(Jwt me, String targetUsername) {
        User user = findUserByJwt(me);
        return this.getProfile(user, findUserByUsername(targetUsername));
    }

    @Transactional(readOnly = true)
    public UserDto getProfile(User me, User target) {
        return new UserDto(me, target);
    }

    @Transactional
    public UserDto follow(Jwt me, String targetUsername) {
        User user = findUserByJwt(me);
        return this.follow(user, findUserByUsername(targetUsername));
    }

    @Transactional
    public UserDto follow(User me, User target) {
        me.follow(target);
        userRepository.save(me);
        return new UserDto(me, target);
    }

    @Transactional
    public UserDto unfollow(Jwt me, String targetUsername) {
        User user = findUserByJwt(me);
        return this.unfollow(user, findUserByUsername(targetUsername));
    }

    @Transactional
    public UserDto unfollow(User me, User target) {
        me.unfollow(target);
        userRepository.save(me);
        return new UserDto(me, target);
    }

    private User findUserByUsername(String username) {
        String message = "User(`%s`) not found".formatted(username);
        return userRepository.findByUsername(username).orElseThrow(() -> new NoSuchElementException(message));
    }

    private User findUserByJwt(Jwt jwt) {
        return userRepository.findById(UUID.fromString(jwt.getSubject())).orElseThrow(() -> new NoSuchElementException(
                "User %s not found".formatted(jwt.getSubject())
        ));
    }
}
