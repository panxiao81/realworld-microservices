package top.ddupan.realworld.users.application.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import top.ddupan.realworld.users.domain.User;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link top.ddupan.realworld.users.domain.User}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserDto(UUID id, String username, Boolean following) implements Serializable {
    public UserDto(User user) {
        this(user.getId(), user.getUsername(), null);
    }

    public UserDto(User from, User to) {
        this(to.getId(), to.getUsername(), from != null && from.isAlreadyFollowing(to));
    }
}