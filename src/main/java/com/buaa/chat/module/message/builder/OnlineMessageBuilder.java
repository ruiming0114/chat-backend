package com.buaa.chat.module.message.builder;

import com.alibaba.fastjson.JSONObject;
import com.buaa.chat.module.message.ContentTypeEnum;
import com.buaa.chat.module.message.MessageTypeEnum;
import com.buaa.chat.module.message.pojo.Message;

import java.util.Date;

public class OnlineMessageBuilder implements MessageBuilder {

    private final Message message = new Message();

    @Override
    public void buildMessageType(JSONObject jsonObject) {
        message.setMessageType(MessageTypeEnum.ONLINE);
    }

    @Override
    public void buildTo(JSONObject jsonObject) {
        String toId = jsonObject.getString("toId");
        message.setToId(toId);
    }

    @Override
    public void buildFrom(String fromId) {
        message.setFromId(fromId);
    }

    @Override
    public void buildSendTime() {
        message.setSendTime(new Date());
    }

    @Override
    public void buildContent(JSONObject jsonObject) {
        message.setContent(ContentTypeEnum.SYSTEM.getMessageContentFactory().newMessageContent("用户上线"));
    }

    @Override
    public Message retrieveMessage() {
        return message;
    }
}
