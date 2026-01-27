package com.epiprev.server.knowledge.dto;

import lombok.Data;

@Data
public class ArticleDTO {
    private String title;
    private String category;
    private String summary;
    private String content;
}

