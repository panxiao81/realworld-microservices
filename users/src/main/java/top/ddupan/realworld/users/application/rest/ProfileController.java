package top.ddupan.realworld.users.application.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import top.ddupan.realworld.users.application.service.ProfileService;
import top.ddupan.realworld.users.application.service.UserDto;

@RestController
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/api/profiles/{username}")
    public ProfileResponse getProfile(Jwt me, @PathVariable("username") String target) {
        UserDto profile = profileService.getProfile(me, target);
        return new ProfileResponse(profile);
    }

    @PostMapping("/api/profiles/{username}/follow")
    public ProfileResponse follow(Jwt me, @PathVariable("username") String target) {
        UserDto profile = profileService.follow(me, target);
        return new ProfileResponse(profile);
    }

    @DeleteMapping("/api/profiles/{username}/follow")
    public ProfileResponse unfollow(Jwt me, @PathVariable("username") String target) {
        UserDto profile = profileService.unfollow(me, target);
        return new ProfileResponse(profile);
    }
}
