package com.epiprev.common.api.article.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文章分页查询请求
 */
@Data
public class ArticlePageQueryRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    private Integer page;

    /**
     * 每页条数
     */
    private Integer size;

    /**
     * 标题搜索
     */
    private String title;

    /**
     * 分类
     */
    private String category;
}

