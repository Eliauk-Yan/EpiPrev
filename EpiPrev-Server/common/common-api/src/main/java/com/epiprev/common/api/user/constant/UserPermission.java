package com.epiprev.server.user.constant;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.EnumValue;
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

    @EnumValue
    @JSONField
    private final String code;

    private final String description;

}
