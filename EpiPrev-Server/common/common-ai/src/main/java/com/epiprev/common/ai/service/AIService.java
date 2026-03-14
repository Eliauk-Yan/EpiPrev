package com.epiprev.common.ai.service;

import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;

public interface AIService {

    String call(String prompt) throws NoApiKeyException, InputRequiredException;
}
