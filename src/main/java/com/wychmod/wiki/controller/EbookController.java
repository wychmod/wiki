package com.wychmod.wiki.controller;

import com.wychmod.wiki.domain.Ebook;
import com.wychmod.wiki.service.EbookService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/ebook") //访问公共前缀
public class EbookController {
    @Resource
    private EbookService ebookService;

    @RequestMapping("/list")
    public List<Ebook> list() {

        return ebookService.list();
    }
}
