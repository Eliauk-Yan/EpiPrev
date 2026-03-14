package com.epiprev.business.user.exception;

import com.epiprev.common.base.exception.BusinessException;
import com.epiprev.common.base.exception.code.ErrorCode;

public class UserException extends BusinessException {

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
