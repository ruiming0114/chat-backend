package com.buaa.chat.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.buaa.chat.module.contacts.dao.ContactsDao;
import com.buaa.chat.exception.MyException;
import com.buaa.chat.module.message.builder.NormalMessageBuilder;
import com.buaa.chat.module.message.director.MessageDirector;
import com.buaa.chat.module.message.builder.OfflineMessageBuilder;
import com.buaa.chat.module.message.builder.OnlineMessageBuilder;
import com.buaa.chat.module.contacts.pojo.Contacts;
import com.buaa.chat.module.message.pojo.Message;
import com.buaa.chat.module.message.service.MessageService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/websocket/{userId}")
@Component
public class WebSocketServer {

    private static MessageService messageService;

    private static ContactsDao contactsDao;

    @Resource
    public void setMessageService(MessageService messageService){
        WebSocketServer.messageService = messageService;
    }

    @Resource
    public void setContactsDao(ContactsDao contactsDao){
        WebSocketServer.contactsDao = contactsDao;
    }

    private static final ConcurrentHashMap<String,WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    private Session session;
    private String userId;

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId){
        this.session = session;
        this.userId = userId;
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            webSocketMap.put(userId,this);
        }
        else{
            webSocketMap.put(userId,this);
        }
        Contacts contacts = contactsDao.findContacts(userId);
        for(String uid : contacts.getContactsList()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("messageType","online");
            jsonObject.put("toId",uid);
            jsonObject.put("fromId",this.userId);
            OnlineMessageBuilder messageBuilder = new OnlineMessageBuilder();
            MessageDirector messageDirector = new MessageDirector(messageBuilder);
            messageDirector.construct(jsonObject,this.userId);
            Message msg = messageBuilder.retrieveMessage();
            try{
                messageService.sendMessage(this,msg);
            }catch (IOException | MyException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClose
    public void onClose(){
        webSocketMap.remove(userId);
        Contacts contacts = contactsDao.findContacts(userId);
        for(String uid : contacts.getContactsList()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("messageType","offline");
            jsonObject.put("toId",uid);
            jsonObject.put("fromId",this.userId);
            OfflineMessageBuilder messageBuilder = new OfflineMessageBuilder();
            MessageDirector messageDirector = new MessageDirector(messageBuilder);
            messageDirector.construct(jsonObject,this.userId);
            Message msg = messageBuilder.retrieveMessage();
            try{
                messageService.sendMessage(this,msg);
            }catch (IOException | MyException e) {
                e.printStackTrace();
            }
        }
    }

    @OnMessage
    public void onMessage(String message) throws MyException{
        JSONObject jsonObject = JSON.parseObject(message);
        NormalMessageBuilder messageBuilder = new NormalMessageBuilder();
        MessageDirector messageDirector = new MessageDirector(messageBuilder);
        messageDirector.construct(jsonObject,this.userId);
        Message msg = messageBuilder.retrieveMessage();
        try{
            messageService.sendMessage(this,msg);
            messageService.saveMessage(msg);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    @OnError
    public void onError(Throwable error){
        error.printStackTrace();
    }

    public static ConcurrentHashMap<String, WebSocketServer> getWebSocketMap() {
        return webSocketMap;
    }
}
