package com.epiprev.business.forum.interfaces.facade;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.epiprev.business.forum.domain.entity.ForumComment;
import com.epiprev.business.forum.domain.entity.ForumPost;
import com.epiprev.business.forum.service.ForumCommentService;
import com.epiprev.business.forum.service.ForumPostService;
import com.epiprev.common.api.forum.ForumFacade;
import com.epiprev.common.api.forum.request.ForumCommentPageQueryRequest;
import com.epiprev.common.api.forum.request.ForumPostPageQueryRequest;
import com.epiprev.common.api.forum.response.ForumCommentInfo;
import com.epiprev.common.api.forum.response.ForumPostInfo;
import com.epiprev.common.api.forum.response.ForumResponse;
import com.epiprev.common.base.response.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 论坛服务 Dubbo RPC 接口实现类
 */
@Slf4j
@Component
@DubboService
@RequiredArgsConstructor
public class ForumFacadeImpl implements ForumFacade {

    private final ForumPostService forumPostService;
    private final ForumCommentService forumCommentService;

    @Override
    public ForumResponse<PageResult<ForumPostInfo>> pagePost(ForumPostPageQueryRequest request) {
        log.info("分页查询帖子: request={}", request);
        // 构建分页对象
        Page<ForumPost> page = new Page<>(request.getCurrent(), request.getSize());
        // 构建查询条件
        LambdaQueryWrapper<ForumPost> queryWrapper = new LambdaQueryWrapper<>();
        if (request.getTitle() != null && !request.getTitle().isEmpty()) {
            queryWrapper.like(ForumPost::getTitle, request.getTitle());
        }
        // 执行查询
        IPage<ForumPost> pageResult = forumPostService.page(page, queryWrapper);
        // 转换为响应对象
        List<ForumPostInfo> records = pageResult.getRecords().stream()
                .map(item -> {
                    ForumPostInfo forumPostInfo = new ForumPostInfo();
                    BeanUtils.copyProperties(item, forumPostInfo);
                    return forumPostInfo;
                })
                .collect(Collectors.toList());

        PageResult<ForumPostInfo> pageInfo = new PageResult<>(records, pageResult.getTotal());
        return ForumResponse.success(pageInfo);
    }

    @Override
    public ForumResponse<Boolean> deletePost(Long id) {
        log.info("删除帖子: id={}", id);
        boolean success = forumPostService.removeById(id);
        ForumResponse<Boolean> forumResponse = new ForumResponse<>();
        if (!success) {
            forumResponse.setSuccess(false);
            forumResponse.setMessage("删除帖子失败");
        }
        forumResponse.setSuccess(true);
        return forumResponse;
    }

    @Override
    public ForumResponse<PageResult<ForumCommentInfo>> pageComment(ForumCommentPageQueryRequest request) {
        log.info("分页查询评论: request={}", request);
        // 构建分页对象
        Page<ForumComment> page = new Page<>(request.getCurrent(), request.getSize());
        // 构建查询条件
        LambdaQueryWrapper<ForumComment> queryWrapper = new LambdaQueryWrapper<>();
        if (request.getContent() != null && !request.getContent().isEmpty()) {
            queryWrapper.like(ForumComment::getContent, request.getContent());
        }
        // 执行查询
        IPage<ForumComment> pageResult = forumCommentService.page(page, queryWrapper);
        // 转换为响应对象
        List<ForumCommentInfo> records = pageResult.getRecords().stream()
                .map(item -> {
                    ForumCommentInfo forumCommentInfo = new ForumCommentInfo();
                    BeanUtils.copyProperties(item, forumCommentInfo);
                    return forumCommentInfo;
                })
                .collect(Collectors.toList());
        PageResult<ForumCommentInfo> pageInfo = new PageResult<>(records, pageResult.getTotal());
        return ForumResponse.success(pageInfo);
    }

    @Override
    public ForumResponse<ForumCommentInfo> getCommentById(Long id) {
        log.info("查询评论详情: id={}", id);
        ForumComment comment = forumCommentService.getById(id);
        if (comment == null) {
            return ForumResponse.fail("评论不存在");
        }
        ForumCommentInfo info = new ForumCommentInfo();
        BeanUtils.copyProperties(comment, info);
        return ForumResponse.success(info);
    }

    @Override
    public ForumResponse<Boolean> deleteComment(Long id) {
        log.info("删除评论: id={}", id);
        boolean success = forumCommentService.removeById(id);
        if (!success) {
            return ForumResponse.fail("删除评论失败");
        }
        return ForumResponse.success(true);
    }

}