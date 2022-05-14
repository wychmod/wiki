package com.wychmod.wiki.controller;

import com.wychmod.wiki.req.UserQueryReq;
import com.wychmod.wiki.req.UserSaveReq;
import com.wychmod.wiki.resp.CommonResp;
import com.wychmod.wiki.resp.PageResp;
import com.wychmod.wiki.resp.UserQueryResp;
import com.wychmod.wiki.service.UserService;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/user") //访问公共前缀
public class UserController {
    @Resource
    private UserService userService;

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

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable long id) {
        CommonResp resp = new CommonResp<>();
        userService.delete(id);
        return resp;
    }
}
