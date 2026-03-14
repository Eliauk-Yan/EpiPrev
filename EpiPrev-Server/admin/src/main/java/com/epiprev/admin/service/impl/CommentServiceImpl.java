package com.epiprev.admin.service.impl;

import com.epiprev.admin.service.CommentService;
import com.epiprev.common.api.forum.ForumFacade;
import com.epiprev.common.api.forum.request.ForumCommentPageQueryRequest;
import com.epiprev.common.api.forum.response.ForumCommentInfo;
import com.epiprev.common.api.forum.response.ForumResponse;
import com.epiprev.common.base.response.PageResult;
import com.epiprev.common.web.result.MultiResult;
import com.epiprev.common.web.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 评论管理服务实现类
 */
@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    @DubboReference
    private ForumFacade forumFacade;

    @Override
    public MultiResult<Map<String, Object>> pageComment(Integer page, Integer size, String content) {
        log.info("Admin 分页查询评论：page={}, size={}, content={}", page, size, content);
        ForumCommentPageQueryRequest request = new ForumCommentPageQueryRequest();
        request.setCurrent(page);
        request.setSize(size);
        request.setContent(content);

        ForumResponse<PageResult<ForumCommentInfo>> response = forumFacade.pageComment(request);
        if (Boolean.TRUE.equals(response.getSuccess()) && response.getData() != null) {
            PageResult<ForumCommentInfo> pageResult = response.getData();
            List<Map<String, Object>> data = pageResult.getList().stream()
                    .map(this::convertToMap)
                    .collect(Collectors.toList());
            return MultiResult.multiSuccess(data, pageResult.getTotal(), page, size);
        }
        return MultiResult.multiSuccess(Collections.emptyList(), 0, page, size);
    }

    @Override
    public Result<Map<String, Object>> getCommentById(Long id) {
        ForumResponse<ForumCommentInfo> response = forumFacade.getCommentById(id);
        if (Boolean.TRUE.equals(response.getSuccess()) && response.getData() != null) {
            return Result.success(convertToMap(response.getData()));
        }
        return Result.success(null);
    }

    @Override
    public Result<Boolean> deleteComment(Long id) {
        log.info("Admin 删除评论：id={}", id);
        ForumResponse<Boolean> response = forumFacade.deleteComment(id);
        if (Boolean.TRUE.equals(response.getSuccess())) {
            return Result.success(response.getData());
        }
        return Result.fail(response.getMessage());
    }

    private Map<String, Object> convertToMap(ForumCommentInfo comment) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", comment.getId());
        map.put("postId", comment.getPostId());
        map.put("userId", comment.getUserId());
        map.put("content", comment.getContent());
        map.put("createTime", comment.getCreateTime());
        return map;
    }
}
