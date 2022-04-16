package com.wychmod.wiki.controller;

import com.wychmod.wiki.domain.Test;
import com.wychmod.wiki.service.TestService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class TestController {
    @Resource
    private TestService testService;

    @RequestMapping("/hello")
    public String hello() {
        return "hello world";
    }

    @PostMapping("/hello/post")
    public String helloPost(String name) {
        return "hello world! post"+name;
    }

    @RequestMapping("/test/list")
    public List<Test> list() {

        return testService.list();
    }
}
