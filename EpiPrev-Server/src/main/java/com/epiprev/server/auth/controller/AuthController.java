package com.epiprev.server.auth.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.epiprev.server.auth.domain.dto.RegisterDTO;
import com.epiprev.server.auth.service.AuthService;
import com.epiprev.server.common.result.Result;
import com.epiprev.server.auth.domain.dto.LoginDTO;
import com.epiprev.server.auth.domain.vo.LoginVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginDTO loginDTO) {
        return Result.success(authService.login(loginDTO));
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


