package com.epiprev.common.api.user.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户权限
 */
@AllArgsConstructor
@Getter
public enum UserPermission {

    BASIC("BASIC", "基础权限"),

    AUTHENTICATE("AUTHENTICATE", "认证权限"),

    FROZEN("FROZEN", "冻结"),

    NONE("NONE", "无权限");

    private final String code;

    private final String description;

}
