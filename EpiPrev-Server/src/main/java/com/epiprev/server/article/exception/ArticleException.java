package com.epiprev.server.article.exception;

import com.epiprev.server.common.exception.BusinessException;
import com.epiprev.server.common.exception.code.ErrorCode;

/**
 * @classname ArticleException
 * @description 文章模块异常
 * @date 2026/01/28 00:57
 */
public class ArticleException extends BusinessException {

    public ArticleException(ErrorCode errorCode) {
        super(errorCode);
    }
}
