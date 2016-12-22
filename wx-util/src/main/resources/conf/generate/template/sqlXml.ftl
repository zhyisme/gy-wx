<?xml version="1.0" encoding="UTF-8" ?>
<!-- ${entity.tableComment}报表 -->
<sqlMap namespace="${entity.lowerClassName}">
	<!-- 查询记录条数 -->
	<sql id="COUNT_RECORD">
	<![CDATA[
  	   SELECT COUNT(0) NUM
        FROM ${entity.tableName} WHERE 1=1
		   	   <#list entity.properties as property>
				<#if property.javaType=="Date">
		   	       ${r'<#if'} ${property.propertyName}Start ${r'?exists>'}
					 	AND ${property.colName} >=:${property.propertyName}Start
				   ${r'</#if>'}
				   ${r'<#if'} ${property.propertyName}End ${r'?exists>'}
				     	AND ${property.colName}<=:${property.propertyName}End
				   ${r'</#if>'}
		   	    <#else>
			   	   ${r'<#if'} ${property.propertyName} ${r'?exists&&'} ${property.propertyName} ${r'!="">'}
				     AND ${property.colName} =:${property.propertyName}
				   ${r'</#if>'}
		   	   	</#if>
		   	   </#list>
		]]>
	</sql>


	<!-- 分页查询 -->
	<sql id="FIND_BY_PAGE">
	    <![CDATA[
			select 
		    <#list entity.properties as property>
		    	<#if !property_has_next>
					${property.colName}
				<#else>
					${property.colName},
				</#if>
		    </#list>	
		    FROM 
		    	${entity.tableName}
		    WHERE 1=1
		       <#list entity.properties as property>
				<#if property.javaType=="Date">
		   	       ${r'<#if'} ${property.propertyName}Start ${r'?exists>'}
					 	AND ${property.colName} >=:${property.propertyName}Start
				   ${r'</#if>'}
				   ${r'<#if'} ${property.propertyName}End ${r'?exists>'}
				     	AND ${property.colName}<=:${property.propertyName}End
				   ${r'</#if>'}
		   	    <#else>
			   	   ${r'<#if'} ${property.propertyName} ${r'?exists&&'} ${property.propertyName} ${r'!="">'}
				     AND ${property.colName} =:${property.propertyName}
				   ${r'</#if>'}
		   	   	</#if>
		   	   </#list>
			   LIMIT :index,:rows
		]]>
	</sql>
	

</sqlMap>

