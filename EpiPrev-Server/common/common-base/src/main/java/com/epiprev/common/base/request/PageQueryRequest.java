package com.epiprev.common.base.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分页查询请求基类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageQueryRequest extends BaseRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    private Integer current = 1;

    /**
     * 每页大小
     */
    private Integer size = 10;

}