package com.epiprev.business.user.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.epiprev.business.user.config.AesEncryptTypeHandler;
import com.epiprev.common.api.user.constant.UserRole;
import com.epiprev.common.api.user.constant.UserState;
import com.epiprev.common.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("users")
public class User extends BaseEntity implements Serializable {

    private String nickName;

    private String password;

    private UserState state;

    private String telephone;

    private String email;

    private String avatar;

    private UserRole role;

    private LocalDateTime lastLoginTime;

    private Boolean certification;

    @TableField(typeHandler = AesEncryptTypeHandler.class)
    private String realName;

    @TableField(typeHandler = AesEncryptTypeHandler.class)
    private String idCardHash;

    public void register(String nickName, String password, String email) {
        this.nickName = nickName;
        this.password = password;
        this.email = email;
        this.role = UserRole.USER;
        this.certification = false;
        init();
    }

    /*
        状态机
     */
    private void init() {
        this.state = UserState.INIT;
    }

    public void auth() {
        this.state = UserState.AUTH;
    }

    public void frozen() {
        this.state = UserState.FROZEN;
    }

}
