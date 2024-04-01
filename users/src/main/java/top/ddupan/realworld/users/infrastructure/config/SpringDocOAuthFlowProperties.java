package top.ddupan.realworld.users.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@ConfigurationProperties(prefix = "springdoc.oauth-flow")
@EnableConfigurationProperties(SpringDocOAuthFlowProperties.class)
public class SpringDocOAuthFlowProperties {
    String token;
    String auth;
}
