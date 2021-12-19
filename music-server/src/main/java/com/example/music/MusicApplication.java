package com.example.music;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/*
  热更新，热加载
  Ctrl+Shift+A搜索，然后搜索registry，注意后面是带三个点的，然后勾选compiler.automake.allow.when.app.running
  执行快捷键
 */
@SpringBootApplication
@MapperScan("com.example.music.dao")
public class MusicApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusicApplication.class, args);
	}

}
