package com.epiprev.server.config;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Sa-Token 配置
 */
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 路由拦截器，校验是否登录
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                // 1. 拦截全部地址
                .addPathPatterns("/**")
                // 2. 排除地址
                .excludePathPatterns("/auth/**", "/error", "/favicon.ico");

    }


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
                    SaRouter.match("/**", _ -> StpUtil.checkLogin());
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
