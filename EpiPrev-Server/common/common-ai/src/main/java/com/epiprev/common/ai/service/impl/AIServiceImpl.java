package com.epiprev.common.ai.service.impl;

import com.alibaba.dashscope.app.Application;
import com.alibaba.dashscope.app.ApplicationParam;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.epiprev.common.ai.config.AIProperties;
import com.epiprev.common.ai.service.AIService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AIServiceImpl implements AIService {

    private final AIProperties aiProperties;

    @Override
    public String call(String prompt) throws NoApiKeyException, InputRequiredException {
        // 1. 构建应用参数
        ApplicationParam param = ApplicationParam.builder()
                .apiKey(aiProperties.getApiKey())
                .appId(aiProperties.getAppId())
                .prompt(prompt)
                .build();
        // 2. 调用大模型并返回
        Application application = new Application();
        return application.call(param).getOutput().getText();
    }
}
