package com.epiprev.common.web.result;

import com.epiprev.common.web.result.enums.ResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @classname MultiResult
 * @description 多结果统一返回类型
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MultiResult<T> extends Result<List<T>> {

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
        return new MultiResult<>(true, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data, total, page,
                size);
    }

    public static <T> MultiResult<T> multiError(ResultCode resultCode) {
        return new MultiResult<>(true, resultCode.getCode(), resultCode.getMessage(), null, 0, 0, 0);
    }

    public static <T> MultiResult<T> multiFail(String message) {
        return new MultiResult<>(false, ResultCode.FAIL.getCode(), message, null, 0, 0, 0);
    }
}
