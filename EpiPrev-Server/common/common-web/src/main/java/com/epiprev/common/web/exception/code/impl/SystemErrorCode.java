package com.epiprev.common.web.exception.code.impl;

import com.epiprev.common.web.exception.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @classname SystemErrorCode
 * @description 系统错误码
 * @date 2025/12/01
 * @created by YanShijie
 */
@Getter
@AllArgsConstructor
public enum SystemErrorCode implements ErrorCode {

    /**
     * 系统内部错误
     */
    INTERNAL_SERVER_ERROR("500", "系统内部错误"),

    /**
     * 服务不可用
     */
    SERVICE_UNAVAILABLE("503", "服务不可用"),

    /**
     * 系统繁忙
     */
    SYSTEM_BUSY("504", "系统繁忙，请稍后重试"),

    /**
     * 未知错误
     */
    UNKNOWN_ERROR("599", "未知错误");

    private final String code;

    private final String message;
}
