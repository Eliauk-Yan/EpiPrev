package com.epiprev.server.article.domain.entity;

import com.epiprev.server.article.constant.enums.ArticleType;
import com.epiprev.server.common.entity.BaseEntity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("article")
public class Article extends BaseEntity {

    private String title;

    private String category;

    private String summary;

    private String content;

    private String cover;

    @TableField("video_url")
    private String videoUrl;

    @TableField("type")
    private ArticleType type;

    private Integer views;

}
