package com.buaa.chat.module.group.dao;

import com.buaa.chat.module.group.pojo.Group;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class GroupDao {

    @Resource
    private MongoTemplate mongoTemplate;

    public void saveGroup(Group group){
        mongoTemplate.save(group);
    }

    public void updateGroup(Group group){
        Query query = Query.query(Criteria.where("_id").is(group.getGroupId()));
        Update update = new Update();
        update.set("leaderId",group.getLeaderId());
        update.set("groupName",group.getGroupName());
        update.set("memberList",group.getMemberList());
        mongoTemplate.findAndModify(query,update,Group.class);
    }

    public Group findGroupByGroupId(String groupId){
        Query query = Query.query(Criteria.where("_id").is(groupId));
        return mongoTemplate.findOne(query,Group.class);
    }

    public List<Group> findGroupByUser(String mongoId){
        Query query = Query.query(Criteria.where("memberList").is(mongoId));
        return mongoTemplate.find(query,Group.class);
    }

    public void deleteGroup(String groupId){
        Query query = Query.query(Criteria.where("_id").is(groupId));
        mongoTemplate.remove(query,Group.class);
    }
}
