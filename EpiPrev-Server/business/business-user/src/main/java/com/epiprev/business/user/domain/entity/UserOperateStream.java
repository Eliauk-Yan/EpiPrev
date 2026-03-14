package com.epiprev.business.user.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.epiprev.common.datasource.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("user_operate_stream")
@AllArgsConstructor
public class UserOperateStream extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long userId;

    private String operateType;

    private LocalDateTime operateTime;

    private String param;

    private String extend_info;

}
