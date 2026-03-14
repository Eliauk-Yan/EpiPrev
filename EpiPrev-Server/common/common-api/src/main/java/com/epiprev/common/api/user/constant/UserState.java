package com.epiprev.common.api.user.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态
 */
@Getter
@AllArgsConstructor
public enum UserState {

    INIT("INIT", "初始化"),

    AUTH("AUTH", "已实名"),

    FROZEN("FROZEN", "已冻结");

    private final String code;

    private final String message;
}
