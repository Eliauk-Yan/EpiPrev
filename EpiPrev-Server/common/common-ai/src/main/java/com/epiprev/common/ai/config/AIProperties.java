package com.epiprev.common.ai.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "ai")
public class AIProperties {

    private String apiKey;

    private String appId;

}
