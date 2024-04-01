package top.ddupan.realworld.articles.application.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ddupan.realworld.articles.application.rest.vo.ListOfTagsResponse;
import top.ddupan.realworld.articles.application.service.TagService;

import java.util.List;
import java.util.Objects;

@RestController
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/api/tags")
    public ListOfTagsResponse getTags() {
        List<String> tags = tagService.getTagNames();
        return new ListOfTagsResponse(tags);
    }

    public TagService tagService() {
        return tagService;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (TagController) obj;
        return Objects.equals(this.tagService, that.tagService);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagService);
    }

    @Override
    public String toString() {
        return "TagController[" +
                "tagService=" + tagService + ']';
    }

}
