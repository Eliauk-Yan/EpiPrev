package com.epiprev.server.knowledge.service.impl;
import com.epiprev.server.common.result.Result;

import cn.hutool.core.bean.BeanUtil;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.epiprev.server.knowledge.entity.KnowledgeArticle;
import com.epiprev.server.knowledge.mapper.KnowledgeArticleMapper;
import com.epiprev.server.knowledge.service.KnowledgeService;
import com.epiprev.server.knowledge.vo.ArticleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Service
public class KnowledgeServiceImpl extends ServiceImpl<KnowledgeArticleMapper, KnowledgeArticle>
        implements KnowledgeService {

    @Autowired
    private KnowledgeArticleMapper knowledgeArticleMapper;

    @Override
    @Cached(name = "knowledgeList:", key = "#category + '-' + #page + '-' + #size", expire = 3600, timeUnit = TimeUnit.SECONDS, cacheType = CacheType.BOTH)
    public Page<ArticleVO> getList(int page, int size, String category) {
        Page<KnowledgeArticle> p = new Page<>(page, size);
        QueryWrapper<KnowledgeArticle> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(category)) {
            wrapper.eq("category", category);
        }
        wrapper.orderByDesc("create_time");
        Page<KnowledgeArticle> articlePage = page(p, wrapper);

        Page<ArticleVO> result = new Page<>(articlePage.getCurrent(), articlePage.getSize(), articlePage.getTotal());
        result.setRecords(BeanUtil.copyToList(articlePage.getRecords(), ArticleVO.class));
        return result;
    }

    @Override
    @Cached(name = "knowledgeDetail:", key = "#id", expire = 3600, timeUnit = TimeUnit.SECONDS, cacheType = CacheType.BOTH)
    public ArticleVO getDetail(Long id) {
        KnowledgeArticle article = getById(id);
        if (article == null) {
            return null;
        }
        ArticleVO vo = new ArticleVO();
        BeanUtil.copyProperties(article, vo);
        return vo;
    }
}



