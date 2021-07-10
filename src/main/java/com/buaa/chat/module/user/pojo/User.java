package com.buaa.chat.module.user.pojo;

import com.alibaba.fastjson.JSONObject;
import com.buaa.chat.module.message.MessageTypeEnum;
import com.buaa.chat.module.message.service.MessageReadService;
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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
@ApiModel("用户")
@Component
public class User implements ChatEntity {

    @Transient
    private static MessageReadService messageReadService;

    @Resource
    public void setMessageReadService(MessageReadService messageReadService){
        User.messageReadService = messageReadService;
    }

    @Id
    @ApiModelProperty("Mongodb_id")
    private String mongoId;

    @ApiModelProperty("用户Id")
    private String userId;

    @ApiModelProperty("用户昵称")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("头像")
    private String imgPath;

    public User(String userId,String password){
        this.userId = userId;
        this.password = password;
    }

    @Override
    public void sendMessage(WebSocketServer webSocketServer, JSONObject jsonObject) throws IOException {
        if (!jsonObject.getString("fromId").equals(mongoId)){
            MessageTypeEnum messageType = MessageTypeEnum.getEnum(jsonObject.getString("messageType"));
            String toId = jsonObject.getString("toId");
            if (messageType==MessageTypeEnum.SINGLE){
                toId = jsonObject.getString("fromId");
            }
            messageReadService.addUnreadCnt(messageType,mongoId,toId);
        }
        if (WebSocketServer.getWebSocketMap().containsKey(mongoId) && !jsonObject.getString("fromId").equals(mongoId)){
            WebSocketServer.getWebSocketMap().get(mongoId).sendMessage(jsonObject.toJSONString());
        }
    }
}
