package com.nexo.common.file.config;

import com.nexo.common.file.service.FileService;
import com.nexo.common.file.service.impl.FileServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

/**
 * @classname FileConfiguration
 * @description 文件存储配置 (RustFS/S3)
 * @date 2025/12/12 10:45
 * @created by YanShijie
 */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(FileProperties.class)
@ConditionalOnProperty(prefix = "rustfs", name = "enabled", havingValue = "true", matchIfMissing = true)
public class FileConfiguration {

    @Bean
    public S3Client s3Client(FileProperties fileProperties) {
        try {
            // 1. 创建客户端
            S3Client s3Client = S3Client.builder()
                    .endpointOverride(URI.create(fileProperties.getEndpoint()))
                    .region(Region.US_EAST_1) // RustFS doesn't validate regions
                    .credentialsProvider(
                            StaticCredentialsProvider.create(
                                    AwsBasicCredentials.create(fileProperties.getAccessKey(),
                                            fileProperties.getSecretKey())))
                    .forcePathStyle(true) // Required for RustFS compatibility
                    .build();

            // 2. 确保桶被创建
            try {
                s3Client.createBucket(CreateBucketRequest.builder().bucket(fileProperties.getBucketName()).build());
            } catch (BucketAlreadyExistsException | BucketAlreadyOwnedByYouException e) {
                log.info("桶: {}, 已经被创建...", fileProperties.getBucketName());
            }
            log.info("RustFS (S3) 客户端初始化成功，端点: {}, 桶: {}", fileProperties.getEndpoint(), fileProperties.getBucketName());
            // 3. 返回客户端
            return s3Client;
        } catch (Exception e) {
            log.error("RustFS 客户端初始化失败", e);
            throw new RuntimeException("RustFS客户端初始化失败: " + e.getMessage(), e);
        }
    }

    /**
     * 初始化 S3 预签名管理器 (S3Presigner)
     */
    @Bean
    public S3Presigner presigner(FileProperties fileProperties) {
        return S3Presigner.builder()
                .endpointOverride(URI.create(fileProperties.getEndpoint()))
                .region(Region.US_EAST_1)
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(fileProperties.getAccessKey(), fileProperties.getSecretKey())
                        )
                )
                .build();
    }

    /**
     * 暴露对象存储服务
     */
    @Bean
    @ConditionalOnMissingBean
    public FileService fileService(S3Client s3Client, S3Presigner presigner, FileProperties fileProperties) {
        return new FileServiceImpl(s3Client, presigner, fileProperties);
    }
}
