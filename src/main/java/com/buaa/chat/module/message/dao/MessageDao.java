package com.buaa.chat.module.message.dao;

import com.buaa.chat.module.group.dao.GroupDao;
import com.buaa.chat.module.message.MessageTypeEnum;
import com.buaa.chat.module.group.pojo.Group;
import com.buaa.chat.module.message.pojo.Message;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class MessageDao {

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private GroupDao groupDao;

    public void saveMessage(Message message) {
        mongoTemplate.save(message);
    }

    public List<Message> findMessageByUser(String myId, String mongoId, int pageNum, int pageSize) {
        Query query = Query.query(Criteria.where("messageType").is(MessageTypeEnum.SINGLE)
                .orOperator(Criteria.where("fromId").is(mongoId).and("toId").is(myId),
                        Criteria.where("fromId").is(myId).and("toId").is(mongoId)));
        query.with(Sort.by(Sort.Order.desc("sendTime"))).skip((long) (pageNum - 1) *pageSize).limit(pageSize);
        return mongoTemplate.find(query,Message.class);
    }

    public List<Message> findMessageByGroup(String groupId, int pageNum, int pageSize) {
        Query query = Query.query(Criteria.where("messageType").is(MessageTypeEnum.GROUP).and("toId").is(groupId));
        query.with(Sort.by(Sort.Order.desc("sendTime"))).skip((long) (pageNum - 1) *pageSize).limit(pageSize);
        return mongoTemplate.find(query,Message.class);
    }

    public List<Message> findMessageList(String mongoId){
        List<Group> groups = groupDao.findGroupByUser(mongoId);
        List<String> ids = new ArrayList<>();
        for(Group group:groups){
            ids.add(group.getGroupId());
        }
        Criteria criteria = new Criteria();
        criteria.orOperator(
                Criteria.where("messageType").is(MessageTypeEnum.SINGLE)
                        .orOperator(Criteria.where("toId").is(mongoId),
                                Criteria.where("fromId").is(mongoId)),
                Criteria.where("messageType").is(MessageTypeEnum.GROUP)
                        .and("toId").in(ids));
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.project("messageType","toId","fromId","sendTime","_id","content")
                        .and(ConditionalOperators.when(new Criteria().andOperator(Criteria.where("messageType").is(MessageTypeEnum.SINGLE),Criteria.where("toId").is(mongoId)))
                                .thenValueOf("fromId").otherwiseValueOf("toId")).as("entityId"),
                Aggregation.sort(Sort.by(Sort.Order.desc("sendTime"))),
                Aggregation.group("messageType","entityId")
                        .first("messageType").as("messageType")
                        .first("toId").as("toId")
                        .first("fromId").as("fromId")
                        .first("content").as("content")
                        .first("sendTime").as("sendTime"));
        return mongoTemplate.aggregate(aggregation,"message",Message.class).getMappedResults();
    }
}
