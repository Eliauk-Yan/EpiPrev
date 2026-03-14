package com.nexo.common.file.service.impl;

import com.nexo.common.file.config.FileProperties;
import com.nexo.common.file.domain.exception.FileException;
import com.nexo.common.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;

import static com.nexo.common.file.constant.FileConstant.SEPARATOR;
import static com.nexo.common.file.domain.exception.FileErrorCode.*;

/**
 * @classname FileServiceImpl
 * @description RustFS (S3 兼容) 文件存储服务实现类
 * @date 2024/01/30
 */
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final S3Client s3Client;

    private final S3Presigner s3Presigner;

    private final FileProperties fileProperties;

    @Override
    public String uploadFile(MultipartFile file, String filePath) {
        // 1. 上传文件
        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(fileProperties.getBucketName())
                            .key(filePath)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );
            log.info("文件已上传到 RustFS: {}", filePath);
        } catch (IOException e) {
            log.error("文件IO异常: {}", e.getMessage());
            throw new FileException(FILE_IO_EXCEPTION);
        } catch (S3Exception e) {
            log.error("文件上传失败: {}", e.getMessage());
            throw new FileException(FILE_UPLOAD_FAILED);
        }
        // 2. 构造文件访问路径
        return fileProperties.getEndpoint() + SEPARATOR + fileProperties.getBucketName() + SEPARATOR + filePath;
    }

    @Override
    public String downloadFile(String filePath) {
        // 1. 构建获取对象请求
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(fileProperties.getBucketName())
                .key(filePath)
                .build();
        // 2. 构建预签名请求
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(getObjectRequest)
                .signatureDuration(Duration.ofMinutes(15))
                .build();
        // 3. 执行签名并获取预签名 URL
        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }

    @Override
    public void deleteFile(String fileURL) {
        String filePath = fileURL.substring(fileURL.lastIndexOf(SEPARATOR) + 1);
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(fileProperties.getBucketName())
                    .key(filePath)
                    .build());
            log.info("文件已从 RustFS 删除: {}", filePath);
        } catch (S3Exception e) {
            log.error("文件删除失败: {}", e.getMessage());
            throw new FileException(FILE_DELETE_FAILED);
        }
    }
}
