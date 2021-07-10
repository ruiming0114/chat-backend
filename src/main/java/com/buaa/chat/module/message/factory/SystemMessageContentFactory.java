package com.buaa.chat.module.message.factory;

import com.buaa.chat.module.message.content.MessageContent;
import com.buaa.chat.module.message.content.SystemMessageContent;

public class SystemMessageContentFactory implements MessageContentFactory{

    @Override
    public MessageContent newMessageContent(String content) {
        return new SystemMessageContent(content);
    }
}
