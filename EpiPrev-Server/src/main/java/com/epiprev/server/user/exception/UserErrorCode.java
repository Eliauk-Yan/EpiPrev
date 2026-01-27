package com.epiprev.server.user.exception;

import com.epiprev.server.common.exception.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {

    USER_NOT_FOUND("USER_NOT_FOUND", "用户不存在");

    private final String code;

    private final String message;
}
