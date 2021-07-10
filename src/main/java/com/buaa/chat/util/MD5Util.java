package com.buaa.chat.util;

import org.springframework.util.DigestUtils;

public class MD5Util {

    public static String toMD5(String password,String userId) {
        String base = password;
        if (userId.length()<4){
            base += userId;
        }
        else {
            base += userId.substring(0,3);
        }
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }

}
