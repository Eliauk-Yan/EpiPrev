package com.epiprev.common.datasource.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
/**
 * @classname MybatisPlusConfig
 * @description MyBatisPlus 配置类
 * @date 2025/11/28 15:56
 */
@AutoConfiguration
@MapperScan(basePackages = "com.epiprev.business.*.mapper")
public class DataSourceAutoConfiguration {

    @Bean
    public DataSourceMetaObjectHandler myMetaObjectHandler() {
        return new DataSourceMetaObjectHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 配置 MyBatisPlus 防止全表更新插件
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        // 配置 MyBatisPlus 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // 配置 MyBatisPlus 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL)); // 如果配置多个插件, 切记分页最后添加
        // 如果有多数据源可以不配具体类型, 否则都建议配上具体的 DbType
        return interceptor;
    }
}
