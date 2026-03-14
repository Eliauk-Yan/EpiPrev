package com.epiprev.common.ai.config;

import com.epiprev.common.ai.service.AIService;
import com.epiprev.common.ai.service.impl.AIServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(AIProperties.class)
@Slf4j
public class AIConfiguration {

    /**
     * 对外暴露AI服务
     */
    @Bean
    @ConditionalOnMissingBean
    public AIService fileService(AIProperties aiProperties) {
        return new AIServiceImpl(aiProperties);
    }

}
