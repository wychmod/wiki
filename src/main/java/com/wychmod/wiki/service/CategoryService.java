package com.wychmod.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wychmod.wiki.domain.Category;
import com.wychmod.wiki.domain.CategoryExample;
import com.wychmod.wiki.mapper.CategoryMapper;
import com.wychmod.wiki.req.CategoryQueryReq;
import com.wychmod.wiki.req.CategorySaveReq;
import com.wychmod.wiki.resp.CategoryQueryResp;
import com.wychmod.wiki.resp.PageResp;
import com.wychmod.wiki.util.CopyUtil;
import com.wychmod.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryService {
    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private SnowFlake snowFlake;

    public PageResp<CategoryQueryResp> list(CategoryQueryReq req) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        // 分页 pagenum查第几页 pagesize查几条 一条pagehelper只对遇见的第一个查询有效
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        // PageInfo 可以获得总行数和总页数
        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        LOG.info("总行数：{}",pageInfo.getTotal());
        LOG.info("总页数：{}",pageInfo.getPages());

        List<CategoryQueryResp> list = CopyUtil.copyList(categoryList, CategoryQueryResp.class);
        PageResp<CategoryQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    /**
     * 保存
     * @param req
     */
    public void save(CategorySaveReq req) {
        Category category = CopyUtil.copy(req, Category.class);
        if (ObjectUtils.isEmpty(req.getId())){
            // 新增
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        } else {
            // 更新
            categoryMapper.updateByPrimaryKey(category);
        }
    }

    public void delete(long id) {
        categoryMapper.deleteByPrimaryKey(id);
    }
}
