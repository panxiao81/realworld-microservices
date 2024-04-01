package top.ddupan.realworld.articles.application.rest.vo;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import top.ddupan.realworld.articles.application.service.dto.ArticleDto;

@RegisterReflectionForBinding
public record SingleArticleResponse(ArticleDto article) {
}
