package com.epiprev.gateway.limiter;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SentinelConfiguration {

    @PostConstruct
    public void initBlockHandler() {
        GatewayCallbackManager.setBlockHandler((exchange, t) -> {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("code", "429"); // 或者使用 500，取决于前端拦截逻辑
            result.put("message", "限流啦，请求太频繁");
            result.put("data", null);
            return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(result));
        });
    }

}
