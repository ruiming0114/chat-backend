package com.buaa.chat.module.user.dao;

import com.buaa.chat.module.user.pojo.User;
import com.buaa.chat.util.RegExpUtil;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class UserDao {

    @Resource
    private MongoTemplate mongoTemplate;

    public void saveUser(User user){
        mongoTemplate.save(user);
    }

    public void updateUser(User user){
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(user.getMongoId()));
        Update update = new Update();
        update.set("username",user.getUsername());
        update.set("password",user.getPassword());
        update.set("imgPath",user.getImgPath());
        mongoTemplate.findAndModify(query,update,User.class);
    }

    public List<User> findUserByUsername(String username){
        Pattern pattern = Pattern.compile("^.*"+ RegExpUtil.escapeRegExpSpecialWord(username) +".*$",Pattern.CASE_INSENSITIVE);
        Query query = Query.query(Criteria.where("username").regex(pattern));
        return mongoTemplate.find(query,User.class);
    }

    public User findUserByUserId(String userId){
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoTemplate.findOne(query,User.class);
    }

    public User findUserByMongoId(String mongoId){
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(mongoId));
        return mongoTemplate.findOne(query,User.class);
    }

}
