package com.epiprev.common.web.reaponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS(200, "成功"),

    FAIL(500, "失败"),

    NOT_FOUND(404, "未找到"),

    UNAUTHORIZED(401, "未授权"),

    FORBIDDEN(403, "禁止访问"),

    NOT_SUPPORTED(405, "不支持"),

    NOT_ACCEPTABLE(406, "不支持"),

    REQUEST_TIMEOUT(408, "请求超时"),

    CONFLICT(409, "冲突");

    private final int code;

    private final String message;

}
