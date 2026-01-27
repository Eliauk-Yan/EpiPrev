package com.epiprev.server.forum.entity;
import com.epiprev.server.common.entity.BaseEntity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("forum_post")
public class ForumPost extends BaseEntity {

    private Long userId;

    private String username;

    private String title;

    private String content;

    private Integer views;

    @TableField(exist = false)
    private List<ForumComment> comments;
}


