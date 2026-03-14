package com.epiprev.common.base.exception.code;

/**
 * @classname ErrorCode
 * @description 错误码
 */
public interface ErrorCode {

    /**
     * 获取错误码
     * @return 错误码
     */
    String getCode();

    /**
     * 获取错误信息
     * @return 错误信息
     */
    String getMessage();
}
