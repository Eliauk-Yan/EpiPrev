package com.nexo.common.file.domain.exception;

import com.nexo.common.base.exception.BusinessException;
import com.nexo.common.base.exception.code.ErrorCode;

/**
 * @classname FileException
 * @description 文件存储异常
 * @date 2025/12/12 10:50
 * @created by YanShijie
 */
public class FileException extends BusinessException {

    public FileException(ErrorCode errorCode) {
        super(errorCode);
    }
}

