package com.ivan.alioth.exception;


import com.ivan.alioth.api.R;
import com.ivan.alioth.api.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Objects;

/**
 * <p>全局异常处理</p>
 *
 * @author ivan
 * @className GlobalExceptionHandler
 * @since 2021/12/7 23:36
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 默认全局异常处理。
     * @param e the e
     * @return R
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<String> exception(Exception e) {
        log.error("全局异常信息 ex={}", e.getMessage(), e);
        return R.fail(ResultCode.FAILURE, e.getMessage());
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<String> exception(SQLIntegrityConstraintViolationException e) {
        log.error("数据重复异常信息 ex={}", e.getMessage(), e);
        return R.fail(ResultCode.FAILURE, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<String> exception(MethodArgumentNotValidException e) {
        return R.fail(ResultCode.PARAM_MISS, Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

}
