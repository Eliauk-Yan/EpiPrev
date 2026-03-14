package com.epiprev.common.api.user.response.data;

import com.epiprev.common.api.user.constant.UserRole;
import com.epiprev.common.api.user.constant.UserState;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户Id
     */
    private Long id;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户状态
     */
    private UserState state;

    /**
     * 实名认证
     */
    private Boolean certification;

    /**
     * 用户角色
     */
    private UserRole role;

    /**
     * 注册时间
     */
    private LocalDateTime createTime;
}
