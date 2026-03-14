package com.epiprev.auth.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.epiprev.auth.exception.AuthException;
import com.epiprev.auth.interfaces.dto.LoginDTO;
import com.epiprev.auth.interfaces.dto.RegisterDTO;
import com.epiprev.auth.interfaces.vo.LoginVO;
import com.epiprev.auth.service.AuthService;
import com.epiprev.common.api.user.UserFacade;
import com.epiprev.common.api.user.constant.UserRole;
import com.epiprev.common.api.user.request.UserLoginRequest;
import com.epiprev.common.api.user.request.UserRegisterRequest;
import com.epiprev.common.api.user.response.UserResponse;
import com.epiprev.common.api.user.response.data.UserInfo;
import com.epiprev.common.base.exception.BusinessException;
import com.epiprev.common.web.utils.AesUtil;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import static com.epiprev.auth.exception.AuthErrorCode.ILLEGAL_LOGIN;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @DubboReference(version = "1.0.0")
    private UserFacade userFacade;

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        // 1. 远程调用用户服务登录并获取用户信息
        UserLoginRequest request = new UserLoginRequest();
        request.setUsername(loginDTO.getUsername());
        request.setPassword(loginDTO.getPassword());
        UserResponse<UserInfo> response = userFacade.login(request);
        UserInfo userInfo = response.getData();
        // 3. 登录
        boolean rememberMe = Boolean.TRUE.equals(loginDTO.getRememberMe());
        StpUtil.login(userInfo.getId(), rememberMe);
        // 4. 保存用户信息到 Session
        StpUtil.getSession().set("userInfo", userInfo);
        return new LoginVO(StpUtil.getTokenValue(), userInfo);
    }

    @Override
    public LoginVO loginAdmin(LoginDTO loginDTO) {
        // 1. 远程调用用户服务登录并获取用户信息
        UserLoginRequest request = new UserLoginRequest();
        request.setUsername(loginDTO.getUsername());
        request.setPassword(loginDTO.getPassword());
        UserResponse<UserInfo> response = userFacade.login(request);
        UserInfo userInfo = response.getData();
        // 2. 验证用户身份
        if (userInfo.getRole() != UserRole.ADMIN) {
            throw new AuthException(ILLEGAL_LOGIN);
        }
        // 3. 登录
        boolean rememberMe = Boolean.TRUE.equals(loginDTO.getRememberMe());
        StpUtil.login(userInfo.getId(), rememberMe);
        // 4. 保存用户信息到 Session
        StpUtil.getSession().set("userInfo", userInfo);
        return new LoginVO(StpUtil.getTokenValue(), userInfo);
    }

    @Override
    public void register(RegisterDTO registerDTO) {
        // 1. 构造用户注册请求
        UserRegisterRequest registerRequest = new UserRegisterRequest();
        // 2. 填充数据
        registerRequest.setNickName(registerDTO.getNickName());
        registerRequest.setEmail(registerDTO.getEmail());
        registerRequest.setPassword(AesUtil.encrypt(registerDTO.getPassword()));
        // 3. 远程调用用户服务进行注册
        UserResponse<Boolean> response = userFacade.register(registerRequest);
        if (!response.getSuccess()) {
            throw new BusinessException(response.getMessage());
        }
    }
}
