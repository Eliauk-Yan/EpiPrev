package com.epiprev.server.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.epiprev.server.user.domain.dto.UserInfoDTO;
import com.epiprev.server.user.domain.dto.UserUpdateDTO;
import com.epiprev.server.user.domain.entity.SysUser;

public interface SysUserService extends IService<SysUser> {

    /**
     * 获取当前用户信息
     * 
     * @return UserInfoDTO
     */
    UserInfoDTO getCurrentUserInfo();

    /**
     * 更新当前用户信息
     * 
     * @param dto UserUpdateDTO
     */
    void updateCurrentUserInfo(UserUpdateDTO dto);

}
