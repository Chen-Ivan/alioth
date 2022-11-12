package com.ivan.alioth.config;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.util.ClassUtils;

import java.nio.charset.Charset;
import java.time.LocalDateTime;

/**
 * <p></p>
 *
 * @author ivan
 * @className MybatisPlusMetaObjectHandler
 * @since 2021/12/5 22:00
 */
@Slf4j
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        LocalDateTime now = LocalDateTime.now();

        fillValIfNullByName("createId", Long.class, 1L, metaObject, false, true);
        fillValIfNullByName("createName", String.class, "admin", metaObject, false, true);
        fillValIfNullByName("createTime", LocalDateTime.class, now, metaObject, false, true);
        fillValIfNullByName("updateId", Long.class,1L, metaObject, false, true);
        fillValIfNullByName("updateName", String.class,"admin", metaObject, false, true);
        fillValIfNullByName("updateTime", LocalDateTime.class, now, metaObject, false, true);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        fillValIfNullByName("updateId", Long.class,1L, metaObject, true, false);
        fillValIfNullByName("updateName", String.class,"admin", metaObject, true, false);
        fillValIfNullByName("updateTime", LocalDateTime.class, LocalDateTime.now(), metaObject, true, false);
    }


    /**
     * 填充值，先判断是否有手动设置，优先手动设置的值。
     * @param fieldName 属性名
     * @param fieldVal 属性值
     * @param metaObject MetaObject
     * @param isCover 是否覆盖原有值,避免更新操作手动入参
     */
    private <T, E extends T> void fillValIfNullByName(String fieldName, Class<T> fieldType, E fieldVal, MetaObject metaObject, boolean isCover, boolean isInsert) {
        // 1. 没有 get 方法
        if (!metaObject.hasGetter(fieldName) || !metaObject.hasSetter(fieldName)) {
            return;
        }
        // 2. 如果用户有手动设置的值
        Object userSetValue = metaObject.getValue(fieldName);
        String setValueStr = StrUtil.str(userSetValue, Charset.defaultCharset());
        if (StrUtil.isNotBlank(setValueStr) && !isCover) {
            return;
        }
        // 3. field 类型相同时设置
        if (ClassUtils.isAssignableValue(fieldType, fieldVal)) {
            if (isInsert) {
                this.strictInsertFill(metaObject, fieldName, fieldType, fieldVal);
            } else {
                this.strictUpdateFill(metaObject, fieldName, fieldType ,fieldVal);
            }
        }
    }
}
