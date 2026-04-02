package com.epiprev.business.user.service;

import com.epiprev.business.user.interfaces.controller.params.RealNameAuthParam;

public interface UserAuthService {

    /**
     * 用户实名认证
     */
    Boolean realNameAuth(RealNameAuthParam param);
}
