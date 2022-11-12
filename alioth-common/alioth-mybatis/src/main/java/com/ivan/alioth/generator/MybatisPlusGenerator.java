package com.ivan.alioth.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.ivan.alioth.base.BaseEntity;


import java.util.Collections;

/**
 * <p>代码生成器</p>
 *
 * @author ivan
 * @className MybatisPlusGenerator
 * @since 2021/12/6 23:37
 */
public class MybatisPlusGenerator {

    private static final DataSourceConfig.Builder DATA_SOURCE_CONFIG = new DataSourceConfig
            .Builder("jdbc:mysql://192.168.122.99:13306/tamao-upms?useUnicode=true&useSSL=false&characterEncoding=utf8", "root", "aini1314liliqun");

    public static void main(String[] args) {

        FastAutoGenerator.create(DATA_SOURCE_CONFIG)
                .globalConfig(builder -> {
                    builder.author("ivan") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("F:\\Code\\tamao-generator"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.ivan.tamao").pathInfo(Collections.singletonMap(OutputFile.mapperXml, "F:\\Code\\tamao-generator")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("t_user") // 设置需要生成的表名
                            .addTablePrefix("t_")
                            .entityBuilder()
                            .superClass(BaseEntity.class)
                            .enableColumnConstant()
                            .enableLombok()
                            .enableTableFieldAnnotation()
                            .enableActiveRecord()
                            .idType(IdType.AUTO)
                            .controllerBuilder()
                            .enableRestStyle()
                            .mapperBuilder()
                            .enableBaseColumnList()
                            .enableBaseResultMap()
                            .enableMapperAnnotation()
                    ; // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }

}
