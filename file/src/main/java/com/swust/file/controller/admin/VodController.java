package com.swust.file.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.AppendObjectRequest;
import com.aliyun.oss.model.AppendObjectResult;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetMezzanineInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.swust.server.dto.FileDto;
import com.swust.server.dto.ResponseDto;
import com.swust.server.enums.FileUseEnum;
import com.swust.server.service.FileService;
import com.swust.server.util.Base64ToMultipartFile;
import com.swust.server.util.UuidUtil;
import com.swust.server.util.VodUtil;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping(value = "/upload")
public class VodController {

    private static final Logger logger = LoggerFactory.getLogger(VodController.class);

    @Value("${vod.accessKeyId}")
    private String accessKeyId;

    @Value("${vod.accessKeySecret}")
    private String accessKeySecret;

    public static final String BUSINESS_NAME = "VOD文件上传";


    @Autowired
    private FileService fileService;

    @PostMapping(value = "/vod-append")
    public ResponseDto fileUpload(@RequestBody FileDto fileDto) throws Exception {
        logger.info("vod文件上传开始处理");

        ResponseDto responseDto = new ResponseDto();

        String use = fileDto.getUse();
        String key = fileDto.getKey();
        String suffix = fileDto.getSuffix();
        Integer shardIndex = fileDto.getShardIndex();
        Integer shardSize = fileDto.getShardSize();
        String shardBase64 = fileDto.getShard();

        // 文件保存到本地
        FileUseEnum useEnum = FileUseEnum.getByCode(use);
        String dir = useEnum.name().toLowerCase();

        String path = new StringBuffer(dir)
                .append("/")
                .append(key)
                .append(".")
                .append(suffix)
                .toString();

        MultipartFile shard = Base64ToMultipartFile.base64ToMultipart(shardBase64);

        // vod 文件上传
        String vod = "";
        String fileUrl = "";

        try {
            // 初始化vod连接客户端
            DefaultAcsClient vodClient = VodUtil.initVodClient(accessKeyId, accessKeySecret);
            CreateUploadVideoResponse createUploadVideoResponse = VodUtil.createUploadVideo(vodClient, path);
            // 指行成功返回videoId、UploadAccress、UploadAuth
            vod = createUploadVideoResponse.getVideoId();
            JSONObject uploadAuth = JSONObject.parseObject(
                    Base64.decodeBase64(createUploadVideoResponse.getUploadAuth()), JSONObject.class);
            JSONObject uploadAddress = JSONObject.parseObject(
                    Base64.decodeBase64(createUploadVideoResponse.getUploadAddress()), JSONObject.class);

            // 使用uploaadAuth和uploadaddress初始化oss客户端
            OSSClient ossClient = VodUtil.initOssClient(uploadAuth, uploadAddress);
            // 上传oss文件
            VodUtil.uploadLocalFile(ossClient,uploadAddress,shard.getInputStream());
            logger.info("上传视频成功, vod : " + vod);
            GetMezzanineInfoResponse response = VodUtil.getMezzanineInfo(vodClient, vod);
            System.out.println("获取视频信息, response : " + JSON.toJSONString(response));
            fileUrl = response.getMezzanine().getFileURL();

            // 关闭流
            ossClient.shutdown();
        }catch (Exception e){
            logger.error("视频传输失败 ==> {}",vod);
            logger.error("errmessage ==> {}",e.getMessage());
        }
        logger.info("保存文件记录开始");
        fileDto.setPath(path);
        fileDto.setVod(vod);
        fileService.save(fileDto);
        // 获取时长
        fileDto.setPath(fileUrl);
        responseDto.setContent(fileDto);
        return responseDto;
    }

    @RequestMapping(value = "/getauth/{vod}", method = RequestMethod.GET)
    public ResponseDto getAuth(@PathVariable String vod) throws ClientException {
        logger.info("开始获取视频授权码");

        ResponseDto responseDto = new ResponseDto();

        DefaultAcsClient client = VodUtil.initVodClient(accessKeyId,accessKeySecret);
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();

        try {
            response = VodUtil.getVideoPlayAuth(client, vod);
            logger.info("授权码{}",response.getPlayAuth());
            responseDto.setContent(response.getPlayAuth());
            // video meta data
            logger.info("video metadata {}", JSON.toJSONString(response.getVideoMeta()));
        }catch (Exception e){
            logger.error("error message ==> {}", e.getMessage());
            e.printStackTrace();
        }
        logger.info("结束获取视频授权码");
        return responseDto;
    }


}
