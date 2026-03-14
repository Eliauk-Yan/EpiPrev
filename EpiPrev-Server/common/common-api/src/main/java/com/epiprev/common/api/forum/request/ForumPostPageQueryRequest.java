package com.epiprev.common.api.forum.request;

import com.epiprev.common.base.request.PageQueryRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 帖子分页查询请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ForumPostPageQueryRequest extends PageQueryRequest {

    /**
     * 帖子标题
     */
    private String title;

}