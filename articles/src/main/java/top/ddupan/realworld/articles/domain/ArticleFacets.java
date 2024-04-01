package top.ddupan.realworld.articles.domain;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record ArticleFacets(String tag, String author, String favorited, int offset, int limit) {
    public ArticleFacets {
        if (offset < 0) {
            offset = 0;
        }
        if (limit < 0 || limit > 100) {
            limit = 20;
        }
    }

    public Pageable getPageable() {
        return PageRequest.of(offset, limit);
    }

    public Pageable getPageable(Sort sort) {
        return PageRequest.of(offset, limit, sort);
    }
}
