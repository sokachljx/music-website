package com.example.music.dao;


import org.springframework.stereotype.Repository;

@Repository
public interface AdminMapper {
    /**
     * 验证密码是否正确
     */
     int verifyPassword(String username,String password);
}
