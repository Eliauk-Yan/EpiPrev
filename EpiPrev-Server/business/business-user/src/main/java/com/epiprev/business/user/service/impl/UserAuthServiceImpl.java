package com.epiprev.business.user.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.epiprev.business.user.api.response.RealNameAuthResponse;
import com.epiprev.business.user.exception.UserErrorCode;
import com.epiprev.business.user.exception.UserException;
import com.epiprev.business.user.interfaces.controller.params.RealNameAuthParam;
import com.epiprev.business.user.service.UserAuthService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix = "epiprev.mock", name = "enable", havingValue = "true")
public class UserAuthServiceImpl implements UserAuthService {

    @Override
    public Boolean realNameAuth(RealNameAuthParam param) {

        String url = "https://kzidcardv1.market.alicloudapi.com/api-mall/api/id_card/check";
        // 1. 构建请求
        HttpRequest request = HttpRequest.post(url)
                .header("Authorization", "APPCODE 65b1e4df85b5405d92fb427d6e44d452")
                .header("Content-Type", "application/x-www-form-urlencoded");
        // 2. 表单参数
        request.form("name", param.getRealName());
        request.form("idcard", param.getIdCard());
        // 3. 发送请求
        try (HttpResponse response = request.execute()) {
            String responseJson = response.body();
            RealNameAuthResponse resp = JSON.parseObject(responseJson, RealNameAuthResponse.class);
            if (!resp.getSuccess()) {
                throw new UserException(UserErrorCode.REAL_NAME_AUTH_SERVICE_ERROR);
            }
            return resp.getCode() == 200;
        }
    }
}
