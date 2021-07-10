package com.buaa.chat.module.message.director;

import com.alibaba.fastjson.JSONObject;
import com.buaa.chat.module.message.builder.MessageBuilder;

public class MessageDirector {
    private final MessageBuilder messageBuilder;

    public MessageDirector(MessageBuilder messageBuilder){
        this.messageBuilder = messageBuilder;
    }

    public void construct(JSONObject jsonObject,String mongoId){
        messageBuilder.buildMessageType(jsonObject);
        messageBuilder.buildTo(jsonObject);
        messageBuilder.buildFrom(mongoId);
        messageBuilder.buildSendTime();
        messageBuilder.buildContent(jsonObject);
    }

}
