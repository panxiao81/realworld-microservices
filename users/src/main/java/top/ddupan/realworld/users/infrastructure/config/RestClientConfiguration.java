package top.ddupan.realworld.users.infrastructure.config;

import org.springframework.boot.autoconfigure.web.client.RestClientBuilderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration(proxyBeanMethods = false)
public class RestClientConfiguration {
    @Bean
    public RestClient restClient(RestClientBuilderConfigurer configurer) {
        return configurer.configure(RestClient.builder())
                .build();
    }
}
