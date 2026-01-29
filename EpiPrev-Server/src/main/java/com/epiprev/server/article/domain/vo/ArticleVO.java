package com.epiprev.server.article.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ArticleVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String category;

    private String summary;

    private String content;

    private String cover;

    private String videoUrl;

    private Integer type;

    private Integer views;

    private LocalDateTime createTime;
}
