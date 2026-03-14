package com.epiprev.business.article;

import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDubbo
@EnableMethodCache(basePackages = "com.epiprev.business.article")
public class ArticleApplication {
    static void main(String[] args) {
        SpringApplication.run(ArticleApplication.class, args);
    }
}
