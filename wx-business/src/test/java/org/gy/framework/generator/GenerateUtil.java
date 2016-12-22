/*
 * Copyright (C), 2002-2016, 苏宁易购电子商务有限公司
 * FileName: GenerateTest.java
 * Author:   guanyang/14050360
 * Date:     2016年12月22日 下午5:30:53
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package org.gy.framework.generator;

import org.gy.framework.util.generator.code.EntityGenerator;
import org.gy.framework.util.generator.code.GenerateParam;

/**
 * 功能描述：
 * 
 * @version 2.0.0
 * @author guanyang/14050360
 */
public class GenerateUtil {

    public static void main(String[] args) {
        // 生成文件之后，需要刷新工程，才能看到新生成的文件

        // 定义数据库配置
        String url = "jdbc:mysql://10.27.150.199:3306/assp";
        String user = "assp_read";
        String password = "sn_321";
        String schema = "assp";
        String[] tableNames = {
            "diamond_record"
        };

        // 设置基础参数
        GenerateParam param = new GenerateParam();
        param.setUrl(url);
        param.setUser(user);
        param.setPassword(password);
        param.setSchema(schema);
        param.setTableNames(tableNames);

        // 加载实体数据
        EntityGenerator.loadEntities(param);

        // 生成实体
        param.setTargetJavaPackage("org.gy.framework.entity");
        EntityGenerator.generateEntity(param);

        // 生成Biz
        param.setTargetJavaPackage("org.gy.framework.biz");
        EntityGenerator.generateBiz(param);

        // 生成Bo
        param.setTargetJavaPackage("org.gy.framework.bo");
        EntityGenerator.generateBo(param);

        // 生成Controller
        param.setTargetJavaPackage("org.gy.framework.controller");
        EntityGenerator.generateController(param);

        // 生成ftl
        param.setTargetJavaPackage("freemarker.web.page");
        EntityGenerator.generateFtl(param);

        // 生成js
        param.setTargetJavaPackage("project.web.js");
        EntityGenerator.generateJs(param);

        // 生产成sql
        param.setTargetJavaPackage("conf.sqlMap");
        EntityGenerator.generateSqlXml(param);
    }
}
