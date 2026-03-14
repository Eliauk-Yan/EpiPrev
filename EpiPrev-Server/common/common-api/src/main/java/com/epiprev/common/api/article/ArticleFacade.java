package com.epiprev.common.api.article;

import com.epiprev.common.api.article.request.ArticlePageQueryRequest;
import com.epiprev.common.api.article.request.ArticleSaveRequest;
import com.epiprev.common.api.article.response.ArticleInfo;
import com.epiprev.common.api.article.response.ArticleResponse;
import com.epiprev.common.base.response.PageResult;

/**
 * 文章服务 Dubbo RPC 接口
 */
public interface ArticleFacade {

    /**
     * 分页查询文章
     */
    ArticleResponse<PageResult<ArticleInfo>> pageArticle(ArticlePageQueryRequest request);

    /**
     * 根据ID查询文章详情
     */
    ArticleResponse<ArticleInfo> getArticleById(Long id);

    /**
     * 保存或更新文章
     */
    ArticleResponse<Boolean> saveOrUpdateArticle(ArticleSaveRequest request);

    /**
     * 删除文章
     */
    ArticleResponse<Boolean> deleteArticle(Long id);
}

