package com.epiprev.common.api.user.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserQueryRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String username;

    private String phone;
}
