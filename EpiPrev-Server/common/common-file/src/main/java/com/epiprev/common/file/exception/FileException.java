package com.epiprev.common.file.exception;

import com.epiprev.common.base.exception.BusinessException;
import com.epiprev.common.base.exception.code.ErrorCode;

/**
 * @classname FileException
 * @description 文件存储异常
 */
public class FileException extends BusinessException {

    public FileException(ErrorCode errorCode) {
        super(errorCode);
    }
}

