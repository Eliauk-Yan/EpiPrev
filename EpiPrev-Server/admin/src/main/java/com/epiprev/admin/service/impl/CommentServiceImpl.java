package com.epiprev.admin.service.impl;

import com.epiprev.admin.service.CommentService;
import com.epiprev.common.web.result.MultiResult;
import com.epiprev.common.web.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

/**
 * 评论管理服务实现类
 */
@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    @Override
    public MultiResult<Map<String, Object>> pageComment(Integer page, Integer size, String content) {
        log.info("Admin分页查询评论: page={}, size={}, content={}", page, size, content);
        return MultiResult.multiSuccess(Collections.emptyList(), 0, page, size);
    }

    @Override
    public Result<Map<String, Object>> getCommentById(Long id) {
        return Result.success(null);
    }

    @Override
    public Result<Boolean> deleteComment(Long id) {
        log.info("Admin删除评论: id={}", id);
        return Result.success(true);
    }
}
