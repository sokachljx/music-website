package com.example.music.controller;

import com.alibaba.fastjson.JSONObject;


import com.example.music.domain.Song;
import com.example.music.service.SongService;
import com.example.music.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;

import org.springframework.util.StringUtils;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/song")
public class SongController {
    @Autowired
    private SongService songService;

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //文件最大10M,DataUnit提供5中类型B,KB,MB,GB,TB
        factory.setMaxFileSize(DataSize.of(100, DataUnit.MEGABYTES));
        /// 设置总上传数据总大小10M
        factory.setMaxRequestSize(DataSize.of(100, DataUnit.MEGABYTES));
        return factory.createMultipartConfig();
    }

    //    添加歌曲
//    F:\SpringBoot\SpringBoot项目\music-server\song\1633321596514小眼鑫 - 该死的懦弱（翻自 胜屿）.mp3
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object addSong(HttpServletRequest req, @RequestParam("file") MultipartFile mpfile){
        JSONObject jsonObject = new JSONObject();
        String singerId = req.getParameter("singerId").trim();
        String name = req.getParameter("name").trim();
        String introduction = req.getParameter("introduction").trim();
        String pic = "/img/songPic/dengziqi.jpg";
        String lyric = req.getParameter("lyric").trim();

        if (mpfile.isEmpty()) {
            jsonObject.put(Constants.CODE, 0);
            jsonObject.put(Constants.MSG, "音乐上传失败！");
            return jsonObject;
        }
        String fileName =System.currentTimeMillis()+ mpfile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "song";
        File file1 = new File(filePath);
        if (!file1.exists()){
            file1.mkdir();
        }

        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        //    F:\SpringBoot\SpringBoot项目\music-server\song\1633321596514小眼鑫 - 该死的懦弱（翻自 胜屿）.mp3
        String storeUrlPath = "/song/"+fileName;
        try {
            mpfile.transferTo(dest);
            Song song = new Song();
            song.setSingerId(Integer.parseInt(singerId));
            song.setName(name);
            song.setIntroduction(introduction);
            song.setCreateTime(new Date());
            song.setUpdateTime(new Date());
            song.setPic(pic);
            song.setLyric(lyric);
            song.setUrl(storeUrlPath);
            boolean res = songService.insert(song);
            if(res){
                jsonObject.put(Constants.CODE, 1);
                jsonObject.put("avator", storeUrlPath);
                jsonObject.put(Constants.MSG, "添加成功");
            } else {
                jsonObject.put(Constants.CODE, 0);
                jsonObject.put(Constants.MSG, "添加失败");
            }
            return jsonObject;
        } catch (IOException e) {
            jsonObject.put(Constants.CODE, 0);
            jsonObject.put(Constants.MSG, "添加失败" + e.getMessage());
            return jsonObject;
        } finally {
            return jsonObject;
        }
    }
    //    返回指定歌手ID的歌曲
    @RequestMapping(value = "/singer/detail", method = RequestMethod.GET)
    public Object songOfSingerId(HttpServletRequest req){
        String singerId = req.getParameter("singerId");
        List<Song> songList =  songService.songOfSingerId(Integer.parseInt(singerId));
        return songList;
    }

    /**
       修改歌曲
   */

    @RequestMapping(value = "/update",method= RequestMethod.POST)
    public Object updateSong(HttpServletRequest request)  {
        JSONObject jsonObject = new JSONObject();
        String id=request.getParameter("id").trim();
        String name = request.getParameter("name").trim();
        String introduction = request.getParameter("introduction").trim();
        String lyric = request.getParameter("lyric").trim();

        //保存到歌曲的对象中
        Song song=new Song();
        //判断传入的id是否为空值
        if(StringUtils.isEmpty(id)||"null".equals(id)){
            jsonObject.put(Constants.CODE,0);
            jsonObject.put(Constants.MSG,"传入的id为空");
            return jsonObject;
        }
        song.setId(Integer.parseInt(id));
        song.setName(name);
        song.setIntroduction(introduction);
        song.setLyric(lyric);
        boolean flag=songService.update(song);
        if(flag){   //修改成功
            jsonObject.put(Constants.CODE,1);
            jsonObject.put(Constants.MSG,"修改成功");
            return jsonObject;
        }
        jsonObject.put(Constants.CODE,0);
        jsonObject.put(Constants.MSG,"修改失败");
        return jsonObject;
    }

    /**
       删除歌曲
   */

    @RequestMapping(value = "/delete",method= RequestMethod.GET)
    public Object deleteSong(HttpServletRequest request){
        // 先查询到数据库中对应的文件地址
        String id=request.getParameter("id").trim();
        boolean flag=songService.delete(Integer.parseInt(id));
        return flag;
    }

    /**
     * 更新歌曲图片
     */

    @RequestMapping(value = "/updateSongPic",method = RequestMethod.POST)
    public Object updateSongPic(@RequestParam("file") MultipartFile urlFile,@RequestParam("id") int id) {
        JSONObject jsonObject = new JSONObject();
        if (urlFile.isEmpty()) {
            jsonObject.put(Constants.CODE, 0);
            jsonObject.put(Constants.MSG, "歌曲上传失败");
            return jsonObject;
        }
        //文件名=当前时间到毫秒+原来的文件名
        String fileName = System.currentTimeMillis() + urlFile.getOriginalFilename();
        // 文件路径
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "img"
                + System.getProperty("file.separator") + "songPic";
        //如果文件不存在，新增该路径
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }
        //实际文件地址
        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        //存储到数据库里的相对地址
        String storeUrlPath = "/img/songPic/" + fileName;
        try {
            urlFile.transferTo(dest);
            Song song = new Song();
            song.setId(id);
            song.setPic(storeUrlPath);
            boolean flag = songService.update(song);
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
            jsonObject.put(Constants.MSG, "上传失败" + e.getMessage());
        } finally {
            return jsonObject;
        }
    }
    /**
     * 更新歌曲Url
     */

    @RequestMapping(value = "/updateSongUrl",method = RequestMethod.POST)
    public Object updateSongUrl(@RequestParam("file") MultipartFile urlFile,@RequestParam("id") int id) {
        JSONObject jsonObject = new JSONObject();
        if (urlFile.isEmpty()) {
            jsonObject.put(Constants.CODE, 0);
            jsonObject.put(Constants.MSG, "歌曲上传失败");
            return jsonObject;
        }
        //文件名=当前时间到毫秒+原来的文件名
        String fileName = System.currentTimeMillis() + urlFile.getOriginalFilename();
        // 文件路径
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator")+ "song";
        //如果文件不存在，新增该路径
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }
        //实际文件地址
        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        //存储到数据库里的相对地址
        String storeUrlPath = "/song/" + fileName;
        try {
            urlFile.transferTo(dest);
            Song song = new Song();
            song.setId(id);
            song.setUrl(storeUrlPath);
            boolean flag = songService.update(song);
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
            jsonObject.put(Constants.MSG, "上传失败" + e.getMessage());
        } finally {
            return jsonObject;
        }
    }

    //    根据歌曲id查询歌曲对象
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public Object detail(HttpServletRequest req){
        String songId = req.getParameter("songId").trim();
        return songService.selectByPrimaryKey(Integer.parseInt(songId));

    }
    //    根据歌手名字精确查询歌曲
    @RequestMapping(value = "/songOfSongName", method = RequestMethod.GET)
    public Object songOfSongName(HttpServletRequest req){
        String songName = req.getParameter("songName");
        return songService.songOfName(songName);

    }

    //    根据歌手名字模糊查询歌曲
    @RequestMapping(value = "/likeSongOfName", method = RequestMethod.GET)
    public Object likeSongOfName(HttpServletRequest req){
        String songName = req.getParameter("songName");
        return songService.likeSongOfName(songName);

    }

    //    查询所有歌曲
    @RequestMapping(value = "/allSong", method = RequestMethod.GET)
    public Object allSong(HttpServletRequest req){
        return songService.allSong();

    }
}
