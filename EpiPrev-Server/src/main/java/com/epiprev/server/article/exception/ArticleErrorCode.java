package com.epiprev.server.article.exception;

import com.epiprev.server.common.exception.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ArticleErrorCode implements ErrorCode {

    ARTICLE_NOT_FOUND("ARTICLE_NOT_FOUND", "没有找到文章");

    private final String code;

    private final String message;

}
