package com.nexo.gateway.auth;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.nexo.common.api.user.constant.UserPermission;
import com.nexo.common.api.user.constant.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @classname SaTokenConfigure
 * @description SaToken配置类
 * @date 2025/12/08 19:55
 * @created by YanShijie
 */
@Configuration
@Slf4j
public class SaTokenConfigure {

    // 注册 Sa-Token全局过滤器
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 拦截地址
                .addInclude("/**")
                // 开放地址
                .addExclude("/favicon.ico", "/admin/auth/**", "/auth/**")
                // 鉴权方法：每次访问进入
                .setAuth(obj -> {
                    // 登录校验
                    SaRouter.match("/**",  _ -> StpUtil.checkLogin());
                    // 管理端模块 -> 用户角色校验
                    SaRouter.match("/admin/**", r -> StpUtil.checkRoleOr(UserRole.ADMIN.getCode(), UserRole.ROOT.getCode(), UserRole.GOD.getCode()));
                    // 交易模块 -> 认证权限校验（未认证的用户无法下单）
                    SaRouter.match("/trade/**", r -> StpUtil.checkPermission(UserPermission.AUTHENTICATE.getCode()));
                })
                // 异常处理方法：每次setAuth函数出现异常时进入
                .setError(this::getSaResult);
    }

    private SaResult getSaResult(Throwable throwable) {
        return switch (throwable) {
            case NotLoginException _ -> {
                log.error("未登录");
                yield SaResult.error("未登录");
            }
            case NotRoleException e -> {
                if (!UserRole.ADMIN.getCode().equals(e.getRole()) && !UserRole.ROOT.getCode().equals(e.getRole()) && !UserRole.GOD.getCode().equals(e.getRole())) {
                    log.error("请勿越权使用");
                    yield SaResult.error("请勿越权使用");
                }
                log.error("您无权限进行此操作");
                yield SaResult.error("您无权限进行此操作");
            }
            case NotPermissionException e -> {
                if (UserPermission.AUTHENTICATE.getCode().equals(e.getPermission())) {
                    log.error("请先进行实名认证");
                    yield SaResult.error("请先进行实名认证");
                }
                log.error("无权限");
                yield SaResult.error("无权限");
            }
            default -> SaResult.error(throwable.getMessage());
        };
    }
}