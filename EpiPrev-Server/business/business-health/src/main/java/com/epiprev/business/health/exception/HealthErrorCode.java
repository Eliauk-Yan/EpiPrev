package com.epiprev.business.health.exception;

import com.epiprev.common.base.exception.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 健康模块错误码
 */
@Getter
@AllArgsConstructor
public enum HealthErrorCode implements ErrorCode {

    TODAY_NOT_RECORD("TODAY_NOT_RECORD", "今日尚未记录健康数据"),
    RECORD_NOT_FOUND("RECORD_NOT_FOUND", "记录不存在");

    private final String code;
    private final String message;
}
