package com.buaa.chat.auth.service;

import com.buaa.chat.module.user.dao.UserDao;
import com.buaa.chat.exception.MyException;
import com.buaa.chat.response.StatusEnum;
import com.buaa.chat.module.user.pojo.User;
import com.buaa.chat.util.JWTUtil;
import com.buaa.chat.util.MD5Util;
import com.buaa.chat.util.RedisUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RegisterAndLoginService {

    @Resource
    private UserDao userDao;

    @Resource
    private RedisUtil redisUtil;

    @Value("${chat.image.default_user_image_path}")
    private String userImgPath;

    private static final int token_expire_time= 3600;

    public boolean hasUser(String userId){
        return userDao.findUserByUserId(userId) != null;
    }

    public void register(String userId,String password) throws MyException {
        if (hasUser(userId)){
            throw new MyException(StatusEnum.DUPLICATED_USERID);
        }
        String md5Pwd = MD5Util.toMD5(password,userId);
        User user = new User(userId,md5Pwd);
        userDao.saveUser(user);
        user = userDao.findUserByUserId(userId);
        user.setUsername("用户"+user.getMongoId().substring(16));
        user.setImgPath(userImgPath);
        userDao.updateUser(user);
    }

    public String login(String userId, String password) throws MyException {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(userId, MD5Util.toMD5(password,userId));
        try {
            subject.login(usernamePasswordToken);
            User user = userDao.findUserByUserId(userId);
            long currentTimeMillis = System.currentTimeMillis();
            String token= JWTUtil.createToken(user.getMongoId(),currentTimeMillis);
            redisUtil.set("token_"+user.getMongoId(),currentTimeMillis,token_expire_time);
            return token;
        }catch (UnknownAccountException | IncorrectCredentialsException e){
            throw new MyException(StatusEnum.WRONG_USERID_PASSWORD);
        }
    }
}
