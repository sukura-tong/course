package com.swust.server.service;

import com.swust.server.pojo.Category;
import com.swust.server.pojo.CategoryExample;
import com.swust.server.dto.CategoryDto;
import com.swust.server.dto.PageDto;
import com.swust.server.mapper.CategoryMapper;
import com.swust.server.util.CopyUtil;
import com.swust.server.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    /**
     * 列表查询
     */
    public PageDto list(PageDto pageDto) {
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        CategoryExample categoryExample = new CategoryExample();
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        pageDto.setTotal(pageInfo.getTotal());

        pageDto.<PageDto>setList(categoryList);
            return pageDto;
    }

    public PageDto selectAll(PageDto pageDto){
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        pageDto.setTotal(pageInfo.getTotal());

        pageDto.<PageDto>setList(categoryList);
        return pageDto;
    }


    /**
     * 保存，id有值时更新，无值时新增
     */
    public void save(CategoryDto categoryDto) {
        Category category = CopyUtil.copy(categoryDto, Category.class);
        if (StringUtils.isEmpty(categoryDto.getId())) {
            this.insert(category);
        } else {
            this.update(category);
        }
    }

    /**
     * 新增
     */
    private void insert(Category category) {

        category.setId(UuidUtil.getShortUuid());
        categoryMapper.insert(category);
    }

    /**
     * 更新
     */
    private void update(Category category) {
        categoryMapper.updateByPrimaryKey(category);
    }

    /**
     * 删除
     */
    @Transactional
    public void delete(String id) {
        deleteAllChildren(id);
        categoryMapper.deleteByPrimaryKey(id);
    }

    public void deleteAllChildren(String parentId){
        Category category = categoryMapper.selectByPrimaryKey(parentId);
        //如果是父分类 则先删除下面所有子节点
        if ("00000000".equals(category.getParent())) {
            CategoryExample categoryExample = new CategoryExample();
            categoryExample.createCriteria().andParentEqualTo(parentId);
            categoryMapper.deleteByExample(categoryExample);
        }

    }
}
