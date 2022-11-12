package com.ivan.alioth.exception;

import com.ivan.alioth.api.IResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>业务异常</p>
 *
 * @author ivan
 * @className BizException
 * @since 2022/5/12 22:27
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BizException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    private String errorMessage;

    private Integer errorCode;

    public BizException(String errorMessage, Integer errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public BizException(IResultCode resultCode) {
        this.errorMessage = resultCode.getMessage();
        this.errorCode = resultCode.getCode();
    }
}
