package top.ddupan.realworld.articles.application.service;

import org.springframework.stereotype.Service;
import top.ddupan.realworld.articles.domain.Tag;
import top.ddupan.realworld.articles.domain.TagRepository;

import java.util.List;


@Service
public record TagService(TagRepository tagRepository) {
    public List<String> getTagNames() {
        return tagRepository.findAll().stream().map(Tag::getName).toList();
    }
}
