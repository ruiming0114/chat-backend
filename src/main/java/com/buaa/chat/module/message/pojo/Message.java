package com.buaa.chat.module.message.pojo;

import com.buaa.chat.module.message.MessageTypeEnum;
import com.buaa.chat.module.message.content.MessageContent;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("消息")
@Document(collection = "message")
public class Message {

    @Id
    private String msgId;

    @ApiModelProperty("消息类型")
    private MessageTypeEnum messageType;

    @ApiModelProperty("发送者")
    private String fromId;

    @ApiModelProperty("接收者")
    private String toId;

    @ApiModelProperty("发送时间")
    private Date sendTime;

    @ApiModelProperty("内容")
    private MessageContent content;

    public String getSendTimeInFormat(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(sendTime);
    }

}
