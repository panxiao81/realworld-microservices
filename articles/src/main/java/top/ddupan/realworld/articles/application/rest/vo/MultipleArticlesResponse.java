package top.ddupan.realworld.articles.application.rest.vo;

import top.ddupan.realworld.articles.application.service.dto.ArticleDto;

import java.util.List;

public record MultipleArticlesResponse(List<ArticleDto> articles, int articlesCount) {
}
