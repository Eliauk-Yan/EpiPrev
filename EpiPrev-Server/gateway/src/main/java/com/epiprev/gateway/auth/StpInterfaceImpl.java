package com.nexo.gateway.auth;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.nexo.common.api.user.constant.UserPermission;
import com.nexo.common.api.user.constant.UserRole;
import com.nexo.common.api.user.constant.UserState;
import com.nexo.common.api.user.response.data.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @classname StpInterfaceImpl
 * @description Sa-Token 权限验证接口实现类
 * @date 2025/12/08 20:03
 * @created by YanShijie
 */
@Slf4j
@Component
public class StpInterfaceImpl implements StpInterface {

    /**
     *
     * @param loginId  账号id
     * @param loginType 账号类型
     * @return 权限列表
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 1. 获取当前会话中的用户信息（当前用户）
        UserInfo userInfo = StpUtil.getSessionByLoginId(loginId).getModel("userInfo", UserInfo.class);
        if (userInfo == null) {
            return List.of();
        }
        // 2. 获取当前用户的角色
        UserRole role = userInfo.getRole();
        // 3. 获取当前用户的状态
        UserState state = userInfo.getState();
        // 4. 根据用户状态或角色返回相应的权限
        if (role == UserRole.ADMIN || state == UserState.AUTHENTICATED || state == UserState.ACTIVE) {
            return List.of(UserPermission.BASIC.getCode(), UserPermission.AUTHENTICATE.getCode());
        }
        if (state == UserState.INIT) {
            return List.of(UserPermission.BASIC.getCode());
        }
        if (state == UserState.FROZEN) {
            return List.of(UserPermission.FROZEN.getCode());
        }
        // 5. 默认返回无权限
        return List.of(UserPermission.NONE.getCode());
    }

    /**
     *
     * @param loginId  账号id
     * @param loginType 账号类型
     * @return 角色列表
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        UserInfo userInfo = StpUtil.getSessionByLoginId(loginId).getModel("userInfo", UserInfo.class);
        if (userInfo == null || userInfo.getRole() == null) {
            return List.of();
        }
        return List.of(userInfo.getRole().getCode());
    }
}
