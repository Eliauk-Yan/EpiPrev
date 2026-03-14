package com.epiprev.business.news;

import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
@EnableMethodCache(basePackages = "com.epiprev.business.news")
public class NewsApplication {

    static void main(String[] args) {
        SpringApplication.run(NewsApplication.class, args);
    }
}

