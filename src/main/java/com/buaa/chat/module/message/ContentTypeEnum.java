package com.buaa.chat.module.message;

import com.buaa.chat.module.message.factory.ImageMessageContentFactory;
import com.buaa.chat.module.message.factory.MessageContentFactory;
import com.buaa.chat.module.message.factory.SystemMessageContentFactory;
import com.buaa.chat.module.message.factory.TextMessageContentFactory;

public enum ContentTypeEnum {
    TEXT("text", new TextMessageContentFactory()),
    IMAGE("image", new ImageMessageContentFactory()),
    SYSTEM("system",new SystemMessageContentFactory());

    private final String type;
    private final MessageContentFactory messageContentFactory;

    ContentTypeEnum(String type,MessageContentFactory messageContentFactory){
        this.type = type;
        this.messageContentFactory = messageContentFactory;
    }

    public static ContentTypeEnum getEnum(String type){
        for(ContentTypeEnum contentTypeEnum:ContentTypeEnum.values()){
            if(contentTypeEnum.type.equals(type)){
                return contentTypeEnum;
            }
        }
        return null;
    }

    public String getType() {
        return type;
    }

    public MessageContentFactory getMessageContentFactory() {
        return messageContentFactory;
    }
}
