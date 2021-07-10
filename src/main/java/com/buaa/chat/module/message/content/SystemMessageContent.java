package com.buaa.chat.module.message.content;

import com.buaa.chat.module.message.ContentTypeEnum;

public class SystemMessageContent extends MessageContent{

    public SystemMessageContent(String content){
        super();
        super.contentType = ContentTypeEnum.SYSTEM;
        super.content = content;
    }
}
