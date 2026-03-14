package com.epiprev.business.forum.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.epiprev.common.datasource.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("forum_comment")
public class ForumComment extends BaseEntity {

    private Long postId;

    private Long userId;

    private String username;

    private String content;
}
