package com.epiprev.server.common.limiter.aspect;

import com.epiprev.server.common.limiter.annotation.RateLimit;
import com.epiprev.server.common.limiter.exception.LimiterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static com.epiprev.server.common.limiter.constant.LimiterConstant.LIMIT_KEY_PREFIX;


/**
 * @classname RateLimitAspect
 * @description 限流切面
 * @date 2025/12/01
 */
@Slf4j
@Aspect
@Component
@Order(Integer.MIN_VALUE)
@RequiredArgsConstructor
public class RateLimitAspect {

    private final RedissonClient redissonClient;

    private final ExpressionParser parser = new SpelExpressionParser();

    @Around("@annotation(com.epiprev.server.common.limiter.annotation.RateLimit)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1. 解析限流注解属性
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RateLimit annotation = signature.getMethod().getAnnotation(RateLimit.class);
        int limit = annotation.limit();
        int windowSize = annotation.windowSize();
        String message = annotation.message();
        // 2.解析注解中的 SpEL 表达式
        StandardEvaluationContext context = new StandardEvaluationContext();
        // 2.1 获取参数名和参数值
        String[] paramNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        // 2.2 将参数名与参数值绑定到 SpEL 上下文
        if (paramNames != null) {
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
        }
        // 2.3 解析 SpEL 表达式，获取 key
        String key = parser.parseExpression(annotation.key()).getValue(context, String.class);
        // 3. 尝试获取令牌
        // 3.1 获取限流器
        RRateLimiter rRateLimiter = redissonClient.getRateLimiter(LIMIT_KEY_PREFIX + key);
        // 3.2 判断限流器是否存在
        if (!rRateLimiter.isExists()) {
            // 3.3 不存在则创建
            rRateLimiter.trySetRate(RateType.OVERALL, limit, Duration.ofSeconds(windowSize));
        }
        // 3.4 尝试获取令牌
        boolean access =  rRateLimiter.tryAcquire();
        // 4. 限流触发
        if (!access) {
            log.warn("{}，key: {}, limit: {}, windowSize: {}", message, key, limit, windowSize);
            throw new LimiterException(message); // 强制限流，抛出异常
        }
        // 5. 执行原方法
        return joinPoint.proceed();
    }
}