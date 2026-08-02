package com.asa.controller;

import com.asa.pojo.Result;
import com.asa.utils.AliOssUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
public class FileUploadController {

    private final AliOssUtil aliOssUtil;
    public FileUploadController(AliOssUtil aliOssUtil) {
        this.aliOssUtil = aliOssUtil;
    }

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
        //把文件内容存储到本地磁盘
        String originalFilename = file.getOriginalFilename();
        //保证文件名唯一，从而防止文件覆盖
        String filename = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
        //file.transferTo(new File("/Users/asa/Desktop/04_测试用例", filename));
        String url = aliOssUtil.uploadFile(filename, file.getInputStream());
        return Result.success(url);
    }
}
