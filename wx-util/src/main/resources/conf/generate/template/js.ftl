var rootPathAdmin = $("#rootPathAdmin").val();// 项目根路径
var url = rootPathAdmin + "/query.do";
tdSize();// 必须放在外面
$(document).ready(function() {
	init();
});

/**
 * 使用正则将字符串转成日期
 * @param strDate yyyy-MM-dd HH:mm:ss格式或yyyy-MM-dd格式字符串类型的日期 panchengliang/15061461
 */
function stringToDate(strDate){
    strDate= strDate.replace(/-/g,"/");
    var date = new Date(strDate);
    return date;
}	
var regDateExpectTime = /^(\d+)-(\d{1,2})-(\d{1,2})$/;

function getQueryParams() {
	<#list entity.properties as property>
			<#if property.javaType!="Date">
				var ${property.propertyName}_temp = $.trim($("#${property.propertyName}").val());
			<#else>
				var ${property.propertyName}Start_temp=$.trim($("#${property.propertyName}Start").datebox('getValue'));
				var ${property.propertyName}End_temp=$.trim($("#${property.propertyName}End").datebox('getValue'));
			</#if>	
	</#list>
	var obj = {
		<#list entity.properties as property>
				<#if property.javaType=="Date">
					<#if !property_has_next>
						${property.propertyName}Start : ${property.propertyName}Start_temp,
						${property.propertyName}End : ${property.propertyName}End_temp
					<#else>
						${property.propertyName}Start : ${property.propertyName}Start_temp,
						${property.propertyName}End : ${property.propertyName}End_temp,
					</#if>
				<#else>
					<#if !property_has_next>
						${property.propertyName} : ${property.propertyName}_temp
					<#else>
						${property.propertyName} : ${property.propertyName}_temp,
					</#if>	
				</#if>
				
		</#list>
	};
	return obj;
}










/**
 * 查询
 */
 

function innitPage() {
	$('#tt').datagrid({
		rownumbers : true,
		pagination : true,
		method : 'POST',
		height : 325,
		width : "100%",
		queryParams : getQueryParams(),
		url : "",
		columns : [ [ 
		<#list entity.properties as property>
				<#if property.javaType=="Date">
					<#if !property_has_next>
						{
							field : '${property.propertyName}',
							title : '${property.annotation}',
							width : 150,
							align : 'center',
							formatter : function(val, rec) {
								return formattime(val);
							}
						}
					<#else>
						{
							field : '${property.propertyName}',
							title : '${property.annotation}',
							width : 150,
							align : 'center',
							formatter : function(val, rec) {
								return formattime(val);
							}
						},
					</#if>
				<#else>
					<#if !property_has_next>
						{
							field : '${property.propertyName}',
							title : '${property.annotation}',
							width : 150,
							align : 'center'
						}
					<#else>
						{
							field : '${property.propertyName}',
							title : '${property.annotation}',
							width : 150,
							align : 'center'
						},
					</#if>	
				</#if>
				
		</#list>
		] ]
	});
}


/**
 * 查询
 */
function doSearch() {
	<#list entity.properties as property>
		<#if property.javaType=="Date">
			var ${property.propertyName}Start=$.trim($("#${property.propertyName}Start").datebox('getValue'));
			var ${property.propertyName}End=$.trim($("#${property.propertyName}End").datebox('getValue'));
		
		
			if (${property.propertyName}Start.length > 0) {
				if (${property.propertyName}Start.match(regDateExpectTime) == null) {
					msgShow("起始${property.annotation}格式错误！");
					return false;
				}
			}
			if (${property.propertyName}End.length > 0) {
				if (${property.propertyName}End.match(regDateExpectTime) == null) {
					msgShow("结束${property.annotation}格式错误！");
					return false;
				}
			}
			if(${property.propertyName}Start && ${property.propertyName}End){
				if(stringToDate(${property.propertyName}Start)>stringToDate(${property.propertyName}End)){
					msgShow("结束${property.annotation}必须大于起始${property.annotation}!");
					return false;
				}
			}
		</#if>
	</#list>	
			// 避免重新初始化grid
			$("#tt").datagrid("options").url = url;
			$("#tt").datagrid("reload", getQueryParams());
}



/**
 * 重置
 */
function doReset() {
	$('#searchFrm').form('clear');
}
