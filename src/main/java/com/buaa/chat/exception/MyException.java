package com.buaa.chat.exception;

import com.buaa.chat.response.StatusEnum;

public class MyException extends Exception{

    private final StatusEnum statusType;

    public MyException(StatusEnum statusType){
        this.statusType = statusType;
    }

    public StatusEnum getStatusType() {
        return statusType;
    }

    public int getCode(){
        return statusType.getCode();
    }

    public String getMsg(){
        return statusType.getMsg();
    }
}
