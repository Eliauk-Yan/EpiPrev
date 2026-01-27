package com.epiprev.server.knowledge.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleVO {
    private Long id;
    private String title;
    private String category;
    private String summary;
    private String content;
    private Integer views;
    private LocalDateTime createTime;
}

