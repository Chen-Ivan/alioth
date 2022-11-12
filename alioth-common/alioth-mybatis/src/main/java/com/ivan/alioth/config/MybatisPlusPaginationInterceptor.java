package com.ivan.alioth.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;

/**
 * <p></p>
 *
 * @author ivan
 * @className MybatisPlusPaginationInterceptor
 * @since 2021/12/5 22:25
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class MybatisPlusPaginationInterceptor extends PaginationInnerInterceptor {

    /**
     * 数据库类型
     * <p>
     * 查看 {@link #findIDialect(Executor)} 逻辑
     */
    private DbType dbType;


    public MybatisPlusPaginationInterceptor(DbType dbType) {
        this.dbType = dbType;
    }
}
