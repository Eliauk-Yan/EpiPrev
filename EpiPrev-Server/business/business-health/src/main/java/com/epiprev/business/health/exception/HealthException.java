package com.epiprev.business.health.exception;

import com.epiprev.common.base.exception.BusinessException;
import com.epiprev.common.base.exception.code.ErrorCode;

/**
 * 健康模块异常
 */
public class HealthException extends BusinessException {

    public HealthException(ErrorCode errorCode) {
        super(errorCode);
    }

    public HealthException(String message) {
        super(message);
    }
}
