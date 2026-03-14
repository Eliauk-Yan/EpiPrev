package com.nexo.common.web.config;

import com.nexo.common.web.filter.TokenFilter;
import com.nexo.common.web.handler.GlobalExceptionHandler;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @classname WebConfiguration
 * @description Web配 置类
 * @date 2025/12/01 10:08
 * @created by YanShinji
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

    /**
     * 注册自定义 Token 过滤器
     */
    @Bean
    public FilterRegistrationBean<TokenFilter> tokenFilter(StringRedisTemplate redisTemplate) {
        // 1. 创建过滤器注册对象
        FilterRegistrationBean<TokenFilter> registrationBean = new FilterRegistrationBean<>();
        // 2. 设置过滤器
        registrationBean.setFilter(new TokenFilter(redisTemplate));
        // 3. 设置过滤路径
        registrationBean.addUrlPatterns("/trade/buy");
        // 4. 设置过滤器优先级
        registrationBean.setOrder(10);
        // 5. 返回注册对象
        return registrationBean;
    }


}
