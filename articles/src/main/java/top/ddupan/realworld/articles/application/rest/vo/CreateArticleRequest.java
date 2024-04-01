package top.ddupan.realworld.articles.application.rest.vo;

import com.fasterxml.jackson.annotation.JsonRootName;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import top.ddupan.realworld.articles.domain.Tag;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@JsonRootName("article")
@RegisterReflectionForBinding
public record CreateArticleRequest(String title, String description, String body, List<String> tagList) {
    public Set<Tag> tags() {
        return tagList.stream().map(Tag::new).collect(Collectors.toSet());
    }
}
