package com.buaa.chat.module.user.service;

import com.buaa.chat.module.user.dao.UserDao;
import com.buaa.chat.exception.MyException;
import com.buaa.chat.response.StatusEnum;
import com.buaa.chat.module.user.pojo.User;
import com.buaa.chat.util.MD5Util;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserInfoService {

    @Resource
    private UserDao userDao;

    public Map<String,Object> getUserInfo(String mongoId) throws MyException {
        User user = userDao.findUserByMongoId(mongoId);
        if (user == null) {
            throw new MyException(StatusEnum.USER_NOT_EXIST);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mongoId",user.getMongoId());
        map.put("username",user.getUsername());
        map.put("imgPath",user.getImgPath());
        return map;
    }

    public User findUserByUserId(String userId){
        return userDao.findUserByUserId(userId);
    }

    public void updateUserName(String mongoId,String username) throws MyException {
        User user = userDao.findUserByMongoId(mongoId);
        if (user == null){
            throw new MyException(StatusEnum.USER_NOT_EXIST);
        }
        user.setUsername(username);
        userDao.updateUser(user);
    }

    public void updatePassword(String mongoId,String password) throws MyException {
        User user = userDao.findUserByMongoId(mongoId);
        if (user == null){
            throw new MyException(StatusEnum.USER_NOT_EXIST);
        }
        String md5pwd = MD5Util.toMD5(password,user.getUserId());
        user.setPassword(md5pwd);
        userDao.updateUser(user);
    }

    public void updateImgPath(String mongoId,String imgPath) throws MyException {
        User user = userDao.findUserByMongoId(mongoId);
        if (user == null){
            throw new MyException(StatusEnum.USER_NOT_EXIST);
        }
        user.setImgPath(imgPath);
        userDao.updateUser(user);
    }
}
