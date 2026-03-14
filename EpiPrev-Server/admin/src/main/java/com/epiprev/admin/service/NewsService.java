package com.epiprev.admin.service;

import com.epiprev.common.web.result.MultiResult;
import com.epiprev.common.web.result.Result;

import java.util.Map;

/**
 * 新闻管理服务接口
 */
public interface NewsService {

    /**
     * 分页查询新闻
     */
    MultiResult<Map<String, Object>> pageNews(Integer page, Integer size, String word, String level);

    /**
     * 根据ID查询新闻详情
     */
    Result<Map<String, Object>> getNewsById(Long id);

    /**
     * 保存或更新新闻
     */
    Result<Boolean> saveOrUpdateNews(Map<String, Object> news);

    /**
     * 删除新闻
     */
    Result<Boolean> deleteNews(Long id);
}
