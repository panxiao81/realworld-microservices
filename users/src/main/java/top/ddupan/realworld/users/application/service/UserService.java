package top.ddupan.realworld.users.application.service;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import top.ddupan.realworld.users.application.rest.UpdateUserRequest;
import top.ddupan.realworld.users.domain.User;
import top.ddupan.realworld.users.domain.UserRepository;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto update(Jwt me, UpdateUserRequest request) {
        User user = findUserByJwt(me);
        user.updateUsername(request.username());
        userRepository.saveAndFlush(user);
        return new UserDto(user);
    }

    public UserDto getUser(Jwt me) {
        User user = findUserByJwt(me);

        return new UserDto(user);
    }

    private User findUserByJwt(Jwt me) {
        return userRepository.findById(UUID.fromString(me.getId()))
                .orElseThrow(() -> new NoSuchElementException("User %s not found.".formatted(me.getId())));
    }
}
