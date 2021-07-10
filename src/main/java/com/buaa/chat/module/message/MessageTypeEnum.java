package com.buaa.chat.module.message;

public enum MessageTypeEnum {
    SINGLE("single"),
    GROUP("group"),
    ONLINE("online"),
    OFFLINE("offline");

    private final String type;

    MessageTypeEnum(String type){
        this.type = type;
    }

    public static MessageTypeEnum getEnum(String type){
        for(MessageTypeEnum messageTypeEnum:MessageTypeEnum.values()){
            if(messageTypeEnum.type.equals(type)){
                return messageTypeEnum;
            }
        }
        return null;
    }

    public String getType() {
        return type;
    }
}
