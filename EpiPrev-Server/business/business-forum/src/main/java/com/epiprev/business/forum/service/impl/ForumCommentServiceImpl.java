package com.epiprev.business.forum.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.epiprev.business.forum.domain.entity.ForumComment;
import com.epiprev.business.forum.mapper.ForumCommentMapper;
import com.epiprev.business.forum.service.ForumCommentService;
import org.springframework.stereotype.Service;

@Service
public class ForumCommentServiceImpl extends ServiceImpl<ForumCommentMapper, ForumComment>
        implements ForumCommentService {
}
