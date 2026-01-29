package com.epiprev.server.user.controller;

import com.epiprev.server.user.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.epiprev.server.common.result.Result;
import com.epiprev.server.user.domain.dto.UserInfoDTO;
import com.epiprev.server.user.domain.dto.UserUpdateDTO;

/**
 * @classname UserController
 * @description 用户模块控制器
 * @date 2026/01/27 18:39
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class SysUserController {

    private final SysUserService sysUserService;

    @GetMapping("/info")
    public Result<UserInfoDTO> getUserInfo() {
        return Result.success(sysUserService.getCurrentUserInfo());
    }

    @PutMapping("/info")
    public Result<Void> updateUserInfo(@RequestBody UserUpdateDTO dto) {
        sysUserService.updateCurrentUserInfo(dto);
        return Result.success();
    }

}
