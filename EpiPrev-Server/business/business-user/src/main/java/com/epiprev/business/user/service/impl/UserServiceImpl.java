package com.epiprev.business.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.epiprev.business.user.domain.entity.User;
import com.epiprev.business.user.domain.entity.UserOperateStream;
import com.epiprev.business.user.exception.UserException;
import com.epiprev.business.user.mapper.UserMapper;
import com.epiprev.business.user.mapper.UserOperateStreamMapper;
import com.epiprev.business.user.service.UserService;
import com.epiprev.common.api.user.constant.UserState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.epiprev.business.user.exception.UserErrorCode.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 用户缓存服务
     */
    private final UserCacheService userCacheService;

    private final UserOperateStreamMapper operateStreamMapper;

    @Override
    public User getUserById(Long userId) {
        return userCacheService.getUserById(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateUserById(User user) {
        int insertRow = operateStreamMapper.insert(new UserOperateStream(user.getId(), "信息修改", LocalDateTime.now(), JSON.toJSONString(user), ""));
        if (insertRow != 1) {
            throw  new UserException(USER_OPERATE_STREAM_INSERT_FAILED);
        }
        return userCacheService.updateUserById(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateState(Long id, UserState userState) {
        // 1. 查询用户
        User user = userCacheService.getUserById(id);
        // 2. 验证用户
        if (user == null) {
            throw new UserException(USER_NOT_FOUND);
        }
        // 3. 操作
        int operateRow = -1;
        Boolean delayDeleteResult = false;
        if (user.getState() == UserState.AUTH && userState == UserState.FROZEN) {
            // 4. 用户冻结
            user.setState(userState);
            delayDeleteResult = userCacheService.updateUserByDelayDelete(user);
            operateRow = operateStreamMapper.insert(new UserOperateStream(id, "用户冻结", LocalDateTime.now(), JSON.toJSONString(user), ""));
        } else if (user.getState() == UserState.FROZEN && userState == UserState.AUTH) {
            // 5. 用户解冻
            user.setState(userState);
            delayDeleteResult = userCacheService.updateUserByDelayDelete(user);
            operateRow = operateStreamMapper.insert(new UserOperateStream(id, "用户解冻", LocalDateTime.now(), JSON.toJSONString(user), ""));
        } else {
            throw new UserException(ILLEGAL_USER_STATE);
        }
        if (operateRow != 1 || !delayDeleteResult) {
            throw new  UserException(USER_UPDATE_FAILED);
        }
        return true;
    }


}
