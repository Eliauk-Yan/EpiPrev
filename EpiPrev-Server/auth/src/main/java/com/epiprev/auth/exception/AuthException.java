package com.epiprev.auth.exception;

import com.epiprev.common.base.exception.BusinessException;
import com.epiprev.common.base.exception.code.ErrorCode;

public class AuthException extends BusinessException {

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }

}
