package com.buaa.chat.module.contacts.dao;

import com.buaa.chat.module.contacts.pojo.Contacts;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ContactsDao {

    @Resource
    private MongoTemplate mongoTemplate;

    public void saveContacts(Contacts contacts){
        mongoTemplate.save(contacts);
    }

    public Contacts findContacts(String mongoId){
        Query query = Query.query(Criteria.where("mongoId").is(mongoId));
        return mongoTemplate.findOne(query,Contacts.class);
    }

    public void updateContacts(Contacts contacts){
        Query query = Query.query(Criteria.where("mongoId").is(contacts.getMongoId()));
        Update update = new Update();
        update.set("contactsList",contacts.getContactsList());
        mongoTemplate.findAndModify(query,update,Contacts.class);
    }
}
