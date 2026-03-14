package com.epiprev.business.forum.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.epiprev.business.forum.domain.entity.ForumPost;
import com.epiprev.business.forum.mapper.ForumPostMapper;
import com.epiprev.business.forum.service.ForumPostService;
import org.springframework.stereotype.Service;

@Service
public class ForumPostServiceImpl extends ServiceImpl<ForumPostMapper, ForumPost> implements ForumPostService {
}
