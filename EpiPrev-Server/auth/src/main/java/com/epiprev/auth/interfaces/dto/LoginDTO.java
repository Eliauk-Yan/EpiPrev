package com.epiprev.auth.interfaces.dto;

import lombok.Data;

@Data
public class LoginDTO {

    private String username;

    private String password;

    private Boolean rememberMe;

}
