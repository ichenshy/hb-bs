package com.chen.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author ChenShengyuan
 */
public interface FileService {
    /**
     * 上传头像到OSS
     *
     * @param file
     * @return
     */
    String uploadFileAvatar(MultipartFile file);
}
