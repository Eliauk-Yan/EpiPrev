package com.epiprev.server.common.exception.code.impl;

import com.epiprev.server.common.exception.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BusinessErrorCode implements ErrorCode {

    /**
     * 业务异常
     */
    BUSINESS_ERROR("600", "业务异常"),

    /**
     * 数据已存在
     */
    DATA_EXISTS("601", "数据已存在"),

    /**
     * 数据不存在
     */
    DATA_NOT_EXIST("602", "数据不存在"),

    /**
     * 数据校验失败
     */
    DATA_VALIDATE_FAIL("603", "数据校验失败"),

    /**
     * 操作失败
     */
    OPERATION_FAIL("604", "操作失败"),

    /**
     * 参数错误
     */
    PARAM_ERROR("605", "参数错误"),

    /**
     * 状态异常
     */
    STATUS_ERROR("606", "状态异常"),

    /**
     * 权限不足
     */
    PERMISSION_DENIED("607", "权限不足");

    private final String code;

    private final String message;
}
