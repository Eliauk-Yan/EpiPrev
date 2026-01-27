package com.epiprev.server.forum.entity;
import com.epiprev.server.common.entity.BaseEntity;

import com.baomidou.mybatisplus.annotation.TableName;
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


