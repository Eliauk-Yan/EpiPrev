package com.epiprev.admin.service.impl;

import com.epiprev.admin.service.PostService;
import com.epiprev.common.web.result.MultiResult;
import com.epiprev.common.web.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

/**
 * 帖子管理服务实现类
 */
@Slf4j
@Service
public class PostServiceImpl implements PostService {

    @Override
    public MultiResult<Map<String, Object>> pagePost(Integer page, Integer size, String title) {
        log.info("Admin分页查询帖子: page={}, size={}, title={}", page, size, title);
        return MultiResult.multiSuccess(Collections.emptyList(), 0, page, size);
    }

    @Override
    public Result<Map<String, Object>> getPostById(Long id) {
        return Result.success(null);
    }

    @Override
    public Result<Boolean> deletePost(Long id) {
        log.info("Admin删除帖子: id={}", id);
        return Result.success(true);
    }
}
