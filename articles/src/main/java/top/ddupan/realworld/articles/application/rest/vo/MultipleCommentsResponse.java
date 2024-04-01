package top.ddupan.realworld.articles.application.rest.vo;

import top.ddupan.realworld.articles.application.service.dto.CommentDto;

import java.util.List;

public record MultipleCommentsResponse(List<CommentDto> comments) {
}
