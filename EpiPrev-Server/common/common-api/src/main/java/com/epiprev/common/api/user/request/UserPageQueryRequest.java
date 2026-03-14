package com.epiprev.common.api.user.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户分页查询请求
 */
@Data
public class UserPageQueryRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 页码
     */
    private Integer page;

    /**
     * 每页大小
     */
    private Integer size;

    /**
     * 搜索关键字
     */
    private String keyword;

    /**
     * 用户角色
     */
    private String role;

    /**
     * 用户状态
     */
    private String state;
}
