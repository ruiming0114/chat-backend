package com.buaa.chat.response;

public enum StatusEnum {
    SUCCESS(1000,"成功"),

    FAIL(2000,"失败"),
    DUPLICATED_USERID(2001,"用户Id重复"),
    WRONG_USERID_PASSWORD(2002,"用户Id或密码错误"),
    USER_NOT_EXIST(2003,"用户不存在"),
    IMAGE_UPLOAD_ERROR(2004,"图片上传错误"),
    APPLICATION_DONE(2005, "好友申请已被处理"),
    GROUP_NOT_EXIST(2006,"群聊不存在"),
    GROUP_AUTHORITY_DENY(2007,"没有群聊管理权限"),
    GROUP_LEADER_QUIT_GROUP(2008,"群聊组长不能退出群聊"),
    GROUP_MEMBER_EXISTS_ALREADY(2009,"用户已在群聊中"),
    GROUP_MEMBER_NO_EXISTS(2010,"用户不在群聊中"),

    UNAUTHORIZED(3001,"用户登录信息有误");

    private final int code;
    private final String msg;

    StatusEnum(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode(){
        return this.code;
    }

    public String getMsg(){
        return this.msg;
    }
}
