package com.ivan.alioth.api.annotation;

import java.lang.annotation.*;

/**
 * <p></p>
 *
 * @author ivan
 * @className CustomResponse
 * @since 2020/12/2 13:19
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomResponse {
}
