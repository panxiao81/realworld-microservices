package top.ddupan.realworld.users.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
@Builder
@Slf4j
public class User {
    @Id
    private UUID id;

    @Transient
    private String username;

    @Transient
    private Jwt token;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "from", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Follow> following = new HashSet<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "to", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Follow> follower = new HashSet<>();

    public User(UUID id) {
        this.id = id;
    }

    public User(String id) {
        this(UUID.fromString(id));
    }

    public User processToken(Jwt jwt) {
        this.token = jwt;
        return this;
    }

    public boolean isAlreadyFollowing(@NotNull User target) {
        Follow follow = new Follow(this, target);
        return this.following.stream().anyMatch(follow::equals);
    }

    public User follow(@NotNull User target) {
        if (isAlreadyFollowing(target)) {
            return this;
        }

        Follow follow = new Follow(this, target);
        addFollowingToCurrentUser(follow);
        addFollowerToTargetUser(follow);

        return this;
    }


    public User unfollow(@NotNull User target) {
        findFollowing(target).ifPresent(follow -> {
            this.removeFollowing(follow);
            target.removeFollower(follow);
        });

        return this;
    }

    public List<User> followUsers() {
        return this.following.stream().map(Follow::getTo).toList();
    }

    public void updateUsername(@NotNull String username) {
        if (username.isBlank() || this.username.equals(username)) {
            log.info("Username(`{}`) is blank or same as current username.", username);
            return;
        }

        this.username = username;
    }

    private void addFollowingToCurrentUser(Follow follow) {
        this.following.add(follow);
    }

    private void addFollowerToTargetUser(Follow follow) {
        follow.getTo().getFollower().add(follow);
    }

    private Optional<Follow> findFollowing(User target) {
        return this.following.stream().filter(target::isFollowing).findFirst();
    }

    private boolean isFollowing(@NotNull Follow follow) {
        return follow.getTo().equals(this);
    }

    private void removeFollowing(@NotNull Follow follow) {
        this.following.remove(follow);
    }

    private void removeFollower(@NotNull Follow follow) {
        this.follower.remove(follow);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }
}
