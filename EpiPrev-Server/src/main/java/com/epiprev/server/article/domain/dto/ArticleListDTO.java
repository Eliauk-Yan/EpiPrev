package com.epiprev.server.article.domain.dto;

import lombok.Data;

/**
 * @classname ArticleListDTO
 * @description 获取文章列表参数
 * @date 2026/01/28 00:41
 */
@Data
public class ArticleListDTO {

    private long size = 10L;

    private long current = 1L;

    private String word;

    private Integer type;

}
