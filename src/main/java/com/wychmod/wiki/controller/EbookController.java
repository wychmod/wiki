package com.wychmod.wiki.controller;

import com.wychmod.wiki.req.EbookQueryReq;
import com.wychmod.wiki.req.EbookSaveReq;
import com.wychmod.wiki.resp.CommonResp;
import com.wychmod.wiki.resp.EbookQueryResp;
import com.wychmod.wiki.resp.PageResp;
import com.wychmod.wiki.service.EbookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/ebook") //访问公共前缀
public class EbookController {
    @Resource
    private EbookService ebookService;

    @GetMapping ("/list")
    public CommonResp list(EbookQueryReq req) {
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        PageResp<EbookQueryResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody EbookSaveReq req) {
        CommonResp resp = new CommonResp<>();
        ebookService.save(req);
        return resp;
    }
}
