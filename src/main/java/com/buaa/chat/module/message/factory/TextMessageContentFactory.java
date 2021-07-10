package com.buaa.chat.module.message.factory;

import com.buaa.chat.module.message.content.MessageContent;
import com.buaa.chat.module.message.content.TextMessageContent;

public class TextMessageContentFactory implements MessageContentFactory{

    @Override
    public MessageContent newMessageContent(String content){
        return new TextMessageContent(content);
    }
}
