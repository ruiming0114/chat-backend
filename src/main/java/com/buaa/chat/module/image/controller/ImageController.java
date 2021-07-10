package com.buaa.chat.module.image.controller;

import com.buaa.chat.exception.MyException;
import com.buaa.chat.module.image.service.ImageService;
import com.buaa.chat.response.Result;
import com.buaa.chat.util.ResultUtil;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;


import javax.annotation.Resource;

@RestController
public class ImageController {

    @Resource
    private ImageService imageService;

    @RequestMapping("/image/{id}")
    @ApiIgnore
    public byte[] showImage(@PathVariable("id") String id) throws Exception {
        return imageService.download(id);
    }

    @PostMapping("/uploadImage")
    public Result uploadImage(@RequestParam("image") @ApiParam("图片") MultipartFile image){
        try{
            String id = imageService.uploadWithoutCompress(image);
            String prefix = "http://39.106.148.233:8080/image/";
            return ResultUtil.success(prefix+id);
        }catch (MyException e){
            return ResultUtil.result(e.getCode(),e.getMsg());
        }
    }

}
