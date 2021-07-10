package com.buaa.chat.module.message.builder;

import com.alibaba.fastjson.JSONObject;
import com.buaa.chat.module.message.ContentTypeEnum;
import com.buaa.chat.module.message.MessageTypeEnum;
import com.buaa.chat.module.message.factory.MessageContentFactory;
import com.buaa.chat.module.message.pojo.Message;

import java.util.Date;
import java.util.Objects;

public class NormalMessageBuilder implements MessageBuilder{

    private final Message message = new Message();

    @Override
    public void buildMessageType(JSONObject jsonObject){
        String messageType = jsonObject.getString("messageType");
        message.setMessageType(MessageTypeEnum.getEnum(messageType));
    }

    @Override
    public void buildTo(JSONObject jsonObject){
        String toId = jsonObject.getString("toId");
        message.setToId(toId);
    }

    @Override
    public void buildFrom(String fromId){
        message.setFromId(fromId);
    }

    @Override
    public void buildSendTime(){
        message.setSendTime(new Date());
    }

    @Override
    public void buildContent(JSONObject jsonObject){
        MessageContentFactory messageContentFactory = Objects.requireNonNull(ContentTypeEnum.getEnum(jsonObject.getString("contentType"))).getMessageContentFactory();
        String content = jsonObject.getString("content");
        message.setContent(messageContentFactory.newMessageContent(content));
    }

    @Override
    public Message retrieveMessage(){
        return message;
    }
}
