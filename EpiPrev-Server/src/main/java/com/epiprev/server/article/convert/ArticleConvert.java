package com.epiprev.server.article.convert;

import com.epiprev.server.article.domain.entity.Article;
import com.epiprev.server.article.domain.vo.ArticleVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @classname ArticleConvert
 * @description 文章转换器
 * @date 2026/01/28 00:51
 */
@Mapper(componentModel = "spring")
public interface ArticleConvert {

    ArticleVO toVO(Article article);

    List<ArticleVO> toVOs(List<Article> articles);

}
