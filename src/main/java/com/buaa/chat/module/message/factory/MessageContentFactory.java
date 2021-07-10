package com.buaa.chat.module.message.factory;

import com.buaa.chat.module.message.content.MessageContent;

public interface MessageContentFactory {

    public abstract MessageContent newMessageContent(String content);
}
