package com.epiprev.common.base.exception;

import com.epiprev.common.base.exception.code.ErrorCode;
import lombok.Getter;

/**
 * @classname SystemException
 * @description 系统异常
 */
@Getter
public class SystemException extends RuntimeException {

    private final ErrorCode errorCode;

    public SystemException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public SystemException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public SystemException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public SystemException(String message) {
        super(message);
        this.errorCode = null;
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = null;
    }
}
