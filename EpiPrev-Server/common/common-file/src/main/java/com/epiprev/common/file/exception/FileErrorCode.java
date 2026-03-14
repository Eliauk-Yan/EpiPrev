package com.nexo.common.file.domain.exception;

import com.nexo.common.base.exception.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @classname FileErrorCode
 * @description 文件存储异常码
 * @date 2025/12/12 11:21
 * @created by YanShijie
 */
@AllArgsConstructor
@Getter
public enum FileErrorCode implements ErrorCode {

    FILE_IO_EXCEPTION("FILE_IO_EXCEPTION", "文件IO异常"),

    FILE_UPLOAD_FAILED("FILE_UPLOAD_FAILED", "文件上传失败"),

    FILE_DELETE_FAILED("FILE_DELETE_FAILED", "文件删除失败");

    private final String code;

    private final String message;
}
