package com.epiprev.business.forum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.epiprev.business.forum.domain.entity.ForumComment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ForumCommentMapper extends BaseMapper<ForumComment> {
}
