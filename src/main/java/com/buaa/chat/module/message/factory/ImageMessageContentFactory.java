package com.buaa.chat.module.message.factory;

import com.buaa.chat.module.message.content.ImageMessageContent;
import com.buaa.chat.module.message.content.MessageContent;

public class ImageMessageContentFactory implements MessageContentFactory{

    @Override
    public MessageContent newMessageContent(String content) {
        return new ImageMessageContent(content);
    }
}
