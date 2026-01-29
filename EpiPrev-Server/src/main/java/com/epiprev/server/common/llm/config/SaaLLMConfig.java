package com.epiprev.server.common.llm.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @classname SaaLLMConfig
 * @description SpringAlibaba 配置类
 * @date 2026/01/29 12:23
 */
@Configuration
public class SaaLLMConfig {


    @Value("${spring.ai.dashscope.api-key}")
    private String apiKey;

    @Bean
    public DashScopeApi getDashScopeApi() {
        return DashScopeApi.builder().apiKey(apiKey).build();
    }
}
