package com.epiprev.server.auth.exception;

import com.epiprev.server.common.exception.BusinessException;
import com.epiprev.server.common.exception.code.ErrorCode;

/**
 * @classname AuthException
 * @description 认证异常
 * @date 2026/01/27 18:26
 */
public class AuthException extends BusinessException {

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
