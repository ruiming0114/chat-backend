package com.buaa.chat.module.contacts.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "contacts")
@ApiModel("联系人列表")
public class Contacts {

    @Id
    private String contactsId;

    @ApiModelProperty("用户MongoId")
    private String mongoId;

    @ApiModelProperty("联系人MongoId列表")
    private List<String> contactsList;

}
