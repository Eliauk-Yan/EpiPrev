package com.epiprev.auth.exception;

import com.epiprev.common.base.exception.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    ILLEGAL_LOGIN("ILLEGAL_LOGIN", "非法登录");

    private final String code;

    private final String message;

}
