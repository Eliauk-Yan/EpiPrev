package com.epiprev.common.web.config;

import com.epiprev.common.web.handler.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

/**
 * @classname WebConfiguration
 * @description Web配 置类
 * @date 2025/12/01 10:08
 * @created by YanShijie
 */
@AutoConfiguration
@ConditionalOnWebApplication // 仅在web 应用中生效
public class WebConfiguration {

    /**
     * 注册全局异常处理
     */
    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

}
