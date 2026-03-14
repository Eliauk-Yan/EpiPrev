package com.epiprev.business.forum;

import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableMethodCache(basePackages = "com.epiprev.business.forum")
@MapperScan("com.epiprev.business.forum.mapper")
@ComponentScan(basePackages = { "com.epiprev.business.forum", "com.epiprev.common" })
public class ForumApplication {
    public static void main(String[] args) {
        SpringApplication.run(ForumApplication.class, args);
    }
}
