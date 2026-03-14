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
import com.epiprev.common.api.user.response.data.UserInfo;
import com.epiprev.common.web.result.MultiResult;
import com.epiprev.common.web.result.Result;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/forum")
public class ForumController {

    private final ForumPostService postService;

    private final ForumCommentService commentService;

    @GetMapping("/post/list")
    public MultiResult<PostVO> list(@RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String word) {
        LambdaQueryWrapper<ForumPost> queryWrapper = new LambdaQueryWrapper<ForumPost>()
                .orderByDesc(ForumPost::getCreateTime);
        if (word != null && !word.isEmpty()) {
            queryWrapper.like(ForumPost::getTitle, word);
        }
        Page<ForumPost> postPage = postService.page(new Page<>(page, size), queryWrapper);

        List<PostVO> postVOs = postPage.getRecords().stream().map(item -> {
            PostVO postVO = new PostVO();
            postVO.setId(item.getId());
            postVO.setAuthorId(item.getUserId());
            postVO.setAuthorName(item.getUsername());
            postVO.setTitle(item.getTitle());
            postVO.setContent(item.getContent());
            postVO.setViews(item.getViews());
            postVO.setCreateTime(item.getCreateTime());
            long count = commentService
                    .count(new LambdaQueryWrapper<ForumComment>().eq(ForumComment::getPostId, item.getId()));
            postVO.setReplyCount((int) count);
            return postVO;
        }).toList();

        return MultiResult.multiSuccess(postVOs, postPage.getTotal(), page, size);
    }

    @GetMapping("/post/detail/{id}")
    public Result<PostVO> detail(@PathVariable Long id) {
        ForumPost post = postService.getById(id);
        if (post == null) {
            return Result.success(null);
        }

        post.setViews((post.getViews() != null ? post.getViews() : 0) + 1);
        postService.updateById(post);

        PostVO postVO = new PostVO();
        postVO.setId(post.getId());
        postVO.setAuthorId(post.getUserId());
        postVO.setAuthorName(post.getUsername());
        postVO.setTitle(post.getTitle());
        postVO.setContent(post.getContent());
        postVO.setViews(post.getViews());
        postVO.setCreateTime(post.getCreateTime());

        List<ForumComment> comments = commentService.list(new LambdaQueryWrapper<ForumComment>()
                .eq(ForumComment::getPostId, id)
                .orderByDesc(ForumComment::getCreateTime));

        List<CommentVO> commentVOs = comments.stream().map(item -> {
            CommentVO commentVO = new CommentVO();
            commentVO.setId(item.getId());
            commentVO.setPostId(item.getPostId());
            commentVO.setAuthorId(item.getUserId());
            commentVO.setAuthorName(item.getUsername());
            commentVO.setContent(item.getContent());
            commentVO.setCreateTime(item.getCreateTime());
            return commentVO;
        }).toList();
        postVO.setComments(commentVOs);
        postVO.setReplyCount(comments.size());

        return Result.success(postVO);
    }

    @PostMapping("/post/publish")
    public Result<Boolean> publishPost(@RequestParam String title, @RequestParam String content) {
        Long userId = StpUtil.getLoginIdAsLong();
        ForumPost post = new ForumPost();
        post.setUserId(userId);
        post.setUsername(getCurrentNickName(userId));
        post.setTitle(title);
        post.setContent(content);
        post.setViews(0);
        return Result.success(postService.save(post));
    }

    @PostMapping("/comment/publish")
    public Result<Boolean> publishComment(@RequestParam Long postId, @RequestParam String content) {
        Long userId = StpUtil.getLoginIdAsLong();
        ForumComment comment = new ForumComment();
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setUsername(getCurrentNickName(userId));
        comment.setContent(content);
        return Result.success(commentService.save(comment));
    }

    /**
     * 查询当前用户发布的帖子列表（我的帖子）
     */
    @GetMapping("/post/my")
    public MultiResult<PostVO> myPosts(@RequestParam(defaultValue = "1") Integer page,
                                      @RequestParam(defaultValue = "10") Integer size,
                                      @RequestParam(required = false) String word) {
        Long userId = StpUtil.getLoginIdAsLong();
        LambdaQueryWrapper<ForumPost> queryWrapper = new LambdaQueryWrapper<ForumPost>()
                .eq(ForumPost::getUserId, userId)
                .orderByDesc(ForumPost::getCreateTime);
        if (word != null && !word.isEmpty()) {
            queryWrapper.like(ForumPost::getTitle, word);
        }
        Page<ForumPost> postPage = postService.page(new Page<>(page, size), queryWrapper);

        List<PostVO> postVOs = postPage.getRecords().stream().map(item -> {
            PostVO postVO = new PostVO();
            postVO.setId(item.getId());
            postVO.setAuthorId(item.getUserId());
            postVO.setAuthorName(item.getUsername());
            postVO.setTitle(item.getTitle());
            postVO.setContent(item.getContent());
            postVO.setViews(item.getViews());
            postVO.setCreateTime(item.getCreateTime());
            long count = commentService
                    .count(new LambdaQueryWrapper<ForumComment>().eq(ForumComment::getPostId, item.getId()));
            postVO.setReplyCount((int) count);
            return postVO;
        }).toList();

        return MultiResult.multiSuccess(postVOs, postPage.getTotal(), page, size);
    }

    private String getCurrentNickName(Long userId) {
        try {
            UserInfo userInfo = StpUtil.getSession().getModel("userInfo", UserInfo.class);
            if (userInfo != null && StringUtils.isNotBlank(userInfo.getNickName())) {
                return userInfo.getNickName();
            }
        } catch (Exception ignored) {
            // ignore and fallback
        }
        return "用户" + userId;
    }
}
