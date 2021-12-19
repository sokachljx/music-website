package com.example.music.dao;


import com.example.music.domain.Rank;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RankMapper {
    /**
     * 增加
     */
     public int insert(Rank rank);


    /**
     * 查总分
     */
    public int selectScoreSum(Integer songListId);
    /**
     * 查总评分人数
     */
    public int selectRankNum(Integer songListId);


}
