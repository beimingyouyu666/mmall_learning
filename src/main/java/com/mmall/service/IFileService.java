package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Auther: yangkun
 * @Date: 2019/2/24 0024 22:52
 * @Description:
 */
public interface IFileService {

    String upload(MultipartFile file, String path);
}
