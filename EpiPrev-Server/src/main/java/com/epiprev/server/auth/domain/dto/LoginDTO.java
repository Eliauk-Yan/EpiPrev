package com.epiprev.server.auth.domain.dto;

import lombok.Data;

@Data
public class LoginDTO {

    private String username;

    private String password;

    private Boolean rememberMe;
}
