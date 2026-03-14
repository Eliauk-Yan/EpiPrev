package com.epiprev.common.api.forum;

import com.epiprev.common.api.forum.request.ForumCommentPageQueryRequest;
import com.epiprev.common.api.forum.request.ForumPostPageQueryRequest;
import com.epiprev.common.api.forum.response.ForumCommentInfo;
import com.epiprev.common.api.forum.response.ForumPostInfo;
import com.epiprev.common.api.forum.response.ForumResponse;
import com.epiprev.common.base.response.PageResult;

/**
 * 论坛服务 Dubbo RPC 接口
 */
public interface ForumFacade {

    /**
     * 分页查询帖子
     */
    ForumResponse<PageResult<ForumPostInfo>> pagePost(ForumPostPageQueryRequest request);
    /**
     * 删除帖子
     */
    ForumResponse<Boolean> deletePost(Long id);

    /**
     * 分页查询评论
     */
    ForumResponse<PageResult<ForumCommentInfo>> pageComment(ForumCommentPageQueryRequest request);
    /**
     * 根据ID查询评论
     */
    ForumResponse<ForumCommentInfo> getCommentById(Long id);

    /**
     * 删除评论
     */
    ForumResponse<Boolean> deleteComment(Long id);
}