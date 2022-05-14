package com.wychmod.wiki.controller;

import com.alibaba.fastjson.JSONObject;
import com.wychmod.wiki.req.UserLoginReq;
import com.wychmod.wiki.req.UserQueryReq;
import com.wychmod.wiki.req.UserResetPasswordReq;
import com.wychmod.wiki.req.UserSaveReq;
import com.wychmod.wiki.resp.CommonResp;
import com.wychmod.wiki.resp.PageResp;
import com.wychmod.wiki.resp.UserLoginResp;
import com.wychmod.wiki.resp.UserQueryResp;
import com.wychmod.wiki.service.EbookService;
import com.wychmod.wiki.service.UserService;
import com.wychmod.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/user") //访问公共前缀
public class UserController {
    @Resource
    private UserService userService;

    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private SnowFlake snowFlake;
    @GetMapping ("/list")
    public CommonResp list(@Valid UserQueryReq req) {
        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
        PageResp<UserQueryResp> list = userService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody UserSaveReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp resp = new CommonResp<>();
        userService.save(req);
        return resp;
    }

    @PostMapping("/reset-password")
    public CommonResp resetPassword(@Valid @RequestBody UserResetPasswordReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp resp = new CommonResp<>();
        userService.resetPassword(req);
        return resp;
    }

    @PostMapping("/login")
    public CommonResp<UserLoginResp> login(@Valid @RequestBody UserLoginReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp<UserLoginResp> resp = new CommonResp<>();
        UserLoginResp userLoginResp = userService.login(req);

        Long token = snowFlake.nextId();
        LOG.info("生成单点登录 token:{}，并放入 redis 中", token);
        userLoginResp.setToken(token.toString());
        redisTemplate.opsForValue().set(token.toString(), JSONObject.toJSONString(userLoginResp));

        resp.setContent(userLoginResp);
        return resp;
    }

    @GetMapping ("/delete/{token}")
    public CommonResp logout(@PathVariable String token) {
        CommonResp resp = new CommonResp<>();
        redisTemplate.delete(token);
        LOG.info("从 redis 中删除 token：{}",token);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable long id) {
        CommonResp resp = new CommonResp<>();
        userService.delete(id);
        return resp;
    }
}
