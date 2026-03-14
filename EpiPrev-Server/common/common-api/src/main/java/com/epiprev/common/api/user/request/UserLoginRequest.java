package com.epiprev.common.api.user.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class UserLoginRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String username;

    private String password;
}
