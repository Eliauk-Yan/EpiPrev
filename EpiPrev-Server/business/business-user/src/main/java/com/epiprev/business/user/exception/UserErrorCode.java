package com.epiprev.business.user.exception;

import com.epiprev.common.base.exception.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {

    ILLEGAL_USER_STATE("ILLEGAL_USER_STATE", "用户状态非法"),

    USER_NOT_FOUND("USER_NOT_FOUND", "用户没有找到"),

    USER_UPDATE_FAILED("USER_UPDATE_FAILED", "用户更新失败"),

    USERNAME_OR_PASSWORD_ERROR("USERNAME_OR_PASSWORD_ERROR", "用户名或密码错误"),

    USER_REGISTER_FAILED("USER_REGISTER_FAILED", "用户注册失败"),

    USER_OPERATE_STREAM_INSERT_FAILED("USER_OPERATE_STREAM_INSERT_FAILED", "用户操作流水插入失败"),

    USER_STATUS_INVALID("USER_STATUS_INVALID", "无效的用户状态");

    private final String code;

    private final String message;
}
