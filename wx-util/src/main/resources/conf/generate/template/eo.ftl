/*
 * Copyright (C), 2002-2016, 苏宁易购电子商务有限公司
 * FileName: ${entity.fileName}.java
 * Author:    
 * Date:     ${entity.createDate?string("yyyy-MM-dd HH:mm:ss")}
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package ${entity.javaPackage};
<#list entity.packages as package>
import ${package};
</#list>
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 功能描述：${entity.tableComment}实体
 *
 */
@Entity(name = "${entity.tableName}")
public class ${entity.className}{
    <#--********** attribute ***********-->
<#list entity.properties as property>
	/**
     * ${property.annotation?replace("\n"," ")?replace("\r"," ")}
     */
    private ${property.javaType} ${property.propertyName};
</#list>
    <#--********** get/set *************-->
<#list entity.properties as property>
	/**
     * 获取${property.annotation}
     * @return ${property.propertyName} ${property.annotation}
     */<#if property.pk>
    @Id</#if><#if property.extra?lower_case =='auto_increment'>
	@GeneratedValue(strategy = GenerationType.AUTO)</#if>
    @Column(name = "${property.colName}")
    public ${property.javaType} get${property.propertyName?cap_first}() {
        return ${property.propertyName};
    }
	/**
	 * 设置${property.annotation}
     * @param ${property.propertyName} ${property.annotation}
     */
    public void set${property.propertyName?cap_first}(${property.javaType} ${property.propertyName}) {
        this.${property.propertyName} = ${property.propertyName};
    }
</#list>
}
