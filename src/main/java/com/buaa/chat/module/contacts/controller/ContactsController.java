package com.buaa.chat.module.contacts.controller;

import com.buaa.chat.exception.MyException;
import com.buaa.chat.module.contacts.service.ContactsService;
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
@Api("联系人")
public class ContactsController {

    @Resource
    private ContactsService contactsService;

    @GetMapping("/findStrangerByUsername")
    @ApiOperation("根据用户名搜索陌生用户")
    public Result findStrangerByUsername(@RequestParam("username") @ApiParam("需要搜索的用户名") String username, ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        String mongoId = JWTUtil.getMongoId(token);
        return ResultUtil.success(contactsService.findStrangerUserByUsername(mongoId, username));
    }

    @GetMapping("/getContacts")
    @ApiOperation("获取好友列表")
    public Result getContacts(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        String mongoId = JWTUtil.getMongoId(token);
        return ResultUtil.success(contactsService.getContacts(mongoId));
    }

    @PostMapping("/sendApplication")
    @ApiOperation("发送好友申请")
    public Result sendApplication(@RequestParam("toId") @ApiParam("好友的MongoId") String toId, @RequestParam("content") @ApiParam("申请信息") String content, ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        String mongoId = JWTUtil.getMongoId(token);
        contactsService.sendApplication(mongoId, toId, content);
        return ResultUtil.success();
    }

    @GetMapping("/getApplicationToMe")
    @ApiOperation("获取发送给我的好友申请(未处理的)")
    public Result getApplicationToMe(ServletRequest request){
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        String mongoId = JWTUtil.getMongoId(token);
        return ResultUtil.success(contactsService.getApplicationToMe(mongoId));
    }

    @GetMapping("/getApplicationFromMe")
    @ApiOperation("获取我发出的好友申请(全部，按时间倒序)")
    public Result getApplicationFromMe(ServletRequest request){
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        String mongoId = JWTUtil.getMongoId(token);
        return ResultUtil.success(contactsService.getApplicationFromMe(mongoId));
    }

    @PostMapping("/passApplication")
    @ApiOperation("同意好友申请")
    public Result passApplication(@RequestParam("applicationId") @ApiParam("申请Id") String applicationId){
        try{
            contactsService.passApplication(applicationId);
            return ResultUtil.success();
        }catch (MyException e){
            return ResultUtil.result(e.getCode(),e.getMsg());
        }
    }

    @PostMapping("/rejectApplication")
    @ApiOperation("拒绝好友申请")
    public Result rejectApplication(@RequestParam("applicationId") @ApiParam("申请Id") String applicationId){
        try{
            contactsService.rejectApplication(applicationId);
            return ResultUtil.success();
        }catch (MyException e){
            return ResultUtil.result(e.getCode(),e.getMsg());
        }
    }

    @PostMapping("/deleteFriend")
    @ApiOperation("删除好友")
    public Result deleteFriend(@RequestParam("friendId") @ApiParam("好友MongoId") String friendId, ServletRequest request){
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        String mongoId = JWTUtil.getMongoId(token);
        contactsService.deleteFriend(mongoId,friendId);
        return ResultUtil.success();
    }
}
