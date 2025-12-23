package com.epiprev.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @classname GatewayApplication
 * @description 网关
 * @date 2025/12/20 19:09
 * @created by ZhangBo
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {
    static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
