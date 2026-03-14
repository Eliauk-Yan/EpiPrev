package com.epiprev.business.news.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewsVO {

    private Long id;

    private String title;

    private String summary;

    private String source;

    private String level;

    private String content;

    private LocalDateTime publishTime;
}

