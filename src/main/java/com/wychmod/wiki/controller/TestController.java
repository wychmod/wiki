package com.wychmod.wiki.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/hello")
    public String hello() {
        return "hello world";
    }

    @PostMapping("/hello/post")
    public String helloPost(String name) {
        return "hello world! post"+name;
    }
}
