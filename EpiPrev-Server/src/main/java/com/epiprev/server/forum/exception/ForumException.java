package com.epiprev.server.forum.exception;

import com.epiprev.server.common.exception.BusinessException;
import com.epiprev.server.common.exception.code.ErrorCode;

/**
 * @classname ForumException
 * @description 论坛模块异常
 * @date 2026/01/28 16:04
 */
public class ForumException extends BusinessException {
    public ForumException(ErrorCode errorCode) {
        super(errorCode);
    }
}
