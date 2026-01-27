package com.epiprev.server.user.exception;

import com.epiprev.server.common.exception.BusinessException;
import com.epiprev.server.common.exception.code.ErrorCode;

/**
 * @classname UserException
 * @description 用户模块异常类
 * @date 2026/01/27 18:52
 */
public class UserException extends BusinessException {
    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
