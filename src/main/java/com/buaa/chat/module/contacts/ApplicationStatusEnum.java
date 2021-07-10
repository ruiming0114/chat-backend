package com.buaa.chat.module.contacts;

public enum ApplicationStatusEnum {
    PASS(1,"通过"),REJECT(-1,"拒绝"),TODO(0,"待处理");

    private final int code;
    private final String res;

    ApplicationStatusEnum(int code,String res){
        this.code = code;
        this.res = res;
    }

    public static ApplicationStatusEnum valueOf(int code){
        for(ApplicationStatusEnum statusEnum:ApplicationStatusEnum.values()){
            if(statusEnum.code == code){
                return statusEnum;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public String getRes() {
        return res;
    }
}
