package com.example.music.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KaptchaConfig {
    @Bean
    DefaultKaptcha producer(){
        Properties properties=new Properties();
        properties.put("kaptch.border","no");
        properties.put("kaptch.textproducer.font.color","pink");
        properties.put("kaptch.textproducer.char.space","4");
        properties.put("kaptch.image.height","40");
        properties.put("kaptch.image.width","120");
        properties.put("kaptch.textproducer.font.size","30");

        Config config=new Config(properties);
        DefaultKaptcha defaultKaptcha=new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
