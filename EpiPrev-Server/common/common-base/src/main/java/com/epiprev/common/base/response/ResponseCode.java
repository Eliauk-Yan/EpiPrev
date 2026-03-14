package com.nexo.common.api.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS("SUCCESS", "成功"),

    FAIL("FAIL", "失败"),

    DUPLICATED("DUPLICATED", "重复"),

    ILLEGAL_ARGUMENT("ILLEGAL_ARGUMENT", "非法参数");

    private final String code;

    private final String message;

}
