package com.epiprev.server.forum.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.epiprev.server.common.limiter.annotation.RateLimit;
import com.epiprev.server.forum.convert.ForumConvert;
import com.epiprev.server.forum.domain.dto.CommentDTO;
import com.epiprev.server.forum.domain.dto.PostDTO;
import com.epiprev.server.forum.domain.dto.PostListDTO;
import com.epiprev.server.forum.domain.entity.ForumComment;
import com.epiprev.server.forum.domain.vo.PostVO;
import com.epiprev.server.forum.domain.entity.ForumPost;
import com.epiprev.server.forum.exception.ForumException;
import com.epiprev.server.user.domain.dto.UserInfoDTO;
import com.epiprev.server.forum.mapper.ForumCommentMapper;
import com.epiprev.server.forum.mapper.ForumPostMapper;
import com.epiprev.server.forum.service.ForumService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.epiprev.server.forum.exception.ForumErrorCode.COMMENT_CREATE_FAILED;
import static com.epiprev.server.forum.exception.ForumErrorCode.POST_CREATE_FAILED;
import static com.epiprev.server.forum.exception.ForumErrorCode.POST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ForumServiceImpl extends ServiceImpl<ForumPostMapper, ForumPost> implements ForumService {

    private final ForumPostMapper forumPostMapper;

    private final ForumCommentMapper forumCommentMapper;

    private final ForumConvert forumConvert;

    @Override
    public Page<PostVO> getList(PostListDTO dto) {
        // 1. 构造帖子分页
        Page<ForumPost> page = new Page<>(dto.getCurrent(), dto.getSize());
        // 2. 构造查询条件
        LambdaQueryWrapper<ForumPost> queryWrapper = new LambdaQueryWrapper<ForumPost>()
                .like(dto.getWord() != null && !dto.getWord().isEmpty(), ForumPost::getTitle, dto.getWord())
                .orderByDesc(ForumPost::getCreateTime);
        // 3. 查询帖子分页数据
        Page<ForumPost> postPage = this.page(page, queryWrapper);
        // 4. 构造帖子分页结果
        Page<PostVO> result = new Page<>(postPage.getCurrent(), postPage.getSize(), postPage.getTotal());
        // 5. 转换并设置评论数
        List<PostVO> postVOs = forumConvert.toPostVOs(postPage.getRecords());
        postVOs.forEach(vo -> {
            Long count = forumCommentMapper
                    .selectCount(new LambdaQueryWrapper<ForumComment>().eq(ForumComment::getPostId, vo.getId()));
            vo.setReplies(count.intValue());
        });
        result.setRecords(postVOs);
        // 6. 返回帖子分页结果
        return result;
    }

    @Override
    public List<PostVO> getHotList(int limit) {
        // 1. 查询热门帖子
        List<ForumPost> posts = list(
                new LambdaQueryWrapper<ForumPost>().orderByDesc(ForumPost::getViews).last("LIMIT " + limit));
        // 2. 转换并设置评论数
        List<PostVO> postVOs = forumConvert.toPostVOs(posts);
        postVOs.forEach(vo -> {
            Long count = forumCommentMapper
                    .selectCount(new LambdaQueryWrapper<ForumComment>().eq(ForumComment::getPostId, vo.getId()));
            vo.setReplies(count.intValue());
        });
        return postVOs;
    }

    @Override
    @Transactional
    @RateLimit(key = "'forum:post:' + T(cn.dev33.satoken.stp.StpUtil).getLoginIdAsLong()", limit = 5, windowSize = 60, message = "发帖太频繁，请稍后再试")
    public void createPost(PostDTO postDTO) {
        // 1. 获取当前用户信息
        long userId = StpUtil.getLoginIdAsLong();
        UserInfoDTO userInfo = StpUtil.getSession().getModel("userInfo", UserInfoDTO.class);
        // 2. 转换实体类
        ForumPost post = forumConvert.toEntity(postDTO);
        // 3. 设置信息
        post.setUserId(userId);
        post.setUsername(userInfo.getUsername());
        post.setCreateTime(LocalDateTime.now());
        post.setViews(0);
        // 4. 保存帖子
        boolean saveRes = save(post);
        if (!saveRes) {
            throw new ForumException(POST_CREATE_FAILED);
        }
    }

    @Override
    @Transactional
    @RateLimit(key = "'forum:comment:' + T(cn.dev33.satoken.stp.StpUtil).getLoginIdAsLong()", limit = 5, windowSize = 60, message = "评论太频繁，请稍后再试")
    public void createComment(CommentDTO commentDTO) {
        // 1. 获取当前用户信息
        long userId = StpUtil.getLoginIdAsLong();
        UserInfoDTO userInfo = StpUtil.getSession().getModel("userInfo", UserInfoDTO.class);
        // 2. 转换实体类
        ForumComment comment = forumConvert.toEntity(commentDTO);
        // 3. 设置信息
        comment.setUserId(userId);
        comment.setUsername(userInfo.getUsername());
        comment.setCreateTime(LocalDateTime.now());
        // 4. 保存评论
        int row = forumCommentMapper.insert(comment);
        if (row != 1) {
            throw new ForumException(COMMENT_CREATE_FAILED);
        }
    }

    @Override
    public PostVO getDetail(Long postId) {
        // 1. 查询帖子
        ForumPost post = getById(postId);
        if (post == null) {
            throw new ForumException(POST_NOT_FOUND);
        }
        // 2. 转换为VO
        PostVO postVO = forumConvert.toPostVO(post);
        // 3. 查询评论列表
        List<ForumComment> comments = forumCommentMapper.selectList(
                new LambdaQueryWrapper<ForumComment>()
                        .eq(ForumComment::getPostId, postId)
                        .orderByDesc(ForumComment::getCreateTime));
        // 4. 设置评论列表和评论数
        postVO.setComments(forumConvert.toCommentVOs(comments));
        postVO.setReplies(comments.size());
        // 5. 更新浏览量（简单+1）
        post.setViews((post.getViews() == null ? 0 : post.getViews()) + 1);
        forumPostMapper.updateById(post);
        return postVO;
    }
}
