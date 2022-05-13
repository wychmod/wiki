package com.wychmod.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wychmod.wiki.domain.User;
import com.wychmod.wiki.domain.UserExample;
import com.wychmod.wiki.mapper.UserMapper;
import com.wychmod.wiki.req.UserQueryReq;
import com.wychmod.wiki.req.UserSaveReq;
import com.wychmod.wiki.resp.UserQueryResp;
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
public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    @Resource
    private UserMapper userMapper;
    @Resource
    private SnowFlake snowFlake;

    public PageResp<UserQueryResp> list(UserQueryReq req) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getLoginName())){
            criteria.andLoginNameLike("%"+req.getLoginName()+"%");
        }
        // 分页 pagenum查第几页 pagesize查几条 一条pagehelper只对遇见的第一个查询有效
        PageHelper.startPage(req.getPage(), req.getSize());
        List<User> userList = userMapper.selectByExample(userExample);
        // PageInfo 可以获得总行数和总页数
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        LOG.info("总行数：{}",pageInfo.getTotal());
        LOG.info("总页数：{}",pageInfo.getPages());

        List<UserQueryResp> list = CopyUtil.copyList(userList, UserQueryResp.class);
        PageResp<UserQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    /**
     * 保存
     * @param req
     */
    public void save(UserSaveReq req) {
        User user = CopyUtil.copy(req, User.class);
        if (ObjectUtils.isEmpty(req.getId())){
            // 新增
            user.setId(snowFlake.nextId());
            userMapper.insert(user);
        } else {
            // 更新
            userMapper.updateByPrimaryKey(user);
        }
    }

    public void delete(long id) {
        userMapper.deleteByPrimaryKey(id);
    }
}
