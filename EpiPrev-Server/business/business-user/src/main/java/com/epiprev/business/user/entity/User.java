package com.epiprev.business.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.epiprev.common.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @classname User
 * @description 用户
 * @date 2025/12/20 19:33
 * @created by ZhangBo
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("users")
public class User extends BaseEntity {

    @TableField("role_id")
    private Long roleId;

    @TableField("username")
    private String username;

    @TableField("phone")
    private String phone;

    @TableField("email")
    private String email;

    @TableField("password")
    private String password;
}
