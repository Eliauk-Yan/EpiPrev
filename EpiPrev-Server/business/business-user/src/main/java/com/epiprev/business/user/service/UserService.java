package com.epiprev.business.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.epiprev.business.user.domain.entity.User;
import com.epiprev.common.api.user.constant.UserState;

public interface UserService extends IService<User> {

    /**
     * 根据ID查询用户
     */
    User getUserById(Long userId);

    /**
     * 更新用户信息
     */
    Boolean updateUserById(User user);

    /**
     * 更新用户状态
     */
    Boolean updateState(Long id, UserState userState);
}
