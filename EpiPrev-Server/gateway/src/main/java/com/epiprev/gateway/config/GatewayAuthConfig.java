package com.epiprev.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @classname GatewayAuthConfig
 * @description 网关鉴权配置
 * @date 2025/12/20
 * @created by Auto
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "gateway.auth")
public class GatewayAuthConfig {

    /**
     * 白名单路径（不需要鉴权的路径）
     */
    private List<String> whiteList = new ArrayList<>();

    /**
     * 是否启用鉴权
     */
    private Boolean enabled = true;

    /**
     * Token在请求头中的名称
     */
    private String tokenName = "Authorization";

    /**
     * Token前缀（如：Bearer）
     */
    private String tokenPrefix = "";

    /**
     * 默认白名单路径
     */
    public GatewayAuthConfig() {
        whiteList.add("/auth/login");
        whiteList.add("/auth/register");
        whiteList.add("/actuator/**");
    }

    /**
     * 判断路径是否在白名单中
     */
    public boolean isWhiteList(String path) {
        if (!enabled) {
            return true;
        }
        return whiteList.stream().anyMatch(pattern -> {
            // 简单的路径匹配，支持通配符
            if (pattern.endsWith("/**")) {
                String prefix = pattern.substring(0, pattern.length() - 3);
                return path.startsWith(prefix);
            }
            return path.equals(pattern) || path.startsWith(pattern + "/");
        });
    }
}

