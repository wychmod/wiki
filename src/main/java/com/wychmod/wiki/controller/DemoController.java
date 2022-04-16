package com.wychmod.wiki.controller;

import com.wychmod.wiki.domain.Demo;
import com.wychmod.wiki.service.DemoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/demo") //访问公共前缀
public class DemoController {
    @Resource
    private DemoService demoService;

    @RequestMapping("/list")
    public List<Demo> list() {

        return demoService.list();
    }
}
