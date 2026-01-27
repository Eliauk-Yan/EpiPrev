package com.epiprev.server.knowledge.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.epiprev.server.knowledge.entity.KnowledgeArticle;
import com.epiprev.server.knowledge.vo.ArticleVO;

public interface KnowledgeService extends IService<KnowledgeArticle> {
    Page<ArticleVO> getList(int page, int size, String category);

    ArticleVO getDetail(Long id);
}


