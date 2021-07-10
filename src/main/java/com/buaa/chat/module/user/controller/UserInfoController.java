package com.buaa.chat.module.user.controller;

import com.buaa.chat.exception.MyException;
import com.buaa.chat.module.image.service.ImageService;
import com.buaa.chat.module.user.service.UserInfoService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

@RestController
@Api("用户信息管理")
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private ImageService imageService;

    @GetMapping("/getMyUserInfo")
    @ApiOperation("获取自己的用户信息")
    public Result getUserInfo(ServletRequest request){
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        String mongoId = JWTUtil.getMongoId(token);
        try{
            return ResultUtil.success(userInfoService.getUserInfo(mongoId));
        }catch (MyException e){
            return ResultUtil.result(e.getCode(),e.getMsg());
        }
    }

    @GetMapping("/getUserInfo")
    @ApiOperation("获取他人的用户信息")
    public Result getUserInfo(@RequestParam("mongoId") @ApiParam("用户MongoId") String mongoId){
        try {
            return ResultUtil.success(userInfoService.getUserInfo(mongoId));
        }catch (MyException e){
            return ResultUtil.result(e.getCode(),e.getMsg());
        }
    }

    @PostMapping("/updateUsername")
    @ApiOperation("更改用户名")
    public Result updateUserName(@RequestParam("username") @ApiParam("新用户名") String username,ServletRequest request){
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        String mongoId = JWTUtil.getMongoId(token);
        try {
            userInfoService.updateUserName(mongoId,username);
            return ResultUtil.success(userInfoService.getUserInfo(mongoId));
        }catch (MyException e){
            return ResultUtil.result(e.getCode(),e.getMsg()) ;
        }
    }

    @PostMapping("/updatePassword")
    @ApiOperation("更改密码")
    public Result updatePassword(@RequestParam("password") @ApiParam("新密码") String password,ServletRequest request){
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        String mongoId = JWTUtil.getMongoId(token);
        try {
            userInfoService.updatePassword(mongoId,password);
            return ResultUtil.success(userInfoService.getUserInfo(mongoId));
        }catch (MyException e){
            return ResultUtil.result(e.getCode(),e.getMsg()) ;
        }
    }

    @PostMapping("/updateUserImage")
    @ApiOperation("更改头像")
    public Result updateUserImage(@RequestParam("image") @ApiParam("图片") MultipartFile image, ServletRequest request){
        try {
            String id = imageService.uploadWithCompress(image, 100);
            HttpServletRequest req = (HttpServletRequest) request;
            String token = req.getHeader("Authorization");
            String mongoId = JWTUtil.getMongoId(token);
            String prefix = "http://39.106.148.233:8080/image/";
            userInfoService.updateImgPath(mongoId, prefix +id);
            return ResultUtil.success(userInfoService.getUserInfo(mongoId));
        }catch (MyException e){
            return ResultUtil.result(e.getCode(),e.getMsg()) ;
        }
    }
}
