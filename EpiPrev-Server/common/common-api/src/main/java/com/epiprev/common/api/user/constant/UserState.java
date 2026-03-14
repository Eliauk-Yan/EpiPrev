package com.epiprev.server.user.constant;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态
 */
@Getter
@AllArgsConstructor
public enum UserState {

    INIT("INIT", "初始化"),

    AUTHENTICATED("AUTHENTICATED", "已实名"),

    ACTIVE("ACTIVE", "已激活"),

    FROZEN("FROZEN", "已冻结");

    @EnumValue
    @JSONField
    private final String code;

    private final String message;
}
