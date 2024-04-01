package top.ddupan.realworld.articles.application.rest.vo;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("comment")
public record CreateCommentRequest(String body) {
}
