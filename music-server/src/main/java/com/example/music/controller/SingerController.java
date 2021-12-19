package com.example.music.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.music.domain.Singer;
import com.example.music.service.SingerService;
import com.example.music.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@RestController
@RequestMapping("/singer")
public class SingerController {

    @Autowired
    private SingerService singerService;
    /**
        添加歌手
    */
    @RequestMapping(value = "/add",method= RequestMethod.POST)
    public Object addSinger(HttpServletRequest request)  {
        JSONObject jsonObject = new JSONObject();
        String name = request.getParameter("name").trim();
        String sex = request.getParameter("sex").trim();
        String pic = request.getParameter("pic").trim();
        String birth = request.getParameter("birth").trim();
        String location = request.getParameter("location").trim();
        String introduction = request.getParameter("introduction").trim();
        //把生日转换成date格式
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate=new Date();
        try {
            birthDate = dateFormat.parse(birth);
        }catch(ParseException e){
            e.printStackTrace();
        }
        //保存到歌手的对象中
        Singer singer=new Singer();
        singer.setName(name);
        singer.setSex(new Byte(sex));
        singer.setPic(pic);
        singer.setBirth(birthDate);
        singer.setLocation(location);
        singer.setIntroduction(introduction);
        boolean flag=singerService.insert(singer);
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
       修改歌手
   */
     @RequestMapping(value = "/update",method= RequestMethod.POST)
     public Object updateSinger(HttpServletRequest request)  {
        JSONObject jsonObject = new JSONObject();
        String id=request.getParameter("id").trim();
        String name = request.getParameter("name").trim();
        String sex = request.getParameter("sex").trim();

        String birth = request.getParameter("birth").trim();
        String location = request.getParameter("location").trim();
        String introduction = request.getParameter("introduction").trim();
        //把生日转换成date格式
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate=new Date();
        try {
            birthDate = dateFormat.parse(birth);
        }catch(ParseException e){
            e.printStackTrace();
        }
        //保存到歌手的对象中
        Singer singer=new Singer();
        singer.setId(Integer.parseInt(id));
        singer.setName(name);
        singer.setSex(new Byte(sex));
        singer.setBirth(birthDate);
        singer.setLocation(location);
        singer.setIntroduction(introduction);
        boolean flag=singerService.update(singer);
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
       删除歌手
   */
    @RequestMapping(value = "/delete",method= RequestMethod.GET)
    public Object deleteSinger(HttpServletRequest request){
        String id=request.getParameter("id").trim();
        boolean flag=singerService.delete(Integer.parseInt(id));
        return flag;
    }

    /**
      根据主键查询整个对象
   */
    @RequestMapping(value = "/selectByPrimaryKey",method= RequestMethod.GET)
    public Object selectByPrimaryKey(HttpServletRequest request){
        String id=request.getParameter("id").trim();
        return singerService.selectByPrimaryKey(Integer.parseInt(id));
    }
    /**
      查询所有歌手
   */
    @RequestMapping(value = "/allSinger",method= RequestMethod.GET)
    public Object allSinger(HttpServletRequest request){
        return singerService.allSinger();
    }

    /**
     * 根据主歌手名字模糊查询列表
     */
    @RequestMapping(value = "/singerOfName",method= RequestMethod.GET)
    public Object  singerOfName(HttpServletRequest request){
        String name=request.getParameter("name").trim();
        return singerService.singerOfName("%"+name+"%");
    }

    /**
     * 根据性别查询
     */
    @RequestMapping(value = "/singerOfSex",method= RequestMethod.GET)
    public Object  singerOfSex(HttpServletRequest request){
        String sex=request.getParameter("sex").trim();
        return singerService.singerOfSex(Integer.parseInt(sex));
    }
    /**
     * 更新歌手图片
     */

    @RequestMapping(value = "/updateSingerPic",method = RequestMethod.POST)
    public Object updateSingerPic(@RequestParam("file") MultipartFile urlFile,@RequestParam("id") int id) {
        JSONObject jsonObject = new JSONObject();
        if (urlFile.isEmpty()) {
            jsonObject.put(Constants.CODE, 0);
            jsonObject.put(Constants.MSG, "文件上传失败");
            return jsonObject;
        }
        //文件名=当前时间到毫秒+原来的文件名
        String fileName = System.currentTimeMillis() + urlFile.getOriginalFilename();
        // 文件路径
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "img"
                + System.getProperty("file.separator") + "singerPic";
        //如果文件不存在，新增该路径
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }
        //实际文件地址
        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        //存储到数据库里的相对地址
        String storeUrlPath = "/img/singerPic/" + fileName;
        try {
//            System.out.println(dest);
            urlFile.transferTo(dest);
            Singer singer = new Singer();
            singer.setId(id);
            singer.setPic(storeUrlPath);
            boolean flag = singerService.update(singer);
            if (flag) {
                jsonObject.put(Constants.CODE, 1);
                jsonObject.put(Constants.MSG, "上传成功");
                jsonObject.put("pic", storeUrlPath);
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
}
