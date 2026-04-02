package com.epiprev.business.user.interfaces.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.epiprev.business.user.domain.entity.User;
import com.epiprev.business.user.exception.UserException;
import com.epiprev.business.user.interfaces.controller.params.RealNameAuthParam;
import com.epiprev.business.user.service.UserAuthService;
import com.epiprev.business.user.service.UserService;
import com.epiprev.business.user.service.impl.UserCacheService;
import com.epiprev.common.api.user.response.data.UserInfo;
import com.epiprev.common.file.service.FileService;
import com.epiprev.common.web.result.Result;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.epiprev.business.user.exception.UserErrorCode.USER_NOT_FOUND;
import static com.epiprev.common.api.user.constant.UserConstant.AVATAR_FILE_PREFIX;

/**
 * 用户控制器
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    /**
     * 用户服务
     */
    private final UserService userService;

    /**
     * 用户缓存服务
     */
    private final UserCacheService userCacheService;

    /**
     * 用户认证服务
     */
    private final UserAuthService userAuthService;

    /**
     * 文件服务
     */
    private final FileService fileService;

    /**
     * 获取当前用户信息
     */
    @GetMapping("/getUserInfo")
    public Result<UserInfo> getUserInfo() {
        Long userId = StpUtil.getLoginIdAsLong();
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new UserException(USER_NOT_FOUND);
        }
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(user, userInfo);
        return Result.success(userInfo);
    }

    /**
     * 修改头像
     */
    @PutMapping("/avatar")
    public Result<Boolean> updateAvatar(@RequestPart("avatar") MultipartFile avatar) {
        // 1. 获取当前登录用户ID
        long userId = StpUtil.getLoginIdAsLong();
        // 2. 根据用户ID查询用户信息
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new UserException(USER_NOT_FOUND);
        }
        // 3. 删除旧头像
        if (StringUtils.isNotBlank(user.getAvatar())) {
            fileService.deleteFile(user.getAvatar());
        }
        // 4. 设置文件路径（模块 + 唯一标识 + 功能 + 时间）
        String filePath = AVATAR_FILE_PREFIX + userId;
        // 5. 上传文件
        String avatarUrl = fileService.uploadFile(avatar, filePath);
        // 6. 更新用户信息
        user.setAvatar(avatarUrl);
        return Result.success(userService.updateUserById(user));
    }

    /**
     * 修改用户昵称
     */
    @PostMapping("/modifyNickName")
    public Result<Boolean> modifyNickName(@RequestParam String nickName) {
        Long userId = StpUtil.getLoginIdAsLong();
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new UserException(USER_NOT_FOUND);
        }
        user.setNickName(nickName);
        return Result.success(userService.updateUserById(user));
    }

    /**
     * 实名认证
     */
    @PostMapping("/realNameAuth")
    public Result<Boolean> realNameAuth(@RequestBody RealNameAuthParam param) {
        Boolean result = userAuthService.realNameAuth(param);
        if (result) {
            User user = userService.getUserById(StpUtil.getLoginIdAsLong());
            user.setRealName(param.getRealName());
            user.setIdCardHash(param.getIdCard());
            user.setCertification(true);
            user.auth();
            Boolean updateResult = userService.updateUserById(user);
            return Result.success(updateResult);
        }
        return Result.success(false);
    }
}
