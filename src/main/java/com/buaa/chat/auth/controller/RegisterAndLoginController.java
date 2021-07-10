package com.buaa.chat.auth.controller;

import com.buaa.chat.exception.MyException;
import com.buaa.chat.response.StatusEnum;
import com.buaa.chat.response.Result;
import com.buaa.chat.module.user.pojo.User;
import com.buaa.chat.auth.service.RegisterAndLoginService;
import com.buaa.chat.module.user.service.UserInfoService;
import com.buaa.chat.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@RestController
@Api("登录注册Api")
public class RegisterAndLoginController {

    @Resource
    private RegisterAndLoginService registerAndLoginService;

    @Resource
    private UserInfoService userInfoService;

    @PostMapping("/register")
    @ApiOperation("注册用户")
    public Result register(@RequestParam("userId") @ApiParam("用户Id") String userId,
                           @RequestParam("password") @ApiParam("密码") String password){
        try {
            registerAndLoginService.register(userId,password);
            return ResultUtil.success();
        }
        catch (MyException e){
            return ResultUtil.result(e.getCode(),e.getMsg());
        }
    }

    @PostMapping("/login")
    @ApiOperation("登录")
    public Result login(@RequestParam("userId") String userId, @RequestParam("password") String password, HttpServletResponse response){
        try {
            String token = registerAndLoginService.login(userId,password);
            response.setHeader("Authorization",token);
            response.setHeader("Access-Control-Expose-Headers", "Authorization");
            User user = userInfoService.findUserByUserId(userId);
            return ResultUtil.success(userInfoService.getUserInfo(user.getMongoId()));
        }catch (MyException e){
            return ResultUtil.result(e.getCode(),e.getMsg());
        }
    }

    @RequestMapping(value = "/unauthorized")
    @ApiIgnore
    public Result unAuthorized(){
        return ResultUtil.result(StatusEnum.UNAUTHORIZED.getCode(), StatusEnum.UNAUTHORIZED.getMsg());
    }
}
