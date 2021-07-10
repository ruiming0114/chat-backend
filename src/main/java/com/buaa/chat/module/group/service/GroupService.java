package com.buaa.chat.module.group.service;

import com.buaa.chat.module.group.dao.GroupDao;
import com.buaa.chat.module.user.dao.UserDao;
import com.buaa.chat.exception.MyException;
import com.buaa.chat.response.StatusEnum;
import com.buaa.chat.module.group.pojo.Group;
import com.buaa.chat.module.user.pojo.User;
import com.buaa.chat.module.user.service.UserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class GroupService {

    @Resource
    private GroupDao groupDao;

    @Resource
    private UserDao userDao;

    @Resource
    private UserInfoService userInfoService;

    public Map<String,Object> getGroupInfo(String groupId) throws MyException {
        Group group = groupDao.findGroupByGroupId(groupId);
        if (group == null){
            throw new MyException(StatusEnum.GROUP_NOT_EXIST);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("groupId",group.getGroupId());
        map.put("groupName",group.getGroupName());
        map.put("leader",userInfoService.getUserInfo(group.getLeaderId()));
        List<Map<String,Object>> list = new ArrayList<>();
        for(String mongoId:group.getMemberList()){
            if (!mongoId.equals(group.getLeaderId())){
                list.add(userInfoService.getUserInfo(mongoId));
            }
        }
        map.put("memberList",list);
        return map;
    }

    public List<Map<String,Object>> getGroupByUser(String mongoId){
        List<Group> list = groupDao.findGroupByUser(mongoId);
        List<Map<String,Object>> res = new ArrayList<>();
        for (Group group:list){
            Map<String,Object> map = new HashMap<>();
            map.put("groupId",group.getGroupId());
            map.put("groupName",group.getGroupName());
            map.put("isLeader",group.getLeaderId().equals(mongoId));
            res.add(map);
        }
        return res;
    }

    public void createGroup(String leaderId,String groupName) throws MyException {
        Group group = new Group();
        User leader = userDao.findUserByMongoId(leaderId);
        if (leader == null){
            throw new MyException(StatusEnum.USER_NOT_EXIST);
        }
        group.setLeaderId(leaderId);
        group.setGroupName(groupName);
        ArrayList<String> list = new ArrayList<>();
        list.add(leaderId);
        group.setMemberList(list);
        groupDao.saveGroup(group);
    }

    public void updateGroupName(String mongoId,String groupId,String groupName) throws MyException {
        Group group = groupDao.findGroupByGroupId(groupId);
        if (group == null){
            throw new MyException(StatusEnum.GROUP_NOT_EXIST);
        }
        if(!group.getLeaderId().equals(mongoId)){
            throw new MyException(StatusEnum.GROUP_AUTHORITY_DENY);
        }
        group.setGroupName(groupName);
        groupDao.updateGroup(group);
    }

    public void addGroupMember(String mongoId,String groupId,String memberId) throws MyException {
        Group group = groupDao.findGroupByGroupId(groupId);
        if (group == null){
            throw new MyException(StatusEnum.GROUP_NOT_EXIST);
        }
        if (!group.getLeaderId().equals(mongoId)){
            throw new MyException(StatusEnum.GROUP_AUTHORITY_DENY);
        }
        if (userDao.findUserByMongoId(memberId)==null){
            throw new MyException(StatusEnum.USER_NOT_EXIST);
        }
        if (group.getMemberList().contains(memberId)){
            throw new MyException(StatusEnum.GROUP_MEMBER_EXISTS_ALREADY);
        }
        group.getMemberList().add(memberId);
        groupDao.updateGroup(group);
    }

    public void delGroupMember(String mongoId,String groupId,String memberId) throws MyException {
        Group group = groupDao.findGroupByGroupId(groupId);
        if (group == null){
            throw new MyException(StatusEnum.GROUP_NOT_EXIST);
        }
        if (!group.getLeaderId().equals(mongoId)){
            throw new MyException(StatusEnum.GROUP_AUTHORITY_DENY);
        }
        if (userDao.findUserByMongoId(memberId)==null){
            throw new MyException(StatusEnum.USER_NOT_EXIST);
        }
        if (!group.getMemberList().contains(memberId)){
            throw new MyException(StatusEnum.GROUP_MEMBER_NO_EXISTS);
        }
        if (mongoId.equals(memberId)){
            throw new MyException(StatusEnum.GROUP_LEADER_QUIT_GROUP);
        }
        group.getMemberList().remove(memberId);
        groupDao.updateGroup(group);
    }

    public void delGroup(String mongoId,String groupId) throws MyException {
        Group group = groupDao.findGroupByGroupId(groupId);
        if (group == null){
            throw new MyException(StatusEnum.GROUP_NOT_EXIST);
        }
        if (!group.getLeaderId().equals(mongoId)){
            throw new MyException(StatusEnum.GROUP_AUTHORITY_DENY);
        }
        groupDao.deleteGroup(groupId);
    }

    public void quitGroup(String mongoId,String groupId) throws MyException {
        Group group = groupDao.findGroupByGroupId(groupId);
        if (group == null){
            throw new MyException(StatusEnum.GROUP_NOT_EXIST);
        }
        if (group.getLeaderId().equals(mongoId)){
            throw new MyException(StatusEnum.GROUP_LEADER_QUIT_GROUP);
        }
        if (userDao.findUserByMongoId(mongoId)==null){
            throw new MyException(StatusEnum.USER_NOT_EXIST);
        }
        if (!group.getMemberList().contains(mongoId)){
            throw new MyException(StatusEnum.GROUP_MEMBER_NO_EXISTS);
        }
        group.getMemberList().remove(mongoId);
        groupDao.updateGroup(group);
    }

}
