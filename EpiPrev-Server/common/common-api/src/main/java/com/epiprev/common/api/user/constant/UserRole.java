package com.epiprev.common.api.user.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {

    USER("USER", "用户"),

    ADMIN("ADMIN", "管理员");

    private final String code;

    private final String description;

}
