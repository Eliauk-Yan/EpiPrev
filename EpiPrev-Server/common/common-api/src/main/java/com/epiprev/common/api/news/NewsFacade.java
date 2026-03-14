package com.epiprev.common.api.news;

import com.epiprev.common.api.news.request.NewsPageQueryRequest;
import com.epiprev.common.api.news.request.NewsSaveRequest;
import com.epiprev.common.api.news.response.NewsInfo;
import com.epiprev.common.api.news.response.NewsResponse;
import com.epiprev.common.base.response.PageResult;

/**
 * 新闻服务 Dubbo RPC 接口
 */
public interface NewsFacade {

    /**
     * 分页查询新闻
     */
    NewsResponse<PageResult<NewsInfo>> pageNews(NewsPageQueryRequest request);

    /**
     * 根据ID查询新闻详情
     */
    NewsResponse<NewsInfo> getNewsById(Long id);

    /**
     * 保存或更新新闻
     */
    NewsResponse<Boolean> saveOrUpdateNews(NewsSaveRequest request);

    /**
     * 删除新闻
     */
    NewsResponse<Boolean> deleteNews(Long id);
}
