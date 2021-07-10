package com.buaa.chat.module.message.controller;

import com.buaa.chat.module.message.MessageTypeEnum;
import com.buaa.chat.module.message.service.MessageReadService;
import com.buaa.chat.module.message.service.MessageService;
import com.buaa.chat.util.JWTUtil;
import com.buaa.chat.response.Result;
import com.buaa.chat.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

@RestController
@Api("消息Api")
public class MessageController {

    @Resource
    private MessageService messageService;

    @Resource
    private MessageReadService messageReadService;

    @GetMapping("/getSingleMessageRecord")
    @ApiOperation("获取一对一聊天记录")
    public Result getSingleMessageRecord(@RequestParam("mongoId") @ApiParam("对方的MongoId") String mongoId,
                                         @RequestParam("pageNum") @ApiParam("页码，从1开始") int pageNum,
                                         @RequestParam("pageSize") @ApiParam("每页几条") int pageSize,
                                         ServletRequest request){
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        String myId = JWTUtil.getMongoId(token);
        return ResultUtil.success(messageService.getSingleMessageRecord(myId,mongoId,pageNum,pageSize));
    }

    @GetMapping("/getGroupMessageRecord")
    @ApiOperation("获取群聊天记录")
    public Result getGroupMessageRecord(@RequestParam("GroupId") @ApiParam("群oId") String groupId,
                                         @RequestParam("pageNum") @ApiParam("页码，从1开始") int pageNum,
                                         @RequestParam("pageSize") @ApiParam("每页几条") int pageSize){
        return ResultUtil.success(messageService.getGroupMessageRecord(groupId,pageNum,pageSize));
    }

    @GetMapping("/getMessageList")
    @ApiOperation("获取聊天列表")
    public Result getMessageList(ServletRequest request){
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        String myId = JWTUtil.getMongoId(token);
        return ResultUtil.success(messageService.getMessageList(myId));
    }

    @PostMapping("/zeroUnreadCnt")
    @ApiOperation("清零未读消息数")
    public Result zeroUnreadCnt(@RequestParam("messageType") @ApiParam("group或single") String messageType,
                                @RequestParam("toId") @ApiParam("对方的mongoId或groupId") String toId,
                                ServletRequest request){
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        String myId = JWTUtil.getMongoId(token);
        messageReadService.zeroUnreadCnt(MessageTypeEnum.getEnum(messageType),myId,toId);
        return ResultUtil.success();
    }

}
