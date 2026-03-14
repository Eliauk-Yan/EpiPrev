package com.epiprev.business.forum.interfaces.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.epiprev.business.forum.domain.entity.ForumComment;
import com.epiprev.business.forum.domain.entity.ForumPost;
import com.epiprev.business.forum.domain.vo.CommentVO;
import com.epiprev.business.forum.domain.vo.PostVO;
import com.epiprev.business.forum.service.ForumCommentService;
import com.epiprev.business.forum.service.ForumPostService;
import com.epiprev.common.web.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/forum")
public class ForumController {

    private final ForumPostService postService;

    private final ForumCommentService commentService;

    /**
     * 分页查询帖子列表
     */
    @GetMapping("/post/list")
    public Result<List<PostVO>> list(@RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<ForumPost> postPage = postService.page(new Page<>(page, size),
                new LambdaQueryWrapper<ForumPost>().orderByDesc(ForumPost::getCreateTime));

        List<PostVO> postVOs = postPage.getRecords().stream().map(item -> {
            PostVO postVO = new PostVO();
            BeanUtils.copyProperties(item, postVO);
            return postVO;
        }).toList();
        // 设置回复数
        for (PostVO vo : postVOs) {
            long count = commentService
                    .count(new LambdaQueryWrapper<ForumComment>().eq(ForumComment::getPostId, vo.getId()));
            vo.setReplyCount((int) count);
        }
        return Result.success(postVOs);
    }

    /**
     * 获取帖子详情及评论
     */
    @GetMapping("/post/detail/{id}")
    public Result<PostVO> detail(@PathVariable Long id) {
        ForumPost post = postService.getById(id);
        if (post == null) {
            return Result.success(null);
        }

        // 增加阅读量
        post.setViews(post.getViews() + 1);
        postService.updateById(post);

        PostVO postVO = new PostVO();
        BeanUtils.copyProperties(post, postVO);
        List<ForumComment> comments = commentService.list(new LambdaQueryWrapper<ForumComment>()
                .eq(ForumComment::getPostId, id)
                .orderByDesc(ForumComment::getCreateTime));

        List<CommentVO> commentVOs = comments.stream().map(item -> {
            CommentVO commentVO = new CommentVO();
            BeanUtils.copyProperties(item, commentVO);
            return commentVO;
        }).toList();
        postVO.setComments(commentVOs);
        postVO.setReplyCount(comments.size());

        return Result.success(postVO);
    }

    /**
     * 发布帖子
     */
    @PostMapping("/post/publish")
    // @RateLimit(key = "'publish_post:' + #title", limit = 1, windowSize = 60, message = "发布帖子过于频繁，请稍后再试")
    public Result<Boolean> publishPost(@RequestParam String title, @RequestParam String content) {
        Long userId = StpUtil.getLoginIdAsLong();
        // 这里简化处理，实际中可能需要通过 RPC 获取用户名
        ForumPost post = new ForumPost();
        post.setUserId(userId);
        post.setUsername("用户" + userId); // 简化
        post.setTitle(title);
        post.setContent(content);
        post.setViews(0);
        return Result.success(postService.save(post));
    }

    /**
     * 发表评论
     */
    @PostMapping("/comment/publish")
    // @RateLimit(key = "'publish_comment:' + #postId", limit = 1, windowSize = 10, message = "评论过于频繁，请稍后再试")
    public Result<Boolean> publishComment(@RequestParam Long postId, @RequestParam String content) {
        Long userId = StpUtil.getLoginIdAsLong();
        ForumComment comment = new ForumComment();
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setUsername("用户" + userId); // 简化
        comment.setContent(content);
        return Result.success(commentService.save(comment));
    }
}
