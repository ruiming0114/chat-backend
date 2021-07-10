package com.buaa.chat.util;

import com.buaa.chat.response.StatusEnum;
import com.buaa.chat.response.Result;

public class ResultUtil {
    public static Result success(Object object) {
        Result result = new Result();
        result.setCode(StatusEnum.SUCCESS.getCode());
        result.setMsg(StatusEnum.SUCCESS.getMsg());
        result.setData(object);
        return result;
    }

    public static Result success() {
        return success(null);
    }

    public static Result result(int code,String msg,Object object){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(object);
        return result;
    }

    public static Result result(int code,String msg){
        return result(code,msg,null);
    }
}