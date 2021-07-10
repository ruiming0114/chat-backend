package com.buaa.chat.module.contacts.dao;

import com.buaa.chat.module.contacts.ApplicationStatusEnum;
import com.buaa.chat.module.contacts.pojo.Application;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ApplicationDao {

    @Resource
    private MongoTemplate mongoTemplate;

    public void saveApplication(Application application){
        mongoTemplate.save(application);
    }

    public List<Application> getApplicationFromMe(String mongoId){
        Query query = Query.query(Criteria.where("fromId").is(mongoId));
        query.with(Sort.by(Sort.Order.desc("applicationTime")));
        return mongoTemplate.find(query,Application.class);
    }

    public List<Application> getApplicationToMe(String mongoId){
        Query query = Query.query(Criteria.where("toId").is(mongoId).and("status").is(ApplicationStatusEnum.TODO));
        query.with(Sort.by(Sort.Order.desc("applicationTime")));
        return mongoTemplate.find(query,Application.class);
    }

    public Application findApplicationById(String applicationId){
        Query query = Query.query(Criteria.where("_id").is(applicationId));
        return mongoTemplate.findOne(query,Application.class);
    }

    public Application findApplicationByToAndFrom(String fromId,String toId){
        Query query = Query.query(Criteria.where("fromId").is(fromId).and("toId").is(toId));
        return mongoTemplate.findOne(query,Application.class);
    }

    public void updateApplication(Application application){
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(application.getApplicationId()));
        Update update = new Update();
        update.set("content",application.getContent());
        update.set("applicationTime",application.getApplicationTime());
        update.set("status",application.getStatus());
        mongoTemplate.findAndModify(query,update,Application.class);
    }
}
