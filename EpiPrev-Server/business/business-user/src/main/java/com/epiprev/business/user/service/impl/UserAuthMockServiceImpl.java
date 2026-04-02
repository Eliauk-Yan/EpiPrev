package com.epiprev.business.user.service.impl;

import com.epiprev.business.user.interfaces.controller.params.RealNameAuthParam;
import com.epiprev.business.user.service.UserAuthService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix = "epiprev.mock", name = "enable", havingValue = "true", matchIfMissing = true)
public class UserAuthMockServiceImpl  implements UserAuthService {
    @Override
    public Boolean realNameAuth(RealNameAuthParam param) {
        return true;
    }
}
