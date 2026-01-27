package com.epiprev.server.auth.service;

import com.epiprev.server.auth.domain.dto.LoginDTO;
import com.epiprev.server.auth.domain.dto.RegisterDTO;
import com.epiprev.server.auth.domain.vo.LoginVO;

/**
 * @classname AuthService
 * @description 认证服务接口
 * @date 2026/01/27 18:19
 */
public interface AuthService {

    /**
     * 用户登录
     * @param loginDTO 登录参数
     * @return 登录结果
     */
    LoginVO login(LoginDTO loginDTO);

    /**
     * 用户注册接口
     * @param registerDTO 注册参数
     */
    void register(RegisterDTO registerDTO);
}
