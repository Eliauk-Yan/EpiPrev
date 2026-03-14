package com.epiprev.common.file.exception;

import com.epiprev.common.base.exception.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @classname FileErrorCode
 * @description 文件存储异常码
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
