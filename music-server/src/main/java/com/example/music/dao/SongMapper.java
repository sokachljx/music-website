package com.example.music.dao;


import com.example.music.domain.Song;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongMapper {
    /**
     * 增加
     */
     public int insert(Song song);
    /**
     * 修改
     */
    public int update(Song song);
    /**
     * 删除
     */
    public int delete(Integer id);
    /**
     * 根据主键查询整个对象
     */
    public Song selectByPrimaryKey(Integer id);
    /**
     * 查询所有歌曲
     */
    public List<Song> allSong();
    /**
     * 根据歌名字精确查询列表
     */
    public  List<Song> songOfName(String name);
    /**
     * 根据歌名吗模糊查询列表
     */
    public  List<Song> likeSongOfName(String name);
    /**
     * 根据歌手id查询
     */
    public List<Song> songOfSingerId(Integer singerId);

}
