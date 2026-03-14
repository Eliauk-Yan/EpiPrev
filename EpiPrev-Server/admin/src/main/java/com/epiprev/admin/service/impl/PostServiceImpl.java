package com.epiprev.admin.service.impl;

import com.epiprev.admin.service.PostService;
import com.epiprev.common.api.forum.ForumFacade;
import com.epiprev.common.api.forum.request.ForumPostPageQueryRequest;
import com.epiprev.common.api.forum.response.ForumPostInfo;
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
 * 帖子管理服务实现类
 */
@Slf4j
@Service
public class PostServiceImpl implements PostService {

    @DubboReference
    private ForumFacade forumFacade;

    @Override
    public MultiResult<Map<String, Object>> pagePost(Integer current, Integer size, String title) {
        log.info("Admin 分页查询帖子：current={}, size={}, title={}", current, size, title);
        ForumPostPageQueryRequest request = new ForumPostPageQueryRequest();
        request.setCurrent(current);
        request.setSize(size);
        request.setTitle(title);

        ForumResponse<PageResult<ForumPostInfo>> response = forumFacade.pagePost(request);
        if (Boolean.TRUE.equals(response.getSuccess()) && response.getData() != null) {
            PageResult<ForumPostInfo> pageResult = response.getData();
            List<Map<String, Object>> list = pageResult.getList().stream()
                    .map(this::convertToMap)
                    .collect(Collectors.toList());
            return MultiResult.multiSuccess(list, pageResult.getTotal(), current, size);
        }
        return MultiResult.multiSuccess(Collections.emptyList(), 0, current, size);
    }

    
    @Override
    public Result<Boolean> deletePost(Long id) {
        log.info("Admin 删除帖子：id={}", id);
        ForumResponse<Boolean> response = forumFacade.deletePost(id);
        if (Boolean.TRUE.equals(response.getSuccess())) {
            return Result.success(response.getData());
        }
        return Result.fail(response.getMessage());
    }

    private Map<String, Object> convertToMap(ForumPostInfo info) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", info.getId());
        map.put("title", info.getTitle());
        map.put("content", info.getContent());
        map.put("userId", info.getUserId());
        map.put("username", info.getUsername());
        map.put("views", info.getViews());
        map.put("createTime", info.getCreateTime());
        map.put("updateTime", info.getUpdateTime());
        return map;
    }
}
