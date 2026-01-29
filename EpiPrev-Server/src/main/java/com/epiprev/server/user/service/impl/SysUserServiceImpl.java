package com.epiprev.server.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.epiprev.server.user.domain.entity.SysUser;
import com.epiprev.server.user.mapper.mybatis.SysUserMapper;
import com.epiprev.server.user.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.epiprev.server.user.convert.SysUserConvert;
import com.epiprev.server.user.domain.dto.UserInfoDTO;
import com.epiprev.server.user.domain.dto.UserUpdateDTO;
import cn.dev33.satoken.stp.StpUtil;
import org.apache.commons.lang3.StringUtils;

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysUserConvert sysUserConvert;

    @Override
    public UserInfoDTO getCurrentUserInfo() {
        Long userId = StpUtil.getLoginIdAsLong();
        SysUser user = getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return sysUserConvert.sysUserToUserInfoDTO(user);
    }

    @Override
    public void updateCurrentUserInfo(UserUpdateDTO dto) {
        Long userId = StpUtil.getLoginIdAsLong();
        SysUser user = getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (StringUtils.isNotBlank(dto.getUsername())) {
            user.setUsername(dto.getUsername());
        }
        if (StringUtils.isNotBlank(dto.getPhone())) {
            user.setPhone(dto.getPhone());
        }
        if (StringUtils.isNotBlank(dto.getEmail())) {
            user.setEmail(dto.getEmail());
        }
        if (StringUtils.isNotBlank(dto.getAvatar())) {
            user.setAvatar(dto.getAvatar());
        }

        updateById(user);
    }
}
