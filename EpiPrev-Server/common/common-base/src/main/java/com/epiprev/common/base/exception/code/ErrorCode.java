package com.nexo.common.base.exception.code;

/**
 * @classname ErrorCode
 * @description 错误码
 * @date 2025/12/01 09:41
 * @created by YanShijie
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
