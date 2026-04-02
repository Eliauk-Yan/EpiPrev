package com.epiprev.business.user.config;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @description MyBatisPlus AES 加密处理
 */
public class AesEncryptTypeHandler extends BaseTypeHandler<String> {

    /**
     * 写数据库（加密）
     */
    @Override
    public void setNonNullParameter(
            PreparedStatement ps,
            int i,
            String parameter,
            JdbcType jdbcType
    ) throws SQLException {
        ps.setString(i, AesUtil.encrypt(parameter));
    }

    /**
     * 读数据库（按列名）
     */
    @Override
    public String getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        return AesUtil.decrypt(rs.getString(columnName));
    }

    /**
     * 读数据库（按列下标）
     */
    @Override
    public String getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        return AesUtil.decrypt(rs.getString(columnIndex));
    }

    /**
     * 读数据库（存储过程）
     */
    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        return AesUtil.decrypt(cs.getString(columnIndex));
    }
}