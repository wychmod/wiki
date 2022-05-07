package com.wychmod.wiki.controller;

import com.wychmod.wiki.req.CategoryQueryReq;
import com.wychmod.wiki.req.CategorySaveReq;
import com.wychmod.wiki.resp.CommonResp;
import com.wychmod.wiki.resp.CategoryQueryResp;
import com.wychmod.wiki.resp.PageResp;
import com.wychmod.wiki.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/category") //访问公共前缀
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    @GetMapping ("/list")
    public CommonResp list(@Valid CategoryQueryReq req) {
        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
        PageResp<CategoryQueryResp> list = categoryService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody CategorySaveReq req) {
        CommonResp resp = new CommonResp<>();
        categoryService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable long id) {
        CommonResp resp = new CommonResp<>();
        categoryService.delete(id);
        return resp;
    }
}
