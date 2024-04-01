package top.ddupan.realworld.articles.application.rest.vo;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

import java.util.List;

@RegisterReflectionForBinding
public record ListOfTagsResponse(List<String> tags) {
}
