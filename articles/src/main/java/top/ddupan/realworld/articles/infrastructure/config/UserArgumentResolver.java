package top.ddupan.realworld.articles.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import top.ddupan.realworld.articles.domain.user.User;
import top.ddupan.realworld.articles.domain.user.UserRepository;

import java.util.UUID;

@RequiredArgsConstructor
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            // TODO: replace null with a empty User class
            return null;
        }

        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            UUID userId = UUID.fromString(jwtAuthenticationToken.getName().strip());
            Jwt token = jwtAuthenticationToken.getToken();
            return userRepository.findById(userId)
                    .map(user -> user.processToken(token))
                    .orElseGet(() -> {
                        User user = User.builder()
                                .id(userId)
                                .username(jwtAuthenticationToken.getToken().getClaimAsString("preferred_name"))
                                .token(token)
                                .build();
                        return userRepository.saveAndFlush(user);
                    });
        }
        return null;
    }
}
