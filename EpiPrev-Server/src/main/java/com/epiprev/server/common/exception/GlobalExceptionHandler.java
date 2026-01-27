package com.epiprev.server.common.exception;

import com.epiprev.server.common.result.Result;
import com.epiprev.server.common.result.enums.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
