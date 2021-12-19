package com.example.music.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.music.domain.ListSong;
import com.example.music.service.ListSongService;
import com.example.music.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/listSong")
public class ListSongController {
    @Autowired
    private ListSongService listSongService;



    //    给歌单添加歌曲
//    F:\SpringBoot\SpringBoot项目\music-server\listSong\1633321596514小眼鑫 - 该死的懦弱（翻自 胜屿）.mp3
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object addListSong(HttpServletRequest req){
        JSONObject jsonObject = new JSONObject();
        String songId = req.getParameter("songId").trim();
        String songListId = req.getParameter("songListId").trim();
        ListSong listSong=new ListSong();
        listSong.setSongId(Integer.parseInt(songId));
        listSong.setSongListId(Integer.parseInt(songListId));
        boolean res = listSongService.insert(listSong);
        //判断传入的id是否为空值
        if(StringUtils.isEmpty(songListId)||"null".equals(songListId)){
            jsonObject.put(Constants.CODE,0);
            jsonObject.put(Constants.MSG,"传入的id为空");
            return jsonObject;
        }
        if(res){
            jsonObject.put(Constants.CODE, 1);
            jsonObject.put(Constants.MSG, "添加成功");
        } else {
            jsonObject.put(Constants.CODE, 0);
            jsonObject.put(Constants.MSG, "添加失败");
        }
        return jsonObject;

    }
    //    根据歌单id查询歌曲
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public Object detail(HttpServletRequest req){
        String songListId = req.getParameter("songListId");
        return listSongService.listSongOfSongListId(Integer.parseInt(songListId));

    }

//    /**
//     修改歌曲
//     */
//
//    @RequestMapping(value = "/update",method= RequestMethod.POST)
//    public Object updateSong(HttpServletRequest request)  {
//        JSONObject jsonObject = new JSONObject();
//        String id=request.getParameter("id").trim();
//        String songId = request.getParameter("songId").trim();
//        String songListId = request.getParameter("songListId").trim();
//        ListSong listSong=new ListSong();
//        listSong.setId(Integer.parseInt(id));
//        listSong.setSongId(Integer.parseInt(songId));
//        listSong.setSongListId(Integer.parseInt(songListId));
//        boolean flag=listSongService.update(listSong);
//        //判断传入的id是否为空值
//        if(StringUtils.isEmpty(id)||"null".equals(id)){
//            jsonObject.put(Constants.CODE,0);
//            jsonObject.put(Constants.MSG,"传入的id为空");
//            return jsonObject;
//        }
//        if(flag){   //修改成功
//            jsonObject.put(Constants.CODE,1);
//            jsonObject.put(Constants.MSG,"修改成功");
//            return jsonObject;
//        }
//        jsonObject.put(Constants.CODE,0);
//        jsonObject.put(Constants.MSG,"修改失败");
//        return jsonObject;
//    }

    /**
       删除歌单里的歌曲
   */

    @RequestMapping(value = "/delete",method= RequestMethod.GET)
    public Object delete(HttpServletRequest request){
        String songId=request.getParameter("songId").trim();
        String songListId=request.getParameter("songListId").trim();
        boolean flag=listSongService.deleteBySongIdAndSongListId(Integer.parseInt(songId),Integer.parseInt(songListId));
        return flag;
    }



}
