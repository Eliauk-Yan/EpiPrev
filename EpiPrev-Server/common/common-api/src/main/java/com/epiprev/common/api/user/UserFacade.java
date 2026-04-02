package com.epiprev.common.api.user;

import com.epiprev.common.api.user.constant.UserState;
import com.epiprev.common.api.user.request.UserLoginRequest;
import com.epiprev.common.api.user.request.UserPageQueryRequest;
import com.epiprev.common.api.user.request.UserRegisterRequest;
import com.epiprev.common.api.user.response.UserResponse;
import com.epiprev.common.api.user.response.data.UserInfo;
import com.epiprev.common.base.exception.BusinessException;
import com.epiprev.common.base.response.PageResult;

/**
 * 用户服务 Dubbo RPC 接口
 */
public interface UserFacade {

    /**
     * 注册用户
     */
    UserResponse<Boolean> register(UserRegisterRequest request) throws BusinessException;

    /**
     * 用户登录
     */
    UserResponse<UserInfo> login(UserLoginRequest request) throws BusinessException;

    /**
     * 分页查询用户
     */
    UserResponse<PageResult<UserInfo>> pageUser(UserPageQueryRequest request) throws BusinessException;

    /**
     * 修改用户状态
     */
    UserResponse<Boolean> updateStatus(Long id, UserState status) throws BusinessException;
}
