package com.buaa.chat.module.message.service;

import com.alibaba.fastjson.JSONObject;
import com.buaa.chat.module.group.dao.GroupDao;
import com.buaa.chat.module.message.dao.MessageDao;
import com.buaa.chat.module.user.dao.UserDao;
import com.buaa.chat.exception.MyException;
import com.buaa.chat.response.StatusEnum;
import com.buaa.chat.module.message.MessageTypeEnum;
import com.buaa.chat.module.group.pojo.Group;
import com.buaa.chat.module.message.pojo.Message;
import com.buaa.chat.module.user.pojo.User;
import com.buaa.chat.module.chat.ChatEntity;
import com.buaa.chat.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MessageService {

    @Resource
    private MessageDao messageDao;

    @Resource
    private UserDao userDao;

    @Resource
    private GroupDao groupDao;

    @Resource
    private MessageReadService messageReadService;

    @Value("${chat.image.default_group_image_path}")
    private String groupImgPath;

    public void saveMessage(Message message){
        messageDao.saveMessage(message);
    }

    public void sendMessage(WebSocketServer webSocketServer,Message message) throws MyException, IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("messageType",message.getMessageType().getType());
        jsonObject.put("fromId",message.getFromId());
        jsonObject.put("toId",message.getToId());
        jsonObject.put("sendTime",message.getSendTimeInFormat());
        jsonObject.put("contentType",message.getContent().getContentType().getType());
        jsonObject.put("content",message.getContent().getContent());
        ChatEntity chatEntity = getChatEntity(message.getMessageType(),message.getToId());
        chatEntity.sendMessage(webSocketServer,jsonObject);
    }

    public List<Map<String,Object>> getSingleMessageRecord(String myId,String mongoId,int pageNum,int pageSize){
        List<Message> messages = messageDao.findMessageByUser(myId,mongoId,pageNum,pageSize);
        List<Map<String,Object>> res = new ArrayList<>();
        for(Message message : messages){
            res.add(getMessageInfo(message));
        }
        return res;
    }

    public List<Map<String,Object>> getGroupMessageRecord(String groupId,int pageNum,int pageSize){
        List<Message> messages = messageDao.findMessageByGroup(groupId,pageNum,pageSize);
        List<Map<String,Object>> res = new ArrayList<>();
        for(Message message : messages){
            res.add(getMessageInfo(message));
        }
        return res;
    }

    public List<Map<String,Object>> getMessageList(String mongoId){
        List<Message> messages = messageDao.findMessageList(mongoId);
        List<Map<String,Object>> res = new ArrayList<>();
        for(Message message:messages){
            Map<String,Object> map = getMessageInfo(message);
            String toId = message.getToId();
            if (message.getMessageType() == MessageTypeEnum.SINGLE){
                toId = message.getToId().equals(mongoId)?message.getFromId():message.getToId();
            }
            map.put("unreadCnt",messageReadService.getUnreadCnt(message.getMessageType(),mongoId,toId));
            res.add(map);
        }
        res.sort(new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    return  (sdf.parse((String)o2.get("sendTime")).compareTo(sdf.parse((String) o1.get("sendTime"))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return -1;
            }
        });
        return res;
    }

    private ChatEntity getChatEntity(MessageTypeEnum messageType,String id) throws MyException {
        if (messageType == MessageTypeEnum.SINGLE || messageType == MessageTypeEnum.ONLINE || messageType == MessageTypeEnum.OFFLINE){
            ChatEntity user = userDao.findUserByMongoId(id);
            if (user == null){
                throw new MyException(StatusEnum.USER_NOT_EXIST);
            }
            return user;
        }
        if (messageType == MessageTypeEnum.GROUP){
            ChatEntity group = groupDao.findGroupByGroupId(id);
            if (group == null){
                throw new MyException(StatusEnum.GROUP_NOT_EXIST);
            }
            return group;
        }
        throw new MyException(StatusEnum.FAIL);
    }

    private Map<String,Object> getMessageInfo(Message message){
        Map<String,Object> map = new HashMap<>();
        map.put("messageType",message.getMessageType().getType());
        User fromUser = userDao.findUserByMongoId(message.getFromId());
        map.put("fromId",fromUser.getMongoId());
        map.put("fromUsername",fromUser.getUsername());
        map.put("fromUserImg",fromUser.getImgPath());
        map.put("toId",message.getToId());
        if (message.getMessageType()==MessageTypeEnum.SINGLE){
            User toUser = userDao.findUserByMongoId(message.getToId());
            map.put("toName",toUser.getUsername());
            map.put("toImg",toUser.getImgPath());
        }
        else {
            Group group = groupDao.findGroupByGroupId(message.getToId());
            map.put("toName",group.getGroupName());
            map.put("toImg",groupImgPath);
        }
        map.put("sendTime",message.getSendTimeInFormat());
        map.put("contentType",message.getContent().getContentType().getType());
        map.put("content",message.getContent().getContent());
        return map;
    }
}
