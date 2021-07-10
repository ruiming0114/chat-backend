package com.buaa.chat.module.message.dao;

import com.buaa.chat.module.message.pojo.MessageReadEntity;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MessageReadEntityDao {

    @Resource
    private MongoTemplate mongoTemplate;

    public MessageReadEntity findMessageReadEntityByUser(String myId){
        Query query = Query.query(Criteria.where("mongoId").is(myId));
        return mongoTemplate.findOne(query,MessageReadEntity.class);
    }

    public void saveMessageReadEntity(MessageReadEntity messageReadEntity){
        mongoTemplate.save(messageReadEntity);
    }

    public void updateMessageReadEntity(MessageReadEntity messageReadEntity){
        Query query = Query.query(Criteria.where("mongoId").is(messageReadEntity.getMongoId()));
        Update update = new Update();
        update.set("singleReadStatusMap",messageReadEntity.getSingleReadStatusMap());
        update.set("groupReadStatusMap",messageReadEntity.getGroupReadStatusMap());
        mongoTemplate.findAndModify(query,update,MessageReadEntity.class);
    }
}
