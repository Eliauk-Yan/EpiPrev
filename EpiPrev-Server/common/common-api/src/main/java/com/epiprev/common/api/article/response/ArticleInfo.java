package com.epiprev.common.api.article.response;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文章信息响应 DTO
 */
@Data
public class ArticleInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String category;

    private String summary;

    private String content;

    private String cover;

    private String videoUrl;

    /**
     * 类型：0=图文，1=视频
     */
    private Integer type;

    private Integer views;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}

