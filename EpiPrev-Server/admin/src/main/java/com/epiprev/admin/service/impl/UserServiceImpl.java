package com.epiprev.admin.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.epiprev.admin.service.UserService;
import com.epiprev.common.api.user.UserFacade;
import com.epiprev.common.api.user.constant.UserState;
import com.epiprev.common.api.user.request.UserPageQueryRequest;
import com.epiprev.common.api.user.response.UserResponse;
import com.epiprev.common.api.user.response.data.UserInfo;
import com.epiprev.common.base.response.PageResult;
import com.epiprev.common.web.result.MultiResult;
import com.epiprev.common.web.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 用户管理服务实现类
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @DubboReference(version = "1.0.0")
    private UserFacade userFacade;

    @Override
    public MultiResult<UserInfo> pageUser(Integer page, Integer size, String keyword, String role, String state) {
        log.info("Admin分页查询用户: page={}, size={}, keyword={}, role={}, state={}", page, size, keyword, role, state);
        UserPageQueryRequest request = new UserPageQueryRequest();
        request.setPage(page);
        request.setSize(size);
        request.setKeyword(keyword);
        request.setRole(role);
        request.setState(state);

        UserResponse<PageResult<UserInfo>> response = userFacade.pageUser(request);
        if (response.getSuccess() && response.getData() != null) {
            PageResult<UserInfo> pageResult = response.getData();
            return MultiResult.multiSuccess(pageResult.getList(), pageResult.getTotal(), page, size);
        }
        return MultiResult.multiSuccess(Collections.emptyList(), 0, page, size);
    }


    @Override
    public Result<Boolean> updateStatus(Long id, String status) {
        UserState userState = UserState.valueOf(status);
        UserResponse<Boolean> response = userFacade.updateStatus(id, userState);
        if (response.getSuccess()) {
            refreshUserSession(id, userState);
            return Result.success(response.getData());
        }
        return Result.error(response.getCode(), response.getMessage());
    }

    /**
     * 刷新用户 Sa-Token 会话中的状态信息，使权限变更立即生效
     */
    private void refreshUserSession(Long userId, UserState newState) {
        try {
            SaSession session = StpUtil.getSessionByLoginId(userId, false);
            if (session != null) {
                UserInfo userInfo = session.getModel("userInfo", UserInfo.class);
                if (userInfo != null) {
                    userInfo.setState(newState);
                    session.set("userInfo", userInfo);
                    log.info("已刷新用户会话状态: userId={}, newState={}", userId, newState);
                }
            }
        } catch (Exception e) {
            log.warn("刷新用户会话信息失败: userId={}", userId, e);
        }
    }
}
