package com.wychmod.wiki.controller;

import com.wychmod.wiki.req.DocQueryReq;
import com.wychmod.wiki.req.DocSaveReq;
import com.wychmod.wiki.resp.DocQueryResp;
import com.wychmod.wiki.resp.CommonResp;
import com.wychmod.wiki.resp.PageResp;
import com.wychmod.wiki.service.DocService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/doc") //访问公共前缀
public class DocController {
    @Resource
    private DocService docService;

    @GetMapping ("/all")
    public CommonResp all() {
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> list = docService.all();
        resp.setContent(list);
        return resp;
    }

    @GetMapping ("/list")
    public CommonResp list(@Valid DocQueryReq req) {
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        PageResp<DocQueryResp> list = docService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody DocSaveReq req) {
        CommonResp resp = new CommonResp<>();
        docService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable long id) {
        CommonResp resp = new CommonResp<>();
        docService.delete(id);
        return resp;
    }
}
