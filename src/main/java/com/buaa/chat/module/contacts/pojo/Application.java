package com.buaa.chat.module.contacts.pojo;

import com.buaa.chat.module.contacts.ApplicationStatusEnum;
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
@Document(collection = "application")
@ApiModel("好友申请")
public class Application {

    @Id
    private String applicationId;

    @ApiModelProperty("申请人MongoId")
    private String fromId;

    @ApiModelProperty("好友MongoId")
    private String toId;

    @ApiModelProperty("申请信息")
    private String content;

    @ApiModelProperty("申请时间")
    private Date applicationTime;

    @ApiModelProperty("申请状态")
    private ApplicationStatusEnum status;

    public String getApplicationTimeInFormat(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(applicationTime);
    }
}
