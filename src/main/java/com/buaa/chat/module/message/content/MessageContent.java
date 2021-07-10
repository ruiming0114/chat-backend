package com.buaa.chat.module.message.content;

import com.buaa.chat.module.message.ContentTypeEnum;

public abstract class MessageContent {

    protected ContentTypeEnum contentType;
    protected String content;

    public ContentTypeEnum getContentType() {
        return contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
