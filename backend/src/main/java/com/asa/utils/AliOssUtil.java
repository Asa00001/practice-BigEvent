package com.asa.utils;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.asa.config.AliOssProperties;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class AliOssUtil {
    private final AliOssProperties properties;

    public AliOssUtil(AliOssProperties properties) {
        this.properties = properties;
    }

    public String uploadFile(String objectName, InputStream in) {

        CredentialsProvider credentialsProvider =
                new DefaultCredentialProvider(
                        properties.getAccessKeyId(),
                        properties.getAccessKeySecret()
                );

        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);

        OSS ossClient = OSSClientBuilder.create()
                    .endpoint(properties.getEndpoint())
                    .credentialsProvider(credentialsProvider)
                    .clientConfiguration(clientBuilderConfiguration)
                    .region(properties.getRegion())
                    .build();

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    properties.getBucketName(), objectName, in);

            PutObjectResult result = ossClient.putObject(putObjectRequest);
            //url组成：https://bucket名称.区域节点/objectName
            return "https://" + properties.getBucketName() + "."+
                    properties.getEndpoint().substring(properties.getEndpoint().lastIndexOf("/")+1)+
                    "/" + objectName;
        } catch (OSSException e) {
            throw new RuntimeException(
                    "文件上传到 OSS 失败：" + e.getErrorMessage(), e);
        } catch (ClientException e) {
            throw new RuntimeException(
                    "连接 OSS 服务失败：" + e.getMessage(), e);
        } finally {
            ossClient.shutdown();
        }
    }
}


