package com.epiprev.server.auth.exception;

import com.epiprev.server.common.exception.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    USER_NOT_EXIST("USER_NOT_EXIST", "用户不存在"),

    USERNAME_DUPLICATED("USERNAME_DUPLICATED", "用户名已经被使用"),

    PASSWORD_ERROR("PASSWORD_ERROR", "用户名密码错误"),

    USER_REGISTER_FAILED("USER_REGISTER_FAILED", "用户注册失败");

    private final String code;

    private final String message;
}
