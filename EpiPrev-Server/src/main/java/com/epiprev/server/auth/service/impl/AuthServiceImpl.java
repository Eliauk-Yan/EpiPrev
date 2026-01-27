package com.epiprev.server.auth.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.epiprev.server.auth.domain.dto.LoginDTO;
import com.epiprev.server.auth.domain.dto.RegisterDTO;
import com.epiprev.server.auth.exception.AuthException;
import com.epiprev.server.auth.service.AuthService;
import com.epiprev.server.auth.domain.vo.LoginVO;
import com.epiprev.server.user.domain.entity.SysUser;
import com.epiprev.server.user.convert.SysUserConvert;
import com.epiprev.server.user.service.SysUserService;
import com.epiprev.server.utils.AesUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.epiprev.server.auth.constant.AuthConstant.SESSION_TIMEOUT;
import static com.epiprev.server.auth.exception.AuthErrorCode.*;

/**
 * @classname AuthServiceImpl
 * @description 认证服务实现类
 * @date 2026/01/27 18:20
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SysUserConvert sysUserConvert;

    private final SysUserService sysUserService;

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        // 1. 验证用户信息
        SysUser user = sysUserService
                .getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, loginDTO.getUsername()));
        if (user == null) {
            throw new AuthException(USER_NOT_EXIST);
        }
        // 2. 验证密码
        if (!AesUtil.encrypt(loginDTO.getPassword()).equals(user.getPassword())) {
            throw new AuthException(PASSWORD_ERROR);
        }
        // 3. 用户登录
        boolean rememberMe = Boolean.TRUE.equals(loginDTO.getRememberMe());
        StpUtil.login(user.getId(), new SaLoginParameter().setIsLastingCookie(rememberMe).setTimeout(SESSION_TIMEOUT));
        // 4. 保存用户信息到会话
        StpUtil.getSession().set("userInfo", sysUserConvert.sysUserToUserInfoDTO(user));
        // 5. 组装登录信息
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(StpUtil.getTokenValue());
        // 6. 返回登录信息
        return loginVO;
    }

    @Override
    public void register(RegisterDTO registerDTO) {
        // 1. 验证用户名是否被注册
        SysUser user = sysUserService
                .getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, registerDTO.getUsername()));
        if (user != null) {
            throw new AuthException(USERNAME_DUPLICATED);
        }
        // 2. 创建用户
        SysUser sysUser = new SysUser();
        sysUser.setUsername(registerDTO.getUsername());
        sysUser.setPassword(AesUtil.encrypt(registerDTO.getPassword()));
        sysUser.setEmail(registerDTO.getEmail());
        sysUser.setPhone(registerDTO.getPhone());
        // 3. 保存用户信息
        boolean save = sysUserService.save(sysUser);
        if (!save) {
            throw new AuthException(USER_REGISTER_FAILED);
        }
    }
}
