package com.epiprev.business.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.epiprev.common.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @classname Role
 * @description 角色
 * @date 2025/12/20 19:33
 * @created by ZhangBo
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("roles")
public class Role extends BaseEntity {

    @TableField("name")
    private String name;

    @TableField("description")
    private String description;

}
