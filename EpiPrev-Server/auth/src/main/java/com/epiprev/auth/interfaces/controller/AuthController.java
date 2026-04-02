package com.epiprev.auth.interfaces.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.epiprev.auth.interfaces.dto.LoginDTO;
import com.epiprev.auth.interfaces.dto.RegisterDTO;
import com.epiprev.auth.interfaces.vo.LoginVO;
import com.epiprev.auth.service.AuthService;
import com.epiprev.common.web.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 普通用户登录
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginDTO loginDTO) {
        return Result.success(authService.login(loginDTO));
    }

    /**
     * 管理员登录
     */
    @PostMapping("/login/admin")
    public Result<LoginVO> loginAdmin(@RequestBody LoginDTO loginDTO) {
        return Result.success(authService.loginAdmin(loginDTO));
    }

    @PostMapping("/register")
    public Result<Boolean> register(@RequestBody RegisterDTO registerDTO) {
        authService.register(registerDTO);
        return Result.success(true);
    }

    @PostMapping("/logout")
    public Result<Boolean> logout() {
        StpUtil.logout();
        return Result.success(true);
    }
}
