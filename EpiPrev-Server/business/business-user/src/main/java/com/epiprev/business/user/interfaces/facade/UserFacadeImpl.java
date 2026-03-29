package com.epiprev.business.user.interfaces.facade;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.epiprev.business.user.domain.entity.User;
import com.epiprev.business.user.domain.entity.UserOperateStream;
import com.epiprev.business.user.exception.UserException;
import com.epiprev.business.user.mapper.UserMapper;
import com.epiprev.business.user.mapper.UserOperateStreamMapper;
import com.epiprev.business.user.service.UserService;
import com.epiprev.business.user.service.impl.UserCacheService;
import com.epiprev.common.api.user.UserFacade;
import com.epiprev.common.api.user.constant.UserState;
import com.epiprev.common.api.user.request.UserLoginRequest;
import com.epiprev.common.api.user.request.UserPageQueryRequest;
import com.epiprev.common.api.user.request.UserRegisterRequest;
import com.epiprev.common.api.user.response.UserResponse;
import com.epiprev.common.api.user.response.data.UserInfo;
import com.epiprev.common.base.exception.BusinessException;
import com.epiprev.common.base.response.PageResult;
import com.epiprev.common.web.utils.AesUtil;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.epiprev.business.user.exception.UserErrorCode.*;

/**
 * 用户服务 Dubbo RPC 实现
 */
@DubboService(version = "1.0.0")
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade {

    private final UserMapper userMapper;

    private final UserOperateStreamMapper operateStreamMapper;

    private final UserService userService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserResponse<Boolean> register(UserRegisterRequest request) {
        // 1. 构造用户
        User user = new User();
        user.register(request.getNickName(), request.getPassword(), request.getEmail());
        int insertRow = userMapper.insert(user);
        // 2. 注册验证
        if (insertRow != 1) {
            throw new BusinessException(USER_REGISTER_FAILED);
        }
        // 3. 更新操作流水
        UserOperateStream stream = new UserOperateStream(user.getId(), "用户注册", LocalDateTime.now(),
                JSON.toJSONString(request), "");
        int operateInsert = operateStreamMapper.insert(stream);
        if (operateInsert != 1) {
            throw new UserException(USER_OPERATE_STREAM_INSERT_FAILED);
        }
        // 4. 返回响应
        UserResponse<Boolean> response = new UserResponse<>();
        response.setSuccess(true);
        response.setData(true);
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserResponse<UserInfo> login(UserLoginRequest request) {
        // 1. 查找用户
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getNickName, request.getUsername())
                        .or().eq(User::getEmail, request.getUsername()));
        // 2. 用户校验
        if (user == null) {
            throw new UserException(USERNAME_OR_PASSWORD_ERROR);
        }
        // 3. 密码校验
        if (!request.getPassword().equals(AesUtil.decrypt(user.getPassword()))) {
            throw new UserException(USERNAME_OR_PASSWORD_ERROR);
        }
        // 4. 记录用户最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        int updateRow = userMapper.updateById(user);
        if (updateRow != 1) {
            throw new UserException(USER_UPDATE_FAILED);
        }
        // 4. 构造响应
        UserResponse<UserInfo> response = new UserResponse<>();
        response.setSuccess(true);
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(user, userInfo);
        response.setData(userInfo);
        return response;
    }

    @Override
    public UserResponse<PageResult<UserInfo>> pageUser(UserPageQueryRequest request) {
        Page<User> page = new Page<>(request.getPage(), request.getSize());
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(request.getKeyword())) {
            queryWrapper.like(User::getNickName, request.getKeyword())
                    .or().like(User::getTelephone, request.getKeyword())
                    .or().like(User::getEmail, request.getKeyword());
        }
        if (StringUtils.hasText(request.getRole())) {
            queryWrapper.eq(User::getRole, request.getRole());
        }
        if (StringUtils.hasText(request.getState())) {
            try {
                // 尝试将state转换为UserState枚举
                String stateStr = request.getState();
                // 处理FREEZE状态，映射到FROZEN
                if ("FREEZE".equals(stateStr)) {
                    queryWrapper.eq(User::getState, UserState.FROZEN);
                } else {
                    UserState userState = UserState.valueOf(stateStr);
                    queryWrapper.eq(User::getState, userState);
                }
            } catch (Exception e) {
                // 如果转换失败，尝试作为数字处理
                try {
                    int stateInt = Integer.parseInt(request.getState());
                    UserState userState;
                    switch (stateInt) {
                        case 0 -> userState = UserState.INIT;
                        case 1 -> userState = UserState.AUTH;
                        case 2 -> userState = UserState.FROZEN;
                        default -> userState = null;
                    }
                    if (userState != null) {
                        queryWrapper.eq(User::getState, userState);
                    }
                } catch (Exception ex) {
                    // 忽略无效的state值
                }
            }
        }
        queryWrapper.orderByDesc(User::getCreateTime);

        Page<User> userPage = userMapper.selectPage(page, queryWrapper);

        List<UserInfo> userInfoList = userPage.getRecords().stream().map(user -> {
            UserInfo userInfo = new UserInfo();
            BeanUtils.copyProperties(user, userInfo);
            return userInfo;
        }).collect(Collectors.toList());

        UserResponse<PageResult<UserInfo>> response = new UserResponse<>();
        response.setSuccess(true);
        response.setData(new PageResult<>(userInfoList, userPage.getTotal()));
        return response;
    }

    @Transactional
    @Override
    public UserResponse<Boolean> updateStatus(Long id, UserState status) {
        // 1. 更新用户
        Boolean result = userService.updateState(id, status);
        // 2. 返回响应
        UserResponse<Boolean> response = new UserResponse<>();
        response.setSuccess(result);
        response.setData(result);
        return response;
    }
}
