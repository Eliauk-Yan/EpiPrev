package com.epiprev.server.user.controller;

import com.epiprev.server.user.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
