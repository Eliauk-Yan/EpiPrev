package com.epiprev.server.user.constant;


import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {

    USER("USER", "用户"),

    ADMIN("ADMIN", "管理员");

    @EnumValue
    @JSONField
    private final String code;

    private final String description;

}
