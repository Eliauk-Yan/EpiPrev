package com.epiprev.server.common.result;

import com.epiprev.server.common.result.enums.ResultCode;

import java.util.List;

/**
 * 为什么不直接Result<Page<T>>而是要在写一个统一返回对象呢？
 * 1. Result<Page<T>>破环了分层规则，接口层不应该暴露持久化层
 * 2. 前端只关心 records / total / page / size，不可控。
 * ...分层解耦，接口稳定，ORM替换成本，前端友好，语义清晰，测试Mock方便
 */
public class MultiResult <T> extends Result<List<T>> {

    private long total;

    private long page;

    private long size;

    public MultiResult() {
        super();
    }

    public MultiResult(Boolean success, String code, String message, List<T> data, long total, long page, long size) {
        super(success, code, message, data);
        this.total = total;
        this.page = page;
        this.size = size;
    }

    public static <T> MultiResult<T> multiSuccess(List<T> data, long total, long page, long size) {
        return new MultiResult<>(true, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data, total, page, size);
    }

    public static <T> MultiResult<T> multiError(ResultCode resultCode) {
        return new MultiResult<>(true, resultCode.getCode(), resultCode.getMessage(), null, 0, 0, 0);
    }
}
