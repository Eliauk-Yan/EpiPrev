package com.epiprev.common.api.article.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文章保存/更新请求
 */
@Data
public class ArticleSaveRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String category;

    private String summary;

    private String content;

    /**
     * 封面地址
     */
    private String cover;

    /**
     * 视频地址
     */
    private String videoUrl;

    /**
     * 类型：0=图文，1=视频
     */
    private Integer type;
}

