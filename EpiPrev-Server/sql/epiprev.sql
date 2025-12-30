create database epiprev;

use epiprev;

CREATE TABLE `roles`
(
    `id`          BIGINT UNSIGNED              NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `name`        VARCHAR(255)                 NOT NULL COMMENT '角色名称',
    `description` VARCHAR(512)     DEFAULT NULL COMMENT '角色描述',
    `deleted`     tinyint unsigned default '0' null comment '删除标记：0-未删除，1-已删除',
    `version`     BIGINT UNSIGNED  DEFAULT 0 COMMENT '版本号',
    `created_at`  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色表';

CREATE TABLE `permissions`
(
    `id`          BIGINT UNSIGNED              NOT NULL AUTO_INCREMENT COMMENT '权限ID',
    `name`        VARCHAR(255)                 NOT NULL COMMENT '权限名称',
    `description` VARCHAR(512)     DEFAULT NULL COMMENT '权限描述',
    `deleted`     tinyint unsigned default '0' null comment '删除标记：0-未删除，1-已删除',
    `version`     BIGINT UNSIGNED  DEFAULT 0 COMMENT '版本号',
    `created_at`  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='权限表';

CREATE TABLE `role_permissions`
(
    `role_id`       BIGINT UNSIGNED NOT NULL COMMENT '角色ID',
    `permission_id` BIGINT UNSIGNED NOT NULL COMMENT '权限ID',
    PRIMARY KEY (`role_id`, `permission_id`),
    FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色与权限关联表';

CREATE TABLE `users`
(
    `id`         BIGINT UNSIGNED              NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username`   VARCHAR(255)                 NOT NULL COMMENT '用户名',
    `password`   VARCHAR(255)                 NOT NULL COMMENT '密码',
    `email`      VARCHAR(255)     DEFAULT NULL COMMENT '邮箱',
    `phone`      VARCHAR(20)      DEFAULT NULL COMMENT '手机号',
    `role_id`    BIGINT UNSIGNED  DEFAULT NULL COMMENT '角色ID',
    `deleted`    tinyint unsigned default '0' null comment '删除标记：0-未删除，1-已删除',
    `version`     BIGINT UNSIGNED  DEFAULT 0 COMMENT '版本号',
    `created_at` TIMESTAMP        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE SET NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户表';


