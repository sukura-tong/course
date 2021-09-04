package com.swust.server.service;

import com.swust.server.pojo.File;
import com.swust.server.pojo.FileExample;
import com.swust.server.dto.FileDto;
import com.swust.server.dto.PageDto;
import com.swust.server.mapper.FileMapper;
import com.swust.server.util.CopyUtil;
import com.swust.server.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Date;

@Service
public class FileService {

    @Resource
    private FileMapper fileMapper;

    /**
     * 列表查询
     */
    public PageDto list(PageDto pageDto) {
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        FileExample fileExample = new FileExample();
        List<File> fileList = fileMapper.selectByExample(fileExample);

        PageInfo<File> pageInfo = new PageInfo<>(fileList);
        pageDto.setTotal(pageInfo.getTotal());

        pageDto.<PageDto>setList(fileList);
            return pageDto;
    }

    /**
     * 保存，id有值时更新，无值时新增
     */
    public String save(FileDto fileDto) {
        File file = CopyUtil.copy(fileDto, File.class);

        File fileDb = selectByKey(fileDto.getKey());

        if (StringUtils.isEmpty(fileDb)) {
            String insert = this.insert(file);
            return insert;
        } else {
//            fileDb.setShardIndex(file.getShardIndex());
            this.update(file);
            return file.getId();
        }
    }

    /**
     * 新增
     */
    private String insert(File file) {
                // 追加创建时间和修改编辑时间
        Date now = new Date();
        String uuid = UuidUtil.getShortUuid();
        file.setId(uuid);
        fileMapper.insert(file);
        return uuid;
    }

    /**
     * 更新
     */
    private void update(File file) {
        fileMapper.updateByPrimaryKey(file);
    }

    /**
     * 删除
     */
    public void delete(String id) {
        fileMapper.deleteByPrimaryKey(id);
    }

    public File selectByKey(String key){
        FileExample fe = new FileExample();
        FileExample.Criteria criteria = fe.createCriteria().andKeyEqualTo(key);
        List<File> files = fileMapper.selectByExample(fe);

        if (CollectionUtils.isEmpty(files)){
            return null;
        }else {
            return files.get(0);
        }
    }

    public FileDto findByKey(String key){
        File file = selectByKey(key);
        FileDto copy = CopyUtil.copy(file, FileDto.class);
        return copy;
    }
}
