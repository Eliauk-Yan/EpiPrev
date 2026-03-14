package com.epiprev.auth.service;

import com.epiprev.auth.interfaces.dto.LoginDTO;
import com.epiprev.auth.interfaces.dto.RegisterDTO;
import com.epiprev.auth.interfaces.vo.LoginVO;

public interface AuthService {

    LoginVO login(LoginDTO loginDTO);

    LoginVO loginAdmin(LoginDTO loginDTO);

    void register(RegisterDTO registerDTO);
}
