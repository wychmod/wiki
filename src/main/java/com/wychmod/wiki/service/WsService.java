package com.wychmod.wiki.service;

import com.wychmod.wiki.websocket.WebSocketServer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class WsService {
    @Resource
    private WebSocketServer webSocketServe;

    @Async
    public void sendInfo(String message) {
        webSocketServe.sendInfo(message);
    }
}