/*
 * Copyright (C), 2002-2016, 苏宁易购电子商务有限公司
 * FileName: ${entity.fileName}.java
 * Author:   guanyang/14050360
 * Date:     ${entity.createDate?string("yyyy-MM-dd HH:mm:ss")}
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package ${entity.javaPackage};
import java.util.Date;
import ${entity.javaPackage}.${entity.className};

/**
 * 功能描述：${entity.tableComment}Bo
 *
 */
public class ${entity.className}Bo extends ${entity.className}{
    /**
     * 当前页码
     */
    private Integer    page  = 1;
    /**
     * 页面记录数
     */
    private Integer    rows  = 10;
    /**
     * 起始索引
     */
    private Integer    index = 0;
    
	<#list entity.properties as property>
		<#if property.javaType=="Date">
	/**
     * ${property.annotation}起始值
     * @return ${property.propertyName}Start ${property.annotation}起始值
     */	
	private Date ${property.propertyName}Start;
	/**
     * ${property.annotation}结束值
     * @return ${property.propertyName}End ${property.annotation}结束值
     */
	private Date ${property.propertyName}End;
		</#if>
	</#list>
    
    
	/**
     * 获取当前页码
     * @return page 当前页码
     */
    public Integer getPage() {
        return page;
    }
    /**
     * 设置当前页码
     * @param page 当前页码
     */
    public void setPage(Integer page) {
        this.page = page;
    }
    /**
     * 获取页面记录数
     * @return rows 页面记录数
     */
    public Integer getRows() {
        return rows;
    }
    /**
     * 设置页面记录数
     * @param rows 页面记录数
     */
    public void setRows(Integer rows) {
        this.rows = rows;
    }
    /**
     * 获取起始索引
     * @return index 起始索引
     */
    public Integer getIndex() {
        return index;
    }
    /**
     * 设置起始索引
     * @param index 起始索引
     */
    public void setIndex(Integer index) {
        this.index = index;
    }
    
	<#list entity.properties as property>
		<#if property.javaType=="Date">
	/**
     * 获取${property.annotation}起始值
     * @return ${property.propertyName}Start ${property.annotation}起始值
     */
    public ${property.javaType} get${property.propertyName?cap_first}Start() {
        return ${property.propertyName}Start;
    }
    /**
	 * 设置${property.annotation}起始值
     * @param ${property.propertyName}Start ${property.annotation}起始值
     */
    public void set${property.propertyName?cap_first}Start(${property.javaType} ${property.propertyName}Start) {
        this.${property.propertyName}Start = ${property.propertyName}Start;
    }
    /**
     * 获取${property.annotation}结束值
     * @return ${property.propertyName}End ${property.annotation}结束值
     */
    public ${property.javaType} get${property.propertyName?cap_first}End() {
        return ${property.propertyName}End;
    }
	/**
	 * 设置${property.annotation}结束值
     * @param ${property.propertyName}End ${property.annotation}结束值
     */
    public void set${property.propertyName?cap_first}End(${property.javaType} ${property.propertyName}End) {
        this.${property.propertyName}End = ${property.propertyName}End;
    }
		</#if>
	</#list>
    
}
