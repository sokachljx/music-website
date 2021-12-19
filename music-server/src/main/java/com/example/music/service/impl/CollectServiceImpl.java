package com.example.music.service.impl;


import com.example.music.dao.CollectMapper;
import com.example.music.domain.Collect;
import com.example.music.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectServiceImpl implements CollectService {

    @Autowired
    private CollectMapper collectMapper;

    @Override
    public boolean insert(Collect collect) {
        return collectMapper.insert(collect)>0;
    }

    @Override
    public boolean delete(Integer id) {
        return collectMapper.delete(id)>0;
    }

    @Override
    public boolean deleteByUserIdSongId(Integer userId, Integer songId) {
        return collectMapper.deleteByUserIdSongId(userId,songId)>0;
    }

    @Override
    public List<Collect> allCollect() {
        return collectMapper.allCollect();
    }

    @Override
    public List<Collect> collectOfUserId(Integer userId) {
        return collectMapper.collectOfUserId(userId);
    }

    @Override
    public boolean existSongId(Integer userId, Integer songId) {
        return collectMapper.existSongId(userId,songId)>0;
    }
}
