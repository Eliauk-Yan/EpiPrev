package com.epiprev.server.common.result.enums;
import com.epiprev.server.common.result.Result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResultCode {

    /**
     * 成功
     */
    SUCCESS("200", "操作成功"),

    /**
     * 失败
     */
    FAIL("500", "操作失败"),

    /**
     * 参数错误
     */
    PARAM_ERROR("400", "参数错误"),

    /**
     * 未授权
     */
    UNAUTHORIZED("401", "未授权"),

    /**
     * 禁止访问
     */
    FORBIDDEN("403", "禁止访问"),

    /**
     * 资源不存在
     */
    NOT_FOUND("404", "资源不存在"),

    /**
     * 请求方法不支持
     */
    METHOD_NOT_ALLOWED("405", "请求方法不支持"),

    /**
     * 请求超时
     */
    REQUEST_TIMEOUT("408", "请求超时"),

    /**
     * 系统内部错误
     */
    INTERNAL_SERVER_ERROR("500", "系统内部错误"),

    /**
     * 服务不可用
     */
    SERVICE_UNAVAILABLE("503", "服务不可用"),

    /**
     * 网关超时
     */
    GATEWAY_TIMEOUT("504", "网关超时"),

    /**
     * 业务异常
     */
    BUSINESS_ERROR("600", "业务异常"),

    /**
     * 数据已存在
     */
    DATA_EXISTS("601", "数据已存在"),

    /**
     * 数据不存在
     */
    DATA_NOT_EXIST("602", "数据不存在"),

    /**
     * 数据校验失败
     */
    DATA_VALIDATE_FAIL("603", "数据校验失败");

    /**
     * 状态码
     */
    private final String code;

    /**
     * 消息
     */
    private final String message;
}

