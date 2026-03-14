package com.epiprev.common.api.user.response;

import com.epiprev.common.base.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
public class UserResponse<T> extends BaseResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    private T data;
}
