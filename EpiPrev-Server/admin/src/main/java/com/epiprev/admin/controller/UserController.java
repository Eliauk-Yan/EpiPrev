package com.epiprev.admin.controller;

import com.epiprev.admin.controller.params.UserUpdateStateParams;
import com.epiprev.admin.service.UserService;
import com.epiprev.common.api.user.response.data.UserInfo;
import com.epiprev.common.web.result.MultiResult;
import com.epiprev.common.web.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/list")
    public MultiResult<UserInfo> list(@RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String state) {
        return userService.pageUser(page, size, keyword, role, state);
    }

    @PutMapping("/status")
    public Result<Boolean> updateStatus(@RequestBody UserUpdateStateParams dto) {
        return userService.updateStatus(dto.getId(), dto.getStatus());
    }
}
