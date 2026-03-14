package com.epiprev.common.api.news.response;

import com.epiprev.common.base.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * 新闻服务统一响应
 */
@Getter
@Setter
public class NewsResponse<T> extends BaseResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    private T data;
}
