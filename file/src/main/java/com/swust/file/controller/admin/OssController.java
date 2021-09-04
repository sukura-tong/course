package com.swust.file.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.AppendObjectRequest;
import com.aliyun.oss.model.AppendObjectResult;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.swust.server.dto.FileDto;
import com.swust.server.dto.ResponseDto;
import com.swust.server.enums.FileUseEnum;
import com.swust.server.service.FileService;
import com.swust.server.util.Base64ToMultipartFile;
import com.swust.server.util.UuidUtil;
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
public class OssController {

    private static final Logger logger = LoggerFactory.getLogger(OssController.class);

    @Value("${oss.accessKeyId}")
    private String accessKeyId;

    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${oss.endpoint}")
    private String endpoint;

    @Value("${oss.bucket}")
    private String bucket;

    @Value("${oss.domain}")
    private String ossDomain;

    public static final String BUSINESS_NAME = "OSS文件上传";


    @Autowired
    private FileService fileService;

    @PostMapping(value = "/oss-append")
    public ResponseDto fileUpload(@RequestBody FileDto fileDto) throws IOException {
        logger.info("追加上传开始处理");
        ResponseDto responseDto = new ResponseDto();

        String use = fileDto.getUse();
        String key = fileDto.getKey();
        String name = fileDto.getName();
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

        // 创建oss实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 指定文件上传类型
        ObjectMetadata metadata = new ObjectMetadata();
        // 通过AppendObjectRequest设置多个参数。
        AppendObjectRequest appendObjectRequest = new AppendObjectRequest(
                bucket, path, new ByteArrayInputStream(shard.getBytes()), metadata);
        // 通过AppendObjectRequest设置单个参数。
        // 设置存储空间名称。
        //appendObjectRequest.setBucketName("<yourBucketName>");
        // 设置文件名称。
        //appendObjectRequest.setKey("<yourObjectName>");
        // 设置待追加的内容。有两种可选类型：InputStream类型和File类型。这里为InputStream类型。
        //appendObjectRequest.setInputStream(new ByteArrayInputStream(content1.getBytes()));
        // 设置待追加的内容。有两种可选类型：InputStream类型和File类型。这里为File类型。
        //appendObjectRequest.setFile(new File("<yourLocalFile>"));
        // 指定文件的元信息，第一次追加时有效。
        //appendObjectRequest.setMetadata(meta);

        // 第一次追加。
        // 设置文件的追加位置。
        appendObjectRequest.setPosition((long)((shardIndex - 1) * shardSize));
        AppendObjectResult appendObjectResult = ossClient.appendObject(appendObjectRequest);
        // 文件的64位CRC值。此值根据ECMA-182标准计算得出。
        System.out.println(appendObjectResult.getObjectCRC());
        System.out.println(JSONObject.toJSONString(appendObjectResult));

        // 第二次追加。
//      // nextPosition指明下一次请求中应当提供的Position，即文件当前的长度。
//        appendObjectRequest.setPosition(appendObjectResult.getNextPosition());
//        appendObjectRequest.setInputStream(new ByteArrayInputStream(content2.getBytes()));
//        appendObjectResult = ossClient.appendObject(appendObjectRequest);

        // 结束
        ossClient.shutdown();
        logger.info("保存文件记录");

        fileDto.setPath(path);
        fileService.save(fileDto);
        fileDto.setPath(ossDomain + path);
        responseDto.setContent(fileDto);

        return responseDto;
    }

    @PostMapping(value = "/oss-simple")
    public ResponseDto fileUpload(@RequestParam MultipartFile multipartFile, String use) throws IOException {
        logger.info("开始文件上传");

        FileUseEnum useEnum = FileUseEnum.getByCode(use);
        String key = UuidUtil.getShortUuid();
        String fileName = multipartFile.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        String dir = useEnum.name().toLowerCase();
        String path = dir + "/" + key + "." + suffix;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 创建PutObjectRequest对象。
//        String content = "Hello OSS";
        // <yourObjectName>表示上传文件到OSS时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
        PutObjectRequest putObjectRequest = new PutObjectRequest(
                bucket, path, new ByteArrayInputStream(multipartFile.getBytes()));

        // 如果需要上传时设置存储类型与访问权限，请参考以下示例代码。
        // ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        // metadata.setObjectAcl(CannedAccessControlList.Private);
        // putObjectRequest.setMetadata(metadata);

        // 上传字符串。
        ossClient.putObject(putObjectRequest);

        ResponseDto responseDto = new ResponseDto();
        FileDto fileDto = new FileDto();
        fileDto.setPath(ossDomain + path);
        responseDto.setContent(fileDto);

        return responseDto;

    }
}
