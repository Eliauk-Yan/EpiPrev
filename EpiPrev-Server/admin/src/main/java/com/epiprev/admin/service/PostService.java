package com.epiprev.admin.service;

import com.epiprev.common.web.result.MultiResult;
import com.epiprev.common.web.result.Result;

import java.util.Map;

/**
 * 论坛帖子管理服务接口
 */
public interface PostService {

    /**
     * 分页查询帖子
     */
    MultiResult<Map<String, Object>> pagePost(Integer page, Integer size, String title);

    /**
     * 根据Id查询帖子
     */
    Result<Map<String, Object>> getPostById(Long id);

    /**
     * 删除帖子
     */
    Result<Boolean> deletePost(Long id);
}
