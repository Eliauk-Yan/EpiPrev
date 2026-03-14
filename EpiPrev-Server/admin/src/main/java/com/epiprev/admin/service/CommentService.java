package com.epiprev.admin.service;

import com.epiprev.common.web.result.MultiResult;
import com.epiprev.common.web.result.Result;

import java.util.Map;

/**
 * 帖子评论管理服务接口
 */
public interface CommentService {

    /**
     * 分页查询评论
     */
    MultiResult<Map<String, Object>> pageComment(Integer page, Integer size, String content);

    /**
     * 根据 Id 查询评论
     */
    Result<Map<String, Object>> getCommentById(Long id);

    /**
     * 删除评论
     */
    Result<Boolean> deleteComment(Long id);
}