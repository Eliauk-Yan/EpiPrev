package com.epiprev.admin.service;

import com.epiprev.common.web.result.MultiResult;
import com.epiprev.common.web.result.Result;

import java.util.Map;

/**
 * 文章管理服务接口
 */
public interface ArticleService {

    /**
     * 分页查询文章
     */
    MultiResult<Map<String, Object>> pageArticle(Integer page, Integer size, String title, String category);

    /**
     * 根据Id查询文章
     */
    Result<Map<String, Object>> getArticleById(Long id);

    /**
     * 保存或更新文章
     */
    Result<Boolean> saveOrUpdateArticle(Map<String, Object> article);

    /**
     * 删除文章
     */
    Result<Boolean> deleteArticle(Long id);
}
