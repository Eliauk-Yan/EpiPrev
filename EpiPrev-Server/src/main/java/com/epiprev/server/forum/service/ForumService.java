package com.epiprev.server.forum.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.epiprev.server.forum.dto.CommentDTO;
import com.epiprev.server.forum.dto.PostDTO;
import com.epiprev.server.forum.entity.ForumPost;
import com.epiprev.server.forum.vo.PostVO;

public interface ForumService extends IService<ForumPost> {

    Page<PostVO> getList(int page, int size);

    void createPost(PostDTO postDTO);

    void createComment(CommentDTO commentDTO);
}


