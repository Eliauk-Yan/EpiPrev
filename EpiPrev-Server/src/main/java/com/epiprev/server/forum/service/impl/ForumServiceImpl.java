package com.epiprev.server.forum.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.epiprev.server.forum.dto.CommentDTO;
import com.epiprev.server.forum.dto.PostDTO;
import com.epiprev.server.forum.entity.ForumComment;
import com.epiprev.server.forum.vo.PostVO;
import com.epiprev.server.forum.entity.ForumPost;
import com.epiprev.server.user.domain.entity.SysUser;
import com.epiprev.server.forum.mapper.ForumCommentMapper;
import com.epiprev.server.forum.mapper.ForumPostMapper;
import com.epiprev.server.forum.service.ForumService;
import com.epiprev.server.user.service.SysUserService;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ForumServiceImpl extends ServiceImpl<ForumPostMapper, ForumPost> implements ForumService {

    @Autowired
    private ForumPostMapper forumPostMapper;
    @Autowired
    private ForumCommentMapper forumCommentMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private RedissonClient redissonClient;

    @Override
    public Page<PostVO> getList(int page, int size) {
        Page<ForumPost> p = new Page<>(page, size);
        QueryWrapper<ForumPost> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        Page<ForumPost> postPage = page(p, wrapper);

        Page<PostVO> result = new Page<>(postPage.getCurrent(), postPage.getSize(), postPage.getTotal());
        List<PostVO> voList = postPage.getRecords().stream().map(post -> {
            PostVO vo = new PostVO();
            BeanUtil.copyProperties(post, vo);
            vo.setAuthorId(post.getUserId());
            vo.setAuthorName(post.getUsername());
            return vo;
        }).toList();
        result.setRecords(voList);

        return result;
    }

    @Override
    @Transactional
    public void createPost(PostDTO postDTO) {
        long userId = StpUtil.getLoginIdAsLong();

        // Rate Limiting: 1 post per 60 seconds per user
        RRateLimiter rateLimiter = redissonClient.getRateLimiter("post_limit_" + userId);
        rateLimiter.trySetRate(RateType.OVERALL, 1, 60, RateIntervalUnit.SECONDS);
        if (!rateLimiter.tryAcquire()) {
            throw new RuntimeException("发帖太频繁，请稍后再试");
        }

        SysUser user = sysUserService.getById(userId);
        ForumPost post = new ForumPost();
        BeanUtil.copyProperties(postDTO, post);
        post.setUserId(userId);
        post.setUsername(user.getUsername());
        post.setCreateTime(LocalDateTime.now());
        save(post);
    }

    @Override
    @Transactional
    public void createComment(CommentDTO commentDTO) {
        long userId = StpUtil.getLoginIdAsLong();

        // Rate Limiting: 5 comments per 60 seconds per user
        RRateLimiter rateLimiter = redissonClient.getRateLimiter("comment_limit_" + userId);
        rateLimiter.trySetRate(RateType.OVERALL, 5, 60, RateIntervalUnit.SECONDS);
        if (!rateLimiter.tryAcquire()) {
            throw new RuntimeException("评论太频繁，请稍后再试");
        }

        SysUser user = sysUserService.getById(userId);
        ForumComment comment = new ForumComment();
        BeanUtil.copyProperties(commentDTO, comment);
        comment.setUserId(userId);
        comment.setUsername(user.getUsername());
        comment.setCreateTime(LocalDateTime.now());
        forumCommentMapper.insert(comment);
    }
}




