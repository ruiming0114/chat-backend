package com.buaa.chat.module.message.content;

import com.buaa.chat.module.message.ContentTypeEnum;

public class TextMessageContent extends MessageContent {

    public TextMessageContent(String content){
        super();
        super.contentType = ContentTypeEnum.TEXT;
        super.content = content;
    }


}
