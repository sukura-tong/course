package com.swust.server.service;

import com.alibaba.fastjson.JSON;
import com.swust.server.pojo.Resources;
import com.swust.server.pojo.ResourcesExample;
import com.swust.server.dto.ResourcesDto;
import com.swust.server.dto.PageDto;
import com.swust.server.mapper.ResourcesMapper;
import com.swust.server.util.CopyUtil;
import com.swust.server.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResourcesService {

    @Resource
    private ResourcesMapper resourcesMapper;

    /**
     * 列表查询
     */
    public PageDto list(PageDto pageDto) {
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        ResourcesExample resourcesExample = new ResourcesExample();
        List<Resources> resourcesList = resourcesMapper.selectByExample(resourcesExample);

        PageInfo<Resources> pageInfo = new PageInfo<>(resourcesList);
        pageDto.setTotal(pageInfo.getTotal());

        pageDto.<PageDto>setList(resourcesList);
            return pageDto;
    }

    /**
     * 保存，id有值时更新，无值时新增
     */
    public void save(ResourcesDto resourcesDto) {
        Resources resources = CopyUtil.copy(resourcesDto, Resources.class);
        if (StringUtils.isEmpty(resourcesDto.getId())) {
            this.insert(resources);
        } else {
            this.update(resources);
        }
    }

    /**
     * 新增
     */
    private void insert(Resources resources) {

        resources.setId(UuidUtil.getShortUuid());
        resourcesMapper.insert(resources);
    }

    /**
     * 更新
     */
    private void update(Resources resources) {
        resourcesMapper.updateByPrimaryKey(resources);
    }

    /**
     * 删除
     */
    public void delete(String id) {
        resourcesMapper.deleteByPrimaryKey(id);
    }

    /**
     * 保存资源树
     */
    @Transactional
    public void saveJson(String json){
        List<ResourcesDto> jsonlist = JSON.parseArray(json, ResourcesDto.class);
        List<ResourcesDto> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(jsonlist)){
            for (ResourcesDto resourcesDto : jsonlist){
                resourcesDto.setParent(null);
                addJsonToList(list,resourcesDto);
            }
        }
        System.out.println("共计"+ list.size() +"条");
        // 每次更新资源清空原始表
        resourcesMapper.deleteByExample(null);
        for (int i = 0; i < list.size(); i++){
            this.insert(CopyUtil.copy(list.get(i), Resources.class));
        }
    }

    public void addJsonToList(List<ResourcesDto> list, ResourcesDto resourcesDto){
        list.add(resourcesDto);
        if (!CollectionUtils.isEmpty(resourcesDto.getChildren())){
            for (ResourcesDto childrenDto : resourcesDto.getChildren()){
                childrenDto.setParent(resourcesDto.getId());
                addJsonToList(list,childrenDto);
            }
        }
    }

    public List<ResourcesDto> loadTree(){
        List<ResourcesDto> list = new ArrayList<>();
        ResourcesExample re = new ResourcesExample();
        re.setOrderByClause("id asc");
        List<Resources> resources = resourcesMapper.selectByExample(re);
        list = CopyUtil.copyList(resources, ResourcesDto.class);

        for (int i = list.size() - 1; i >= 0; i--){
            // 当前要移动的记录
            ResourcesDto children = list.get(i);
            // 如果当前节点没有父节点 则不需要向下搜找
            if (StringUtils.isEmpty(children.getParent())){
                continue;
            }
            // 查找父节点
            for (int j = i - 1; j >= 0; j--){
                ResourcesDto parent = list.get(j);
                if (children.getParent().equals(parent.getId())){
                    if (CollectionUtils.isEmpty(parent.getChildren())){
                        parent.setChildren(new ArrayList<>());
                    }
                    // 追加到最前面
                    parent.getChildren().add(0,children);
                    // 字节的找到父节点后，删除列表中的子节点
                    list.remove(children);
                }
            }
        }

        return list;
    }
}
