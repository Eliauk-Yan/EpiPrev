package com.epiprev.business.user.service.impl;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheManager;
import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.alicp.jetcache.template.QuickConfig;
import com.epiprev.business.user.domain.entity.User;
import com.epiprev.business.user.exception.UserException;
import com.epiprev.business.user.mapper.UserMapper;
import com.epiprev.common.base.exception.BusinessException;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import static com.epiprev.business.user.exception.UserErrorCode.USER_UPDATE_FAILED;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCacheService {

    /**
     * 用户Mapper
     */
    private final UserMapper userMapper;

    /**
     * 用户延迟双删 线程工厂
     */
    private static final ThreadFactory userCacheDelayProcessFactory = new ThreadFactoryBuilder()
            .setNameFormat("user-cache-delay-delete-pool-%d").build();

    /**
     * 用户延迟双删 线程池
     */
    private final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(10, userCacheDelayProcessFactory);

    /**
     * 缓存管理器
     */
    private final CacheManager cacheManager;

    /**
     * 通过用户ID对用户信息做的缓存
     */
    private Cache<String, User> userCacheById;

    /**
     * JetCache 编程式缓存初始化
     */
    @PostConstruct
    public void init() {
        QuickConfig idQc = QuickConfig.newBuilder(":user:cache:id:")
                .cacheType(CacheType.BOTH)
                .expire(Duration.ofHours(2))
                .syncLocal(true)
                .build();
        userCacheById = cacheManager.getOrCreateCache(idQc);
    }

    @Cached(name = ":user:cache:id:", cacheType = CacheType.BOTH, key = "#userId", cacheNullValue = true)
    @CacheRefresh(refresh = 60, timeUnit = TimeUnit.MINUTES) // 每60分钟刷新
    public User getUserById(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户未找到");
        }
        return user;
    }

    @CacheInvalidate(name = ":user:cache:id:", key = "#user.id")
    public Boolean updateUserById(User user) {
        int row = userMapper.updateById(user);
        if (row <= 0) {
            throw new BusinessException("用户信息更新失败");
        }
        return true;
    }


    public Boolean updateUserByDelayDelete(User user) {
        // 1. 第一次删除
        userCacheById.remove(user.getId().toString());
        // 2. 更新用户
        int row = userMapper.updateById(user);
        if (row != 1) {
            throw new UserException(USER_UPDATE_FAILED);
        }
        // 3. 第二次删除缓存
        scheduler.schedule(() -> {
            boolean idDeleteResult = userCacheById.remove(user.getId().toString());
            log.info("用户延迟双删, key = {} , result  = {}", user.getId(), idDeleteResult);
        }, 2, TimeUnit.SECONDS);
        return true;
    }
}
