package com.buaa.chat.module.contacts.service;

import com.buaa.chat.module.contacts.ApplicationStatusEnum;
import com.buaa.chat.module.contacts.dao.ApplicationDao;
import com.buaa.chat.module.contacts.dao.ContactsDao;
import com.buaa.chat.module.user.dao.UserDao;
import com.buaa.chat.exception.MyException;
import com.buaa.chat.response.StatusEnum;
import com.buaa.chat.module.contacts.pojo.Application;
import com.buaa.chat.module.contacts.pojo.Contacts;
import com.buaa.chat.module.user.pojo.User;
import com.buaa.chat.websocket.WebSocketServer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ContactsService {

    @Resource
    private UserDao userDao;

    @Resource
    private ApplicationDao applicationDao;

    @Resource
    private ContactsDao contactsDao;

    public List<Map<String,Object>> findStrangerUserByUsername(String mongoId, String username){
        List<User> users = userDao.findUserByUsername(username);
        List<Map<String,Object>> res = new ArrayList<>();
        Contacts contacts = findAndCreate(mongoId);
        for(User user : users) {
            if (!contacts.getContactsList().contains(user.getMongoId()) && !user.getMongoId().equals(mongoId)){
                Map<String,Object> map = new HashMap<>();
                map.put("mongoId",user.getMongoId());
                map.put("username",user.getUsername());
                map.put("imgPath",user.getImgPath());
                res.add(map);
            }
        }
        return res;
    }

    public List<Map<String,Object>> getContacts(String mongoId){
        Contacts contacts = findAndCreate(mongoId);
        List<Map<String,Object>> res = new ArrayList<>();
        for (String mid:contacts.getContactsList()){
            User user = userDao.findUserByMongoId(mid);
            Map<String,Object> map = new HashMap<>();
            map.put("mongoId",user.getMongoId());
            map.put("username",user.getUsername());
            map.put("imgPath",user.getImgPath());
            map.put("isOnline",WebSocketServer.getWebSocketMap().containsKey(mid));
            res.add(map);
        }
        return res;
    }

    public void deleteFriend(String mongoId,String friendMongoId){
        Contacts myContacts = findAndCreate(mongoId);
        myContacts.getContactsList().remove(friendMongoId);
        Contacts friendContacts = findAndCreate(friendMongoId);
        friendContacts.getContactsList().remove(mongoId);
        contactsDao.updateContacts(myContacts);
        contactsDao.updateContacts(friendContacts);
    }

    public void sendApplication(String fromId,String toId,String content){
        Application application = applicationDao.findApplicationByToAndFrom(fromId,toId);
        if (application == null){
            application = new Application();
            application.setFromId(fromId);
            application.setToId(toId);
            application.setContent(content);
            application.setApplicationTime(new Date());
            application.setStatus(ApplicationStatusEnum.TODO);
            applicationDao.saveApplication(application);
        }
        else {
            application.setContent(content);
            application.setApplicationTime(new Date());
            application.setStatus(ApplicationStatusEnum.TODO);
            applicationDao.updateApplication(application);
        }
    }

    public List<Map<String,Object>> getApplicationFromMe(String mongoId){
        List<Application> apps = applicationDao.getApplicationFromMe(mongoId);
        List<Map<String,Object>> res = new ArrayList<>();
        for(Application application: apps){
            User user = userDao.findUserByMongoId(application.getToId());
            if (user!=null){
                Map<String,Object> map = new HashMap<>();
                map.put("applicationId",application.getApplicationId());
                map.put("toId",application.getToId());
                map.put("toUsername",user.getUsername());
                map.put("content",application.getContent());
                map.put("status",application.getStatus().getRes());
                map.put("applicationTime",application.getApplicationTimeInFormat());
                res.add(map);
            }
        }
        return res;
    }

    public List<Map<String,Object>> getApplicationToMe(String mongoId){
        List<Application> apps = applicationDao.getApplicationToMe(mongoId);
        List<Map<String,Object>> res = new ArrayList<>();
        for(Application application:apps){
            User user = userDao.findUserByMongoId(application.getFromId());
            if (user!=null){
                Map<String,Object> map = new HashMap<>();
                map.put("applicationId",application.getApplicationId());
                map.put("fromId",application.getFromId());
                map.put("fromUsername",user.getUsername());
                map.put("content",application.getContent());
                map.put("status",application.getStatus().getRes());
                map.put("applicationTime",application.getApplicationTimeInFormat());
                res.add(map);
            }
        }
        return res;
    }

    public void passApplication(String applicationId) throws MyException {
        Application application = applicationDao.findApplicationById(applicationId);
        if (application.getStatus()==ApplicationStatusEnum.TODO){
            application.setStatus(ApplicationStatusEnum.PASS);
            applicationDao.updateApplication(application);
            Contacts contactsTo = findAndCreate(application.getToId());
            if (!contactsTo.getContactsList().contains(application.getFromId())){
                contactsTo.getContactsList().add(application.getFromId());
                contactsDao.updateContacts(contactsTo);
            }
            Contacts contactsFrom = findAndCreate(application.getFromId());
            if (!contactsFrom.getContactsList().contains(application.getToId())){
                contactsFrom.getContactsList().add(application.getToId());
                contactsDao.updateContacts(contactsFrom);
            }
        }
        else {
            throw new MyException(StatusEnum.APPLICATION_DONE);
        }
    }

    public void rejectApplication(String applicationId) throws MyException {
        Application application = applicationDao.findApplicationById(applicationId);
        if (application.getStatus()==ApplicationStatusEnum.TODO){
            application.setStatus(ApplicationStatusEnum.REJECT);
            System.out.println(application);
            applicationDao.updateApplication(application);
        }
        else {
            throw new MyException(StatusEnum.APPLICATION_DONE);
        }
    }

    private Contacts findAndCreate(String mongoId){
        Contacts contacts = contactsDao.findContacts(mongoId);
        if (contacts == null){
            contacts = new Contacts();
            contacts.setMongoId(mongoId);
            contacts.setContactsList(new ArrayList<>());
            contactsDao.saveContacts(contacts);
        }
        return contacts;
    }
}
