package com.buaa.chat.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("响应体封装")
public class Result {

    @ApiModelProperty("状态码,1xxx成功,2xxx失败,3xxx未授权")
    private int code;

    @ApiModelProperty("消息")
    private String msg;

    @ApiModelProperty("数据")
    private Object data;
}
