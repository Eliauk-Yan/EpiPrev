package com.epiprev.common.web.handler;

import com.epiprev.common.web.constant.ResultCode;
import com.epiprev.common.web.exception.BusinessException;
import com.epiprev.common.web.exception.SystemException;
import com.epiprev.common.web.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @classname GlobalExceptionHandler
 * @description 全局异常处理
 * @date 2025/12/01 09:49
 * @created by YanShijie
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result<Void> handleBusinessException(BusinessException e) {
        log.warn("业务异常：{}", e.getMessage(), e);
        if (e.getErrorCode() != null) {
            return Result.error(e.getErrorCode().getCode(), e.getMessage());
        }
        return Result.error(ResultCode.BUSINESS_ERROR.getCode(), e.getMessage());
    }

    /**
     * 处理系统异常
     */
    @ExceptionHandler(SystemException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result<Void> handleSystemException(SystemException e) {
        log.error("系统异常：{}", e.getMessage(), e);
        if (e.getErrorCode() != null) {
            return Result.error(e.getErrorCode().getCode(), e.getMessage());
        }
        return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
    }
}
