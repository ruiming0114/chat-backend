package com.buaa.chat.module.chat;

import com.alibaba.fastjson.JSONObject;
import com.buaa.chat.websocket.WebSocketServer;

import java.io.IOException;

public interface ChatEntity {

    public abstract void sendMessage(WebSocketServer webSocketServer, JSONObject jsonObject) throws IOException;
}
