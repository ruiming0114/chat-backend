package com.buaa.chat.module.message.builder;

import com.alibaba.fastjson.JSONObject;
import com.buaa.chat.module.message.pojo.Message;

public interface MessageBuilder {

    public abstract void buildMessageType(JSONObject jsonObject);

    public abstract void buildTo(JSONObject jsonObject);

    public abstract void buildFrom(String fromId);

    public abstract void buildSendTime();

    public abstract void buildContent(JSONObject jsonObject);

    public abstract Message retrieveMessage();
}
