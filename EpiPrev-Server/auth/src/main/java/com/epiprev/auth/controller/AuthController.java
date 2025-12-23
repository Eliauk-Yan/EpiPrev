package com.epiprev.auth.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.epiprev.auth.service.AuthService;
import com.epiprev.common.web.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @classname AuthController
 * @description 认证
 * @date 2025/12/20 20:19
 */
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Result<?> login() {
        return Result.success();
    }

    @PostMapping("/register")
    public Result<?> register() {
        return Result.success();
    }

    @PostMapping("/logout")
    public Result<?> logout() {
        StpUtil.logout();
        return Result.success();
    }

}
