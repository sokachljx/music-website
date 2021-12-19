package com.example.music.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.music.utils.Constants;
import com.example.music.utils.RedisUtil;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/code")
public class AuthController {

    @Autowired
    Producer producer;
    @Autowired
    RedisUtil redisUtil;

    @RequestMapping (value ="/captcha",method= RequestMethod.POST)
    public JSONObject captcha() throws IOException{
        String key= UUID.randomUUID().toString();
        String code=producer.createText();
        BufferedImage image=producer.createImage(code);
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        ImageIO.write(image,"jpg",outputStream);
        Base64.Encoder encoder= Base64.getEncoder();
        String str="data:image/jpeg;base64,";
        String base64Img=str+encoder.encodeToString(outputStream.toByteArray());
        redisUtil.hset(Constants.CAPTCHA_KEY,key,code,120);
        JSONObject jsonObject = new JSONObject();
        Map<String,String> map=new LinkedHashMap<String,String>();
        map.put("key",key);
        map.put("captchaImg",base64Img);
        jsonObject.put(Constants.CODE,200);
        jsonObject.put(Constants.MSG,"请求成功");
        jsonObject.put(Constants.DATA,map);
        return jsonObject;

    }
}
