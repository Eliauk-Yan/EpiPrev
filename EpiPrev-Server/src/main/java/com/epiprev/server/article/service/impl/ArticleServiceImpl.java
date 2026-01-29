package com.epiprev.server.article.service.impl;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.epiprev.server.article.convert.ArticleConvert;
import com.epiprev.server.article.domain.dto.ArticleListDTO;
import com.epiprev.server.article.domain.entity.Article;
import com.epiprev.server.article.exception.ArticleException;
import com.epiprev.server.article.mapper.ArticleMapper;
import com.epiprev.server.article.service.ArticleService;
import com.epiprev.server.article.domain.vo.ArticleVO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.epiprev.server.article.exception.ArticleErrorCode.ARTICLE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    private final ArticleMapper articleMapper;

    private final ArticleConvert articleConvert;

    @Override
    @Cached(name = "article:list:", key = "#dto.current + '-' + #dto.size + '-' + #dto.word + '-' + #dto.type", expire = 3600, timeUnit = TimeUnit.SECONDS, cacheType = CacheType.BOTH)
    public Page<ArticleVO> getList(ArticleListDTO dto) {
        // 1. 获取分页对象
        Page<Article> articlePage = new Page<>(dto.getCurrent(), dto.getSize());
        // 2. 构建查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<Article>()
                .like(StringUtils.isNoneBlank(dto.getWord()), Article::getTitle, dto.getWord())
                .eq(dto.getType() != null, Article::getType, dto.getType())
                .orderByDesc(Article::getCreateTime);
        // 3. 获取分页数据
        IPage<Article> articleIPage = articleMapper.selectPage(articlePage, queryWrapper);
        // 4. 转换为vo page
        Page<ArticleVO> articleVOPage = new Page<>();
        articleVOPage.setRecords(articleConvert.toVOs(articleIPage.getRecords()));
        articleVOPage.setTotal(articleIPage.getTotal());
        articleVOPage.setSize(articleIPage.getSize());
        articleVOPage.setCurrent(articleIPage.getCurrent());
        // 5. 返回结果
        return articleVOPage;
    }

    @Override
    @Cached(name = "article:detail:", key = "#id", expire = 3600, timeUnit = TimeUnit.SECONDS, cacheType = CacheType.BOTH)
    public ArticleVO getDetail(Long id) {
        Article article = getById(id);
        if (article == null) {
            throw new ArticleException(ARTICLE_NOT_FOUND);
        }
        return articleConvert.toVO(article);
    }
}
