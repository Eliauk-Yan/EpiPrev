package com.epiprev.common.api.forum.response;

import com.epiprev.common.base.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * 论坛服务响应类
 */
@Getter
@Setter
public class ForumResponse<T> extends BaseResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    private T data;

    public static <T> ForumResponse<T> success(T data) {
        ForumResponse<T> response = new ForumResponse<>();
        response.setSuccess(true);
        response.setData(data);
        return response;
    }

    public static <T> ForumResponse<T> fail(String message) {
        ForumResponse<T> response = new ForumResponse<>();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }
}