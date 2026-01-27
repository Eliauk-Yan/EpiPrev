package com.epiprev.server.article.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.epiprev.server.article.domain.dto.ArticleListDTO;
import com.epiprev.server.article.domain.entity.Article;
import com.epiprev.server.article.domain.vo.ArticleVO;


public interface ArticleService extends IService<Article> {

    /**
     * 获取分页文章列表
     * @param dto 分页参数
     * @return 文章列表
     */
    Page<ArticleVO> getList(ArticleListDTO dto);

    /**
     * 获取文章详情
     * @param id 文章id
     * @return 文章详情
     */
    ArticleVO getDetail(Long id);
}
