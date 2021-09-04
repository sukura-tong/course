package com.swust.file.controller.admin;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetMezzanineInfoResponse;
import com.swust.server.dto.FileDto;
import com.swust.server.dto.PageDto;
import com.swust.server.dto.ResponseDto;
import com.swust.server.enums.FileUseEnum;
import com.swust.server.service.FileService;
import com.swust.server.util.Base64ToMultipartFile;
import com.swust.server.util.UuidUtil;
import com.swust.server.util.ValidatorUtil;
import com.swust.server.util.VodUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;

@RestController
@RequestMapping("/admin/file")
public class FileManagementController {

    private static final Logger LOG = LoggerFactory.getLogger(FileManagementController.class);
    public static final String BUSINESS_NAME = "文件";

    @Resource
    private FileService fileService;

    @Value("${vod.accessKeyId}")
    private String accessKeyId;

    @Value("${vod.accessKeySecret}")
    private String accessKeySecret;

    /**
     * 列表查询
     */
    @RequestMapping(value = "/queryfileinfo",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto list(@RequestBody PageDto pageDto) {
        ResponseDto responseDto = new ResponseDto();
        fileService.list(pageDto);
        responseDto.setContent(pageDto);
        return responseDto;
    }

    /**
     * 保存，id有值时更新，无值时新增
     */
    @PostMapping("/savefileinfo")
    public ResponseDto save(@RequestBody FileDto fileDto) {
        // 保存校验
        ValidatorUtil.require(fileDto.getPath(),"相对路径");
        ValidatorUtil.length(fileDto.getPath(),"相对路径",1,100);
        ValidatorUtil.length(fileDto.getName(),"文件名",1,100);
        ValidatorUtil.length(fileDto.getSuffix(),"后缀",1,10);

        ResponseDto responseDto = new ResponseDto();
        fileService.save(fileDto);
        responseDto.setContent(fileDto);
        return responseDto;
    }

    /**
     * 删除
     */
    @DeleteMapping("/deletefileinfo/{id}")
    public ResponseDto delete(@PathVariable String id) {
        ResponseDto responseDto = new ResponseDto();
        fileService.delete(id);
        return responseDto;
    }

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto upload(@RequestParam MultipartFile file, String use) throws IOException {
        ResponseDto responseDto = new ResponseDto();
        LOG.info("文件上传开始");

        FileUseEnum code = FileUseEnum.getByCode(use);

        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        String key = UuidUtil.getShortUuid();
        String localPath = "F:/项目代码/course/admins/public/static/image/" + code.name().toLowerCase();

        // 如果文件路径不存在则创建路径
        File localDir = new File(localPath);
        if (!localDir.exists()){
            localDir.mkdir();
        }
        String fullPath = localPath + "/" + key + "-" + fileName;
        File des = new File(fullPath);
        file.transferTo(des);

        // 存储数据到数据库
        String path = "/static/image/"+ code.name().toLowerCase() + "/"+ key + "." + suffix;
        FileDto fileDto = new FileDto();
        fileDto.setPath(path);
        fileDto.setName(fileName);
        fileDto.setSize(Math.toIntExact(file.getSize()));
        fileDto.setSuffix(suffix);
        fileDto.setUse(use);
        fileDto.setKey(key);
        String fileId = fileService.save(fileDto);
        fileDto.setId(fileId);

        LOG.info("文件保存完成");
        String dir = "/static/image/" + code.name().toLowerCase() + "/"+ key + "-" + fileName;
        fileDto.setPath(dir);
        responseDto.setContent(fileDto);

        LOG.info(responseDto.getContent().toString());
        return responseDto;
    }

//    @RequestParam MultipartFile shard,String use, String name,
//    String suffix,Integer size,Integer shardIndex,
//    Integer shardSize,Integer shardTotal,String key

    @RequestMapping(value = "/bigfileupload")
    @ResponseBody
    public ResponseDto bigFileUpload(@RequestBody FileDto fileDto) throws IOException {
        ResponseDto responseDto = new ResponseDto();
        LOG.info("文件上传开始");

        String use = fileDto.getUse();
        FileUseEnum code = FileUseEnum.getByCode(use);

//        String key = UuidUtil.getShortUuid();
        String localPath = "F:/项目代码/course/admins/public/static/image/" + code.name().toLowerCase();

        // 如果文件路径不存在则创建路径
        File localDir = new File(localPath);
        if (!localDir.exists()){
            localDir.mkdir();
        }

        String key = fileDto.getKey();
        String name = fileDto.getName();
        String suffix = fileDto.getSuffix();
        String shardBase64 = fileDto.getShard();

        String fullPath = localPath + "/" + key + "-" + name;
        // 将字符串转换为图片数据、存储到本地
        MultipartFile shard = Base64ToMultipartFile.base64ToMultipart(shardBase64);
        String localSavePath = fullPath + "." + fileDto.getShardIndex();
        File des = new File(localSavePath);
        shard.transferTo(des);


        // 存储数据到数据库
        String path = "/static/image/"+ code.name().toLowerCase() + "/"+ key + "." + suffix;
        fileDto.setPath(path);
        fileDto.setUse(use);

        String fileId = fileService.save(fileDto);
        fileDto.setId(fileId);

        LOG.info("文件保存完成");
        String dir = "/static/image/" + code.name().toLowerCase() + "/"+ key + "-" + name + "." + fileDto.getShardIndex();
        fileDto.setPath(dir);
        responseDto.setContent(fileDto);

        // 销毁文件对象
//        LOG.info(responseDto.getContent().toString());

        if (fileDto.getShardIndex().equals(fileDto.getShardTotal())){
            String merge = merge(fileDto);
            fileDto.setPath(merge);
            fileService.save(fileDto);
            responseDto.setContent(fileDto);

        }
        return responseDto;
    }

    /**
     * 分片数据合并
     * @param fileDto
     * @return
     * @throws FileNotFoundException
     */
    public String merge(FileDto fileDto) throws FileNotFoundException {
        int index = fileDto.getPath().lastIndexOf(".");
        String FILE_PATH = fileDto.getPath().substring(0,index);

        String obivousPath = "admins/public";
        String outputPath = obivousPath + FILE_PATH;
        File file = new File(outputPath);

        FileOutputStream outputStream = new FileOutputStream(file, true);
        FileInputStream inputStream = null;

        byte[] buffer = new byte[10 * 1024 * 1024];
        int len;

        try {
            for (int i = 0; i < fileDto.getShardIndex(); i++){
                int FILE_INDEX = i + 1;
                // 读取第一个分片
                File mergeFile = new File(outputPath + "." + FILE_INDEX);
                inputStream = new FileInputStream(mergeFile);
                while ((len = inputStream.read(buffer)) != -1){
                    //每次读满缓冲区 写入新文件
                    outputStream.write(buffer, 0 ,len);
                }
//                mergeFile.delete();
            }
        }catch (IOException e){
            e.printStackTrace();
            LOG.error(e.getMessage());
        }finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.gc();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        deleteFile(outputPath,fileDto.getShardTotal());
        return FILE_PATH;
    }
    /***
     * 文件合并完成后删除小文件
     * @param path
     */
    public void deleteFile(String path, int shardTotal){
        LOG.info("开始删除分片数据");
        for (int i = 0; i < shardTotal; i++){
            int FILE_INDEX = i + 1;
            String filePath = path + "." + FILE_INDEX;
            LOG.info("数据路径 {}",filePath);
            File file = new File(filePath);
            try {
                boolean delete = file.delete();
                if (delete){
                    LOG.info("删除分片{}数据成功",FILE_INDEX);
                }
            }catch (Exception e){
                LOG.error(e.getMessage());
            }
        }
    }

    @GetMapping(value = "/check/{key}")
    public ResponseDto check(@PathVariable String key) throws Exception {
        LOG.info("文件检查开始");
        ResponseDto responseDto = new ResponseDto();
        FileDto fileDto = fileService.findByKey(key);
        if (fileDto != null){
           if (StringUtils.isEmpty(fileDto.getVod())){
               fileDto.setPath(fileDto.getPath());
           }else {
               DefaultAcsClient vodClient = VodUtil.initVodClient(accessKeyId,accessKeySecret);
               GetMezzanineInfoResponse response = VodUtil.getMezzanineInfo(vodClient, fileDto.getVod());
               String fileUrl = response.getMezzanine().getFileURL();
               fileDto.setPath(fileUrl);
           }
        }
        responseDto.setContent(fileDto);
        return responseDto;
    }
}
