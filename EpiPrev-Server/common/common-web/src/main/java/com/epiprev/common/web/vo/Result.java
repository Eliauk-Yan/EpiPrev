package com.epiprev.common.web.vo;


import com.epiprev.common.web.constant.ResultCode;
import lombok.Data;

/**
 * @classname Result
 * @description 统一返回对象
 * @date 2025/11/27 21:01
 * @created by YanShijie
 */
@Data
public class Result<T> {

    private String code;

    private Boolean success;

    private String message;

    private T data;

    public Result() {
    }

    public Result(Boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public Result(Boolean success, String code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功返回
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 成功返回，带数据
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(true, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回，自定义消息
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(true, ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败返回
     */
    public static <T> Result<T> error(String code, String message) {
        return new Result<>(false, code, message, null);
    }

    /**
     * 失败返回，使用ResultCode
     */
    public static <T> Result<T> error(ResultCode resultCode) {
        return new Result<>(false, resultCode.getCode(), resultCode.getMessage(), null);
    }

    /**
     * 失败返回，使用ResultCode，自定义消息
     */
    public static <T> Result<T> error(ResultCode resultCode, String message) {
        return new Result<>(false, resultCode.getCode(), message, null);
    }
}
