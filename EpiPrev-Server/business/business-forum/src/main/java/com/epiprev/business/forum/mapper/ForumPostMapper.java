package com.epiprev.business.forum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.epiprev.business.forum.domain.entity.ForumPost;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ForumPostMapper extends BaseMapper<ForumPost> {
}
