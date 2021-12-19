package com.example.music.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.music.domain.Comment;
import com.example.music.service.CommentService;
import com.example.music.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;




@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;
    /**
     添加评论
     */
    @RequestMapping(value = "/add",method= RequestMethod.POST)
    public Object addComment(HttpServletRequest request)  {
        JSONObject jsonObject = new JSONObject();
        String userId = request.getParameter("userId");
        String type = request.getParameter("type");
        String songId = request.getParameter("songId");
        String songListId = request.getParameter("songListId");
        String content = request.getParameter("content").trim();

        //保存到评论的对象中
        Comment comment=new Comment();
        comment.setUserId(Integer.parseInt(userId));
        comment.setType(new Byte(type));
        if (new Byte(type) ==0){
            comment.setSongId(Integer.parseInt(songId));
        }else{
            comment.setSongListId(Integer.parseInt(songListId));
        }
        comment.setContent(content);
        boolean flag=commentService.insert(comment);
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
     修改评论
     */
    @RequestMapping(value = "/update",method= RequestMethod.POST)
    public Object updateComment(HttpServletRequest request)  {
        JSONObject jsonObject = new JSONObject();
        String id=request.getParameter("id").trim();
        String userId = request.getParameter("userId").trim();
        String type = request.getParameter("type").trim();
        String songId = request.getParameter("songId").trim();
        String songListId = request.getParameter("songListId").trim();
        String content = request.getParameter("content").trim();

        //保存到评论的对象中
        Comment comment=new Comment();
        comment.setId(Integer.parseInt(id));
        comment.setUserId(Integer.parseInt(userId));
        comment.setType(new Byte(type));
        if (songId!=null&&songId.equals("")){
            songId=null;
        }else{
            comment.setSongId(Integer.parseInt(songId));
        }
        if (songListId!=null&&songListId.equals("")){
            songListId=null;
        }else{
            comment.setSongListId(Integer.parseInt(songListId));
        }
        comment.setContent(content);
        boolean flag=commentService.update(comment);
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
    public Object deleteComment(HttpServletRequest request){
        String id=request.getParameter("id").trim();
        boolean flag=commentService.delete(Integer.parseInt(id));
        return flag;
    }

    /**
     根据主键查询整个对象
     */
    @RequestMapping(value = "/selectByPrimaryKey",method= RequestMethod.GET)
    public Object selectByPrimaryKey(HttpServletRequest request){
        String id=request.getParameter("id").trim();
        return commentService.selectByPrimaryKey(Integer.parseInt(id));
    }
    /**
     查询所有评论
     */
    @RequestMapping(value = "/allComment",method= RequestMethod.GET)
    public Object allComment(HttpServletRequest request){
        return commentService.allComment();
    }

    /**
     * 查询某个歌曲下的所有评论
     */
    @RequestMapping(value = "/commentOfSongId",method= RequestMethod.GET)
    public Object  commentOfSongId(HttpServletRequest request){
        String songId=request.getParameter("songId");
        return commentService.commentOfSongId(Integer.parseInt(songId));
    }
    /**
     * 查询某个歌单下的所有评论
     */
    @RequestMapping(value = "/commentOfSongListId",method= RequestMethod.GET)
    public Object  commentOfSongListId(HttpServletRequest request){
        String songListId=request.getParameter("songListId");
        return commentService.commentOfSongListId(Integer.parseInt(songListId));
    }
    /**
     * 给某个评论点赞
     */
    @RequestMapping(value = "/like",method= RequestMethod.POST)
    public Object  like(HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        String id=request.getParameter("id").trim();
        String up = request.getParameter("up").trim();

        //保存到评论的对象中
        Comment comment=new Comment();
        comment.setId(Integer.parseInt(id));
        comment.setUp(Integer.parseInt(up));

        boolean flag=commentService.update(comment);
        if(flag){   //添加成功
            jsonObject.put(Constants.CODE,1);
            jsonObject.put(Constants.MSG,"点赞成功");
            return jsonObject;
        }
        jsonObject.put(Constants.CODE,0);
        jsonObject.put(Constants.MSG,"点赞失败");
        return jsonObject;
    }
}

