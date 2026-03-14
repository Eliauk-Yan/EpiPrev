package com.nexo.common.file.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @classname FileService
 * @description 文件存储服务接口 (RustFS/S3 兼容)
 * @date 2025/12/12 10:52
 * @created by YanShijie
 */
public interface FileService {


    /**
     * 上传文件
     * @param file 文件
     * @param filePath 文件路径
     * @return 返回访问地址
     */
    String uploadFile(MultipartFile file, String filePath);

    /**
     * 下载文件
     * @param filePath 文件路径
     * @return 下载链接
     */
    String downloadFile(String filePath);

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     */
    void deleteFile(String filePath);

}
