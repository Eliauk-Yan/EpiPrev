package com.epiprev.admin.service;

import com.epiprev.common.api.user.response.data.UserInfo;
import com.epiprev.common.web.result.MultiResult;
import com.epiprev.common.web.result.Result;

/**
 * 用户管理服务接口
 */
public interface UserService {

    /**
     * 分页查询用户
     */
    MultiResult<UserInfo> pageUser(Integer page, Integer size, String keyword, String role, String state);



    /**
     * 禁用/启用用户
     */
    Result<Boolean> updateStatus(Long id, String status);
}
