package com.buaa.chat.module.message.content;

import com.buaa.chat.module.message.ContentTypeEnum;

public class ImageMessageContent extends MessageContent {

    public ImageMessageContent(String content){
        super();
        super.contentType = ContentTypeEnum.IMAGE;
        super.content = content;
    }
}
