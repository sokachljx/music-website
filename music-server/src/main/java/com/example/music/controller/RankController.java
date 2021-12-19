package com.example.music.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.music.domain.Rank;
import com.example.music.service.RankService;
import com.example.music.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
public class RankController {

    @Autowired
    private RankService rankService;

    /**
    新增评价
     */
    @RequestMapping(value = "/rank/add", method = RequestMethod.POST)
    public Object add(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String songListId = request.getParameter("songListId").trim();
        String consumerId = request.getParameter(" consumerId").trim();
        String score = request.getParameter(" score").trim();

        Rank rank=new Rank();
        rank.setSongListId(Integer.parseInt(songListId));
        rank.setConsumerId(Integer.parseInt(consumerId));
        rank.setScore(Integer.parseInt(score));
        boolean flag = rankService.insert(rank);
        if (flag) {
            jsonObject.put(Constants.CODE, "1");
            jsonObject.put(Constants.MSG, "评价成功");

            return jsonObject;

        } else {
            jsonObject.put(Constants.CODE, "0");
            jsonObject.put(Constants.MSG, "评价失败");
            return jsonObject;
        }
    }

    /**
     计算平均分
     */
    @RequestMapping(value = "/rank", method = RequestMethod.GET)
    public Object rankOfSongListId(HttpServletRequest request) {
        String songListId=request.getParameter("songListId");
        return rankService.rankOfSongListId(Integer.parseInt(songListId));
    }
}
