package com.example.music.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.music.domain.Consumer;
import com.example.music.service.ConsumerService;
import com.example.music.utils.Constants;
import com.example.music.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@RestController
@RequestMapping("/consumer")
public class ConsumerController {

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    RedisUtil redisUtil;
    /**
        添加前端用户
    */
    @RequestMapping(value = "/add",method= RequestMethod.POST)
    public Object addConsumer(HttpServletRequest request)  {
        JSONObject jsonObject = new JSONObject();
        String username = request.getParameter("username").trim();
        String password = request.getParameter("password").trim();
        String sex = request.getParameter("sex").trim();
        String phoneNum = request.getParameter("phoneNum").trim();
        String email = request.getParameter("email").trim();
        String birth = request.getParameter("birth").trim();
        String introduction = request.getParameter("introduction").trim();
        String location = request.getParameter("location").trim();
        String avator = request.getParameter("avator").trim();

        if(username==null||username.equals("")){
            jsonObject.put(Constants.CODE,0);
            jsonObject.put(Constants.MSG,"用户名不能为空");
            return jsonObject;
        }
        Consumer consumer1=consumerService.getByUsername(username);
        if(consumer1!=null){
            jsonObject.put(Constants.CODE,0);
            jsonObject.put(Constants.MSG,"用户名已存在");
            return jsonObject;
        }
        if(password==null||password.equals("")){
            jsonObject.put(Constants.CODE,0);
            jsonObject.put(Constants.MSG,"密码不能为空");
            return jsonObject;
        }
        //把生日转换成date格式
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate=new Date();
        try {
            birthDate = dateFormat.parse(birth);
        }catch(ParseException e){
            e.printStackTrace();
        }
        //保存到前端用户的对象中
        Consumer consumer=new Consumer();
        consumer.setUsername(username);
        consumer.setPassword(password);
        consumer.setSex(new Byte(sex));
        consumer.setPhoneNum(phoneNum);
        consumer.setEmail(email);
        consumer.setBirth(birthDate);
        consumer.setIntroduction(introduction);
        consumer.setLocation(location);
        consumer.setAvator(avator);
        boolean flag=consumerService.insert(consumer);
        if(flag){   //添加成功
            jsonObject.put(Constants.CODE,1);
            jsonObject.put(Constants.MSG,"添加成功");
            return jsonObject;
        }
        jsonObject.put(Constants.CODE,0);
        jsonObject.put(Constants.MSG,"添加失败");
        return jsonObject;
     }

    /**
       修改前端用户
   */
     @RequestMapping(value = "/update",method= RequestMethod.POST)
     public Object updateConsumer(HttpServletRequest request)  {
        JSONObject jsonObject = new JSONObject();
        String id=request.getParameter("id").trim();
        String username = request.getParameter("username").trim();
        String password = request.getParameter("password").trim();
        String sex = request.getParameter("sex").trim();
        String phoneNum = request.getParameter("phoneNum").trim();
        String email = request.getParameter("email").trim();
        String birth = request.getParameter("birth").trim();
        String introduction = request.getParameter("introduction").trim();
        String location = request.getParameter("location").trim();


         if(username==null||username.equals("")){
             jsonObject.put(Constants.CODE,0);
             jsonObject.put(Constants.MSG,"用户名不能为空");
             return jsonObject;
         }
         if(password==null||password.equals("")){
             jsonObject.put(Constants.CODE,0);
             jsonObject.put(Constants.MSG,"密码不能为空");
             return jsonObject;
         }
        //把生日转换成date格式
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate=new Date();
        try {
            birthDate = dateFormat.parse(birth);
        }catch(ParseException e){
            e.printStackTrace();
        }
        //保存到前端用户的对象中
        Consumer consumer=new Consumer();
        consumer.setId(Integer.parseInt(id));
        consumer.setUsername(username);
        consumer.setPassword(password);
        consumer.setSex(new Byte(sex));
        consumer.setPhoneNum(phoneNum);
        consumer.setEmail(email);
        consumer.setBirth(birthDate);
        consumer.setIntroduction(introduction);
        consumer.setLocation(location);
        boolean flag=consumerService.update(consumer);
        if(flag){   //添加成功
            jsonObject.put(Constants.CODE,1);
            jsonObject.put(Constants.MSG,"修改成功");
            return jsonObject;
        }
        jsonObject.put(Constants.CODE,0);
        jsonObject.put(Constants.MSG,"修改失败");
        return jsonObject;
    }


    /**
       删除前端用户
   */
    @RequestMapping(value = "/delete",method= RequestMethod.GET)
    public Object deleteConsumer(HttpServletRequest request){
        String id=request.getParameter("id").trim();
        boolean flag=consumerService.delete(Integer.parseInt(id));
        return flag;
    }

    /**
      根据主键查询整个对象
   */
    @RequestMapping(value = "/selectByPrimaryKey",method= RequestMethod.GET)
    public Object selectByPrimaryKey(HttpServletRequest request){
        String id=request.getParameter("id").trim();
        return consumerService.selectByPrimaryKey(Integer.parseInt(id));
    }
    /**
      查询所有前端用户
   */
    @RequestMapping(value = "/allConsumer",method= RequestMethod.GET)
    public Object allConsumer(HttpServletRequest request){
        return consumerService.allConsumer();
    }


    /**
     * 更新前端用户图片
     */

    @RequestMapping(value = "/updateConsumerPic",method = RequestMethod.POST)
    public Object updateConsumerPic(@RequestParam("file") MultipartFile urlFile,@RequestParam("id") int id) {
        JSONObject jsonObject = new JSONObject();
        if (urlFile.isEmpty()) {
            jsonObject.put(Constants.CODE, 0);
            jsonObject.put(Constants.MSG, "文件上传失败");
            return jsonObject;
        }
        //文件名=当前时间到毫秒+原来的文件名
        String fileName = System.currentTimeMillis() + urlFile.getOriginalFilename();
        // 文件路径
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "avatorImages";
        //如果文件不存在，新增该路径
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }
        //实际文件地址
        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        //存储到数据库里的相对地址
        String storeUrlPath = "/avatorImages/" + fileName;
        try {

            urlFile.transferTo(dest);
            Consumer consumer = new Consumer();
            consumer.setId(id);
            consumer.setAvator(storeUrlPath);
            boolean flag = consumerService.update(consumer);
            if (flag) {
                jsonObject.put(Constants.CODE, 1);
                jsonObject.put(Constants.MSG, "上传成功");
                jsonObject.put("avator", storeUrlPath);
                return jsonObject;
            }
            jsonObject.put(Constants.CODE, 0);
            jsonObject.put(Constants.MSG, "上传失败");
            return jsonObject;
        } catch (IOException e) {
            jsonObject.put(Constants.CODE, 0);
            jsonObject.put(Constants.MSG, "上传失败"+e.getMessage());
        }finally{
            return jsonObject;
        }
    }
    /**
     前端用户登录
     */
    @RequestMapping(value = "/login",method= RequestMethod.POST)
    public Object login(HttpServletRequest request)  {
        JSONObject jsonObject = new JSONObject();


        String username = request.getParameter("username").trim();
        String password = request.getParameter("password").trim();
        if(username==null||username.equals("")){
            jsonObject.put(Constants.CODE,0);
            jsonObject.put(Constants.MSG,"用户名不能为空");
            return jsonObject;
        }
        if(password==null||password.equals("")){
            jsonObject.put(Constants.CODE,0);
            jsonObject.put(Constants.MSG,"密码不能为空");
            return jsonObject;
        }
        String code=request.getParameter("code").trim();
        if(StringUtils.isEmpty(code)){
            jsonObject.put(Constants.CODE,0);
            jsonObject.put(Constants.MSG,"验证码不能为空");
            return jsonObject;
        }
        String key=request.getParameter("key").trim();
        if(StringUtils.isEmpty(key)){
            jsonObject.put(Constants.CODE,0);
            jsonObject.put(Constants.MSG,"无该验证码");
            return jsonObject;
        }
        String saveCode = (String) redisUtil.hget(Constants.CAPTCHA_KEY,key);
        if(!code.equals(saveCode)){
            jsonObject.put(Constants.CODE,0);
            jsonObject.put(Constants.MSG,"验证码错误");
            return jsonObject;
        }

        //保存到前端用户的对象中
        Consumer consumer=new Consumer();
        consumer.setUsername(username);
        consumer.setPassword(password);
        boolean flag=consumerService.verifyPassword(username,password);
        if(flag){   //验证成功
            jsonObject.put(Constants.CODE,1);
            jsonObject.put(Constants.MSG,"登录成功");
            jsonObject.put("userMsg",consumerService.getByUsername(username));
            return jsonObject;
        }
        jsonObject.put(Constants.CODE,0);
        jsonObject.put(Constants.MSG,"用户名或密码错误");
        return jsonObject;
    }
}
