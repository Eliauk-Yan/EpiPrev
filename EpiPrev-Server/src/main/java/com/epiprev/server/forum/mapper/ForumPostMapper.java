package com.epiprev.server.forum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.epiprev.server.forum.domain.entity.ForumPost;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ForumPostMapper extends BaseMapper<ForumPost> {
}


