package com.buaa.chat.module.group.controller;

import com.buaa.chat.exception.MyException;
import com.buaa.chat.response.StatusEnum;
import com.buaa.chat.module.group.service.GroupService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api("群聊Api")
public class GroupController {

    @Resource
    private GroupService groupService;

    @PostMapping("/createGroup")
    @ApiOperation("创建群聊")
    public Result createGroup(@RequestParam("groupName") @ApiParam("群聊名称") String groupName, ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        String mongoId = JWTUtil.getMongoId(token);
        try {
            groupService.createGroup(mongoId, groupName);
            return ResultUtil.success();
        } catch (MyException e) {
            return ResultUtil.result(e.getCode(), e.getMsg());
        }
    }

    @GetMapping("/getMyGroupList")
    @ApiOperation("获取我的群聊列表")
    public Result getMyGroupList(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        String mongoId = JWTUtil.getMongoId(token);
        return ResultUtil.success(groupService.getGroupByUser(mongoId));
    }

    @GetMapping("/getGroupInfo")
    @ApiOperation("获取群聊信息")
    public Result getGroupInfo(@RequestParam("groupId") @ApiParam("群聊Id") String groupId) {
        try {
            return ResultUtil.success(groupService.getGroupInfo(groupId));
        } catch (MyException e) {
            return ResultUtil.result(e.getCode(), e.getMsg());
        }
    }

    @PostMapping("/updateGroupName")
    @ApiOperation("修改群聊名称(仅组长)")
    public Result updateGroupName(@RequestParam("groupId") @ApiParam("群聊Id") String groupId, @RequestParam("groupName") @ApiParam("群聊名称") String groupName, ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        String mongoId = JWTUtil.getMongoId(token);
        try {
            groupService.updateGroupName(mongoId,groupId,groupName);
            return ResultUtil.success();
        }catch (MyException e){
            return ResultUtil.result(e.getCode(),e.getMsg());
        }
    }

    @PostMapping("/addGroupMember")
    @ApiOperation("组长加人")
    public Result addGroupMember(@RequestParam("groupId") @ApiParam("群聊Id") String groupId,@RequestParam("memberList") @ApiParam("成员的MongoId列表") List<String> memberList, ServletRequest request){
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        String mongoId = JWTUtil.getMongoId(token);
        boolean isDuplicate = false;
        List<Map<String,Object>> failedList = new ArrayList<>();
        for (String memberId: memberList){
            try {
                groupService.addGroupMember(mongoId,groupId,memberId);
            }catch (MyException e){
                if (e.getStatusType() == StatusEnum.GROUP_MEMBER_EXISTS_ALREADY || e.getStatusType() == StatusEnum.USER_NOT_EXIST){
                    isDuplicate = true;
                    Map<String,Object> map = new HashMap<>();
                    map.put("error_code",e.getCode());
                    map.put("error_msg",e.getMsg());
                    map.put("error_mongoId",memberId);
                    failedList.add(map);
                }
                else {
                    return ResultUtil.result(e.getCode(),e.getMsg());
                }
            }
        }
        if (isDuplicate){
            return ResultUtil.result(StatusEnum.FAIL.getCode(),StatusEnum.FAIL.getMsg(),failedList);
        }
        return ResultUtil.success();
    }

    @PostMapping("/delGroupMember")
    @ApiOperation("组长踢人")
    public Result delGroupMember(@RequestParam("groupId") @ApiParam("群聊Id") String groupId, @RequestParam("memberList") @ApiParam("成员的MongoId列表") List<String> memberList, ServletRequest request){
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        String mongoId = JWTUtil.getMongoId(token);
        boolean isDuplicate = false;
        List<Map<String,Object>> failedList = new ArrayList<>();
        for (String memberId: memberList){
            try {
                groupService.delGroupMember(mongoId,groupId,memberId);
            }catch (MyException e){
                if (e.getStatusType() == StatusEnum.GROUP_MEMBER_NO_EXISTS || e.getStatusType() == StatusEnum.USER_NOT_EXIST){
                    isDuplicate = true;
                    Map<String,Object> map = new HashMap<>();
                    map.put("error_code",e.getCode());
                    map.put("error_msg",e.getMsg());
                    map.put("error_mongoId",memberId);
                    failedList.add(map);
                }
                else {
                    return ResultUtil.result(e.getCode(),e.getMsg());
                }
            }
        }
        if (isDuplicate){
            return ResultUtil.result(StatusEnum.FAIL.getCode(),StatusEnum.FAIL.getMsg(),failedList);
        }
        return ResultUtil.success();
    }

    @PostMapping("/quitGroup")
    @ApiOperation("群成员退群")
    public Result quitGroup(@RequestParam("groupId") @ApiParam("群聊Id") String groupId, ServletRequest request){
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        String mongoId = JWTUtil.getMongoId(token);
        try {
            groupService.quitGroup(mongoId,groupId);
            return ResultUtil.success();
        }catch (MyException e){
            return ResultUtil.result(e.getCode(),e.getMsg());
        }
    }

    @PostMapping("/delGroup")
    @ApiOperation("组长解散群聊")
    public Result delGroup(@RequestParam("groupId") @ApiParam("群聊Id") String groupId, ServletRequest request){
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        String mongoId = JWTUtil.getMongoId(token);
        try {
            groupService.delGroup(mongoId,groupId);
            return ResultUtil.success();
        }catch (MyException e){
            return ResultUtil.result(e.getCode(),e.getMsg());
        }
    }

}
