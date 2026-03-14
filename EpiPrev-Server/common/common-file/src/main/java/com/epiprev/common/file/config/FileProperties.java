package com.nexo.common.file.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @classname FileProperties
 * @description 文件存储配置属性类
 * @date 2025/12/12 10:46
 * @created by YanShijie
 */
@Data
@ConfigurationProperties(prefix = "rustfs")
public class FileProperties {

    /**
     * 是否启用RustFS（默认启用）
     */
    private boolean enabled = true;

    /**
     * RustFS 服务端点
     */
    private String endpoint;

    /**
     * 访问密钥
     */
    private String accessKey;

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 桶名称
     */
    private String bucketName = "nexo";

}
