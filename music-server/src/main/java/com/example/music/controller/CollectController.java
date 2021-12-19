package com.example.music.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.music.domain.Collect;
import com.example.music.service.CollectService;
import com.example.music.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/collect")
public class CollectController {

    @Autowired
    private CollectService collectService;
    /**
     添加收藏
     */
    @RequestMapping(value = "/add",method= RequestMethod.POST)
    public Object addCollect(HttpServletRequest request)  {
        JSONObject jsonObject = new JSONObject();
        String userId = request.getParameter("userId");
        String type = request.getParameter("type");
        String songId = request.getParameter("songId");
        if(songId==null||songId.equals("")){
            jsonObject.put(Constants.CODE,0);
            jsonObject.put(Constants.MSG,"收藏歌曲为空");
            return jsonObject;
        }
        if(collectService.existSongId(Integer.parseInt(userId),Integer.parseInt(songId))){
            jsonObject.put(Constants.CODE,2);
            jsonObject.put(Constants.MSG,"已收藏");
            return jsonObject;
        }
        //保存到收藏的对象中
        Collect collect=new Collect();
        collect.setUserId(Integer.parseInt(userId));
        collect.setType(new Byte(type));
        collect.setSongId(Integer.parseInt(songId));

        boolean flag=collectService.insert(collect);
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
     删除收藏
     */
    @RequestMapping(value = "/delete",method= RequestMethod.GET)
    public Object deleteCollect(HttpServletRequest request){
        String userId = request.getParameter("userId");
        String songId = request.getParameter("songId");
        boolean flag=collectService.deleteByUserIdSongId(Integer.parseInt(userId),Integer.parseInt(songId));
        return flag;
    }

    /**
     查询所有收藏
     */
    @RequestMapping(value = "/allCollect",method= RequestMethod.GET)
    public Object allCollect(HttpServletRequest request){
        return collectService.allCollect();
    }

    /**
     * 查询某个用户的收藏列表
     */
    @RequestMapping(value = "/collectOfUserId",method= RequestMethod.GET)
    public Object  collectOfUserId(HttpServletRequest request){
        String userId=request.getParameter("userId");
        return collectService.collectOfUserId(Integer.parseInt(userId));
    }

}

