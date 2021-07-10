package com.buaa.chat.module.group.pojo;

import com.alibaba.fastjson.JSONObject;
import com.buaa.chat.module.user.dao.UserDao;
import com.buaa.chat.module.user.pojo.User;
import com.buaa.chat.module.chat.ChatEntity;
import com.buaa.chat.websocket.WebSocketServer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("群")
@Document("group")
@Component
@SuppressWarnings("all")
public class Group implements ChatEntity {

    @Transient
    private static UserDao userDao;

    @Resource
    public void setUserDao(UserDao userDao){
        Group.userDao = userDao;
    }

    @Id
    private String groupId;

    @ApiModelProperty("群聊名称")
    private String groupName;

    @ApiModelProperty("组长Id")
    private String leaderId;

    @ApiModelProperty("群成员列表")
    private List<String> memberList;

    @Override
    public void sendMessage(WebSocketServer webSocketServer, JSONObject jsonObject) throws IOException {
        ListIterator<String> iterator = memberList.listIterator();
        while (iterator.hasNext()){
            User user = userDao.findUserByMongoId(iterator.next());
            user.sendMessage(webSocketServer, jsonObject);
        }
    }
}
