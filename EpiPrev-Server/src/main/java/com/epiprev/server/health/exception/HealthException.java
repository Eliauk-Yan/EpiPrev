package com.epiprev.server.health.exception;

import com.epiprev.server.common.exception.BusinessException;
import com.epiprev.server.common.exception.code.ErrorCode;

/**
 * @classname HealthException
 * @description 健康模块异常
 * @date 2026/01/29 12:40
 */
public class HealthException extends BusinessException {

    public HealthException(ErrorCode errorCode) {
        super(errorCode);
    }

}
