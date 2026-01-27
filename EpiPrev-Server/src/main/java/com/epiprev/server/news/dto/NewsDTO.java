package com.epiprev.server.news.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewsDTO {
    private String title;
    private String summary;
    private String source;
    private String level;
    private String content;
    private LocalDateTime publishTime;
}

