package com.buaa.chat.module.message.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("read")
public class MessageReadEntity {

    @Id
    private String entityId;

    @ApiModelProperty("用户Id")
    private String mongoId;

    @ApiModelProperty("对每个人的消息未读数量")
    private Map<String,Object> singleReadStatusMap = new HashMap<>();

    @ApiModelProperty("对群聊消息未读数量")
    private Map<String,Object> groupReadStatusMap = new HashMap<>();
}
