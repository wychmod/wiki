package com.wychmod.wiki.controller;

import com.wychmod.wiki.req.EbookReq;
import com.wychmod.wiki.resp.CommonResp;
import com.wychmod.wiki.resp.EbookResp;
import com.wychmod.wiki.service.EbookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/ebook") //访问公共前缀
public class EbookController {
    @Resource
    private EbookService ebookService;

    @GetMapping ("/list")
    public CommonResp list(EbookReq req) {
        CommonResp<List<EbookResp>> resp = new CommonResp<>();
        List<EbookResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }
}
