package com.epiprev.business.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.epiprev.business.article.domain.entity.Article;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
}
