package com.epiprev.server.forum.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import com.epiprev.server.forum.domain.dto.CommentDTO;
import com.epiprev.server.forum.domain.dto.PostDTO;
import com.epiprev.server.forum.domain.dto.PostListDTO;
import com.epiprev.server.forum.domain.entity.ForumPost;
import com.epiprev.server.forum.domain.vo.PostVO;

public interface ForumService extends IService<ForumPost> {

    /**
     * 获取帖子列表
     * 
     * @param dto 请求参数
     * @return 帖子列表
     */
    Page<PostVO> getList(PostListDTO dto);

    /**
     * 获取热门帖子列表
     * 
     * @param limit 限制
     * @return 热门帖子列表
     */
    List<PostVO> getHotList(int limit);

    /**
     * 发布帖子
     * 
     * @param postDTO 帖子数据
     */
    void createPost(PostDTO postDTO);

    /**
     * 评论
     * 
     * @param commentDTO 评论数据
     */
    void createComment(CommentDTO commentDTO);

    /**
     * 获取帖子详情
     * 
     * @param postId 帖子ID
     * @return 帖子详情
     */
    PostVO getDetail(Long postId);
}
