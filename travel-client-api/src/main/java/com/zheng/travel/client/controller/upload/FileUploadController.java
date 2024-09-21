package com.zheng.travel.client.controller.upload;

import com.zheng.travel.client.config.middle.minio.MinIOConfig;
import com.zheng.travel.client.config.middle.minio.MinIoUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(tags="文件上传")
@Slf4j
public class FileUploadController {

    @Autowired
    private MinIoUploadService minIoUploadService;
    @Autowired
    private MinIOConfig minIOConfig;

    @PostMapping("upload")
    @ApiOperation(value = "文件上传")
    public String upload(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        minIoUploadService.uploadFile(minIOConfig.getBucketName(), fileName, file.getInputStream());
        String imgUrl = minIOConfig.getFileHost()
                + "/"
                + minIOConfig.getBucketName()
                + "/"
                + fileName;

        return imgUrl;
    }
}
