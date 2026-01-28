package com.epiprev.server.forum.exception;

import com.epiprev.server.common.exception.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ForumErrorCode implements ErrorCode {

    POST_CREATE_FAILED("POST_CREATE_FAILED", "发帖失败"),

    COMMENT_CREATE_FAILED("COMMENT_CREATE_FAILED", "评论失败"),

    POST_NOT_FOUND("POST_NOT_FOUND", "帖子不存在");

    private final String code;

    private final String message;
}
