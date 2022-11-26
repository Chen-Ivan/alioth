package com.ivan.alioth.exception;

import com.ivan.alioth.api.R;
import com.ivan.alioth.api.annotation.CustomResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;

/**
 * <p>全局返回处理</p>
 *
 * @author ivan
 * @className ResponseAdvice
 * @since 2021/12/7 23:17
 */
@Slf4j
@RestControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    /**
     * 是否需要处理返回结果
     *
     * @param methodParameter
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {

        log.info("In supports() method of " + getClass().getSimpleName());

        Method method = methodParameter.getMethod();

        assert method != null;

        return method.isAnnotationPresent(CustomResponse.class);
    }

    /**
     * 处理返回结果
     *
     * @param body
     * @param methodParameter
     * @param mediaType
     * @param aClass
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @return
     */
    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter methodParameter,
                                  MediaType mediaType,
                                  Class aClass,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {

        log.info("In beforeBodyWrite() method of " + getClass().getSimpleName());

        if (body instanceof R) {
            return body;
        }

        return R.data(body);
    }
}
