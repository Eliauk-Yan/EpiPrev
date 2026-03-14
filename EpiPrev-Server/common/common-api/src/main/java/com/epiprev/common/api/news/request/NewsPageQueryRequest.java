package com.epiprev.common.api.news.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 新闻分页查询请求
 */
@Data
public class NewsPageQueryRequest implements Serializable {

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
     * 关键词搜索（标题、摘要）
     */
    private String word;

    /**
     * 新闻级别
     */
    private String level;
}
