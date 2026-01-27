package com.epiprev.server.knowledge.entity;
import com.epiprev.server.common.entity.BaseEntity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("knowledge_article")
public class KnowledgeArticle extends BaseEntity {

    private String title;

    private String category;

    private String summary;

    private String content;

    private Integer views;

}


