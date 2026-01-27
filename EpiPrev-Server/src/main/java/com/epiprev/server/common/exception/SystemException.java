package com.epiprev.server.common.exception;

import com.epiprev.server.common.exception.code.ErrorCode;
import lombok.Getter;

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
