package com.buaa.chat.module.message.service;

import com.buaa.chat.module.message.dao.MessageReadEntityDao;
import com.buaa.chat.module.message.MessageTypeEnum;
import com.buaa.chat.module.message.pojo.MessageReadEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

@Service
public class MessageReadService {

    @Resource
    private MessageReadEntityDao messageReadEntityDao;

    public void addUnreadCnt(MessageTypeEnum messageType,String myId,String toId){
        MessageReadEntity messageReadEntity = findAndCreate(myId);
        if (messageType==MessageTypeEnum.SINGLE){
            int cnt = (int) messageReadEntity.getSingleReadStatusMap().getOrDefault(toId,0);
            cnt++;
            messageReadEntity.getSingleReadStatusMap().put(toId,cnt);
        }
        if (messageType==MessageTypeEnum.GROUP){
            int cnt = (int) messageReadEntity.getGroupReadStatusMap().getOrDefault(toId,0);
            cnt++;
            messageReadEntity.getGroupReadStatusMap().put(toId,cnt);
        }
        messageReadEntityDao.updateMessageReadEntity(messageReadEntity);
    }

    public void zeroUnreadCnt(MessageTypeEnum messageType,String myId,String toId){
        MessageReadEntity messageReadEntity = findAndCreate(myId);
        if (messageType==MessageTypeEnum.SINGLE){
            messageReadEntity.getSingleReadStatusMap().put(toId,0);
        }
        if (messageType==MessageTypeEnum.GROUP){
            messageReadEntity.getGroupReadStatusMap().put(toId,0);
        }
        messageReadEntityDao.updateMessageReadEntity(messageReadEntity);
    }

    public int getUnreadCnt(MessageTypeEnum messageType,String myId,String toId){
        MessageReadEntity messageReadEntity = findAndCreate(myId);
        if (messageType==MessageTypeEnum.SINGLE){
            return (int) messageReadEntity.getSingleReadStatusMap().getOrDefault(toId,0);
        }
        if (messageType==MessageTypeEnum.GROUP){
            return (int) messageReadEntity.getGroupReadStatusMap().getOrDefault(toId,0);
        }
        return 0;
    }

    private MessageReadEntity findAndCreate(String mongoId){
        MessageReadEntity messageReadEntity = messageReadEntityDao.findMessageReadEntityByUser(mongoId);
        if (messageReadEntity == null){
            messageReadEntity = new MessageReadEntity();
            messageReadEntity.setMongoId(mongoId);
            messageReadEntity.setSingleReadStatusMap(new HashMap<>());
            messageReadEntity.setGroupReadStatusMap(new HashMap<>());
            messageReadEntityDao.saveMessageReadEntity(messageReadEntity);
        }
        return messageReadEntity;
    }
}
