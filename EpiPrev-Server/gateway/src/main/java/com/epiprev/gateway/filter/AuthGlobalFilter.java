package com.epiprev.gateway.filter;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.reactor.context.SaReactorSyncHolder;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.epiprev.common.web.constant.ResultCode;
import com.epiprev.common.web.vo.Result;
import com.epiprev.gateway.config.GatewayAuthConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @classname AuthGlobalFilter
 * @description 网关统一鉴权过滤器
 * @date 2025/12/20
 * @created by Auto
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class AuthGlobalFilter {

    private final GatewayAuthConfig gatewayAuthConfig;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 注册 Sa-Token 全局过滤器
     */
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 拦截所有路由
                .addInclude("/**")
                // 排除白名单路径
                .addExclude("/actuator/**", "/favicon.ico")
                // 在鉴权之前执行：从请求头中获取token
                .setBeforeAuth(obj -> {
                    ServerWebExchange exchange = SaReactorSyncHolder.getContext();
                    // 从 Authorization 请求头获取 token
                    String token = exchange.getRequest().getHeaders().getFirst("Authorization");
                    if (token != null && !token.isEmpty()) {
                        // 如果token有Bearer前缀，去掉前缀
                        if (token.startsWith("Bearer ")) {
                            token = token.substring(7);
                        }
                        // 设置token到上下文，供后续验证使用
                        StpUtil.setTokenValue(token);
                    }
                })
                // 鉴权方法：每次访问进入
                .setAuth(obj -> {
                    // 获取当前请求路径
                    String path = SaRouter.getMatchPath();
                    
                    // 检查是否在白名单中
                    if (gatewayAuthConfig.isWhiteList(path)) {
                        log.debug("路径 {} 在白名单中，跳过鉴权", path);
                        return;
                    }

                    // 验证token（token已在setBeforeAuth中设置）
                    try {
                        // 检查是否登录（Sa-Token会自动验证token）
                        StpUtil.checkLogin();
                        log.debug("token验证成功，路径: {}, 用户ID: {}", path, StpUtil.getLoginId());
                    } catch (NotLoginException e) {
                        log.warn("token验证失败，路径: {}, 原因: {}", path, e.getMessage());
                        throw e;
                    } catch (Exception e) {
                        log.error("token验证异常，路径: {}", path, e);
                        throw new NotLoginException("token验证失败: " + e.getMessage());
                    }
                })
                // 异常处理
                .setError(e -> {
                    ServerWebExchange exchange = SaReactorSyncHolder.getContext();
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

                    Result<?> result;
                    if (e instanceof NotLoginException) {
                        result = Result.error(ResultCode.UNAUTHORIZED, "未登录或token已过期，请先登录");
                    } else {
                        result = Result.error(ResultCode.UNAUTHORIZED, "鉴权失败: " + e.getMessage());
                    }

                    try {
                        String json = objectMapper.writeValueAsString(result);
                        DataBuffer buffer = response.bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
                        return response.writeWith(Mono.just(buffer));
                    } catch (JsonProcessingException ex) {
                        log.error("序列化响应结果失败", ex);
                        return response.setComplete();
                    }
                });
    }

}

