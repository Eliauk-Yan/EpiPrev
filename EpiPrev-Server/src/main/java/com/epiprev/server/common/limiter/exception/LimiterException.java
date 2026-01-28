package com.epiprev.server.common.limiter.exception;

import com.epiprev.server.common.exception.BusinessException;

public class LimiterException extends BusinessException {

    public LimiterException(String message) {
        super(message);
    }
}
