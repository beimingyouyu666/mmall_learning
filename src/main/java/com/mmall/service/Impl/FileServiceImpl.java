package com.mmall.service.Impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @Auther: yangkun
 * @Date: 2019/2/24 0024 22:53
 * @Description:
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService{

    private final  Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file,String path){

        //先得到文件名，拿到后缀后再用uuid新建文件名
        String originalFilename = file.getOriginalFilename();
        String end = originalFilename.substring(originalFilename.indexOf(".") + 1);
        String fileName = UUID.randomUUID().toString()+"."+end;
        logger.info("开始上传文件,上传文件的文件名:{},上传的路径:{},新文件名:{}",originalFilename,path,fileName);
        //先根据path创建文件夹
        File fileDir = new File(path);
        if (!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path,fileName);
        try {
            file.transferTo(targetFile);
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //将文件上传到服务器上后删除本地文件
            targetFile.delete();
        } catch (IOException e) {
            logger.error("上传文件异常",e);
            return null;
        }
        return targetFile.getName();
    }
}
