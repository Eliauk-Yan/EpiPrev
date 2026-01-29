package com.epiprev.server;

import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableMethodCache(basePackages = "com.epiprev.server")
public class EpiPrevServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EpiPrevServerApplication.class, args);
    }
}
