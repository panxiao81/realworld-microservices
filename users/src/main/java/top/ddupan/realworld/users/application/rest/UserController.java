package top.ddupan.realworld.users.application.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.ddupan.realworld.users.application.service.UserDto;
import top.ddupan.realworld.users.application.service.UserService;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "oauth2")
public class UserController {
    private final UserService userService;

    @GetMapping("/api/user")
    public UserResponse getCurrentUser(@AuthenticationPrincipal Jwt me) {
        UserDto userDto = userService.getUser(me);
        return new UserResponse(userDto);
    }

    @PutMapping("/api/user")
    public UserResponse updateCurrentUser(@AuthenticationPrincipal Jwt me, @RequestBody UpdateUserRequest request) {
        UserDto userDto = userService.update(me, request);
        return new UserResponse(userDto);
    }
}
