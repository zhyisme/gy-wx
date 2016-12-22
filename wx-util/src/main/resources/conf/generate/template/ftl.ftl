<#setting url_escaping_charset='utf-8'>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <title>${entity.tableComment}报表</title>
	
    ${r'<#include "/common/meta.ftl">'}
  </head>
  <body> 
	<div id="searchPanel" class="easyui-panel" title="${entity.tableComment}报表"
		style="text-align:left;width:auto;background: #fafafa;" collapsible="false"
		minimizable="false" maximizable="false" data-options="fit:true">
		
		<div class="grid-toolbar">
			<form id="searchFrm">
				<table class="tblContent" style="width:100%;">
					<#list entity.properties as property>
						<#if property.javaType!="Date">
							<tr>
								<td class="tdLeft"><span>${property.annotation}:</span></td>
								<td class="tdRight"><input id="${property.propertyName}" name="${property.propertyName}"/></td>
							</tr>
						<#else>
							<tr>
								<td class="tdLeft">${property.annotation}:</td>
									<td class="tdRight">
									<input id="${property.propertyName}Start" name="${property.propertyName}Start" class="easyui-datebox"></input>
								</td>
								<td class="tdTo">--------</td>
								<td class="tdRight">	
									<input id="${property.propertyName}End" name="${property.propertyName}End" class="easyui-datebox"></input>						
								</td>
 							</tr>
						</#if>
					</#list>
					
						
					
					<tr>
						<td class="tdLeft"><span>&nbsp;</span></td>
						<td>
							<div style="float:right; ">
								<a href="javascript:void(0);" class="easyui-linkbutton" icon="icon-search" onclick="doSearch()">查询</a>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-no'" onclick="doReset()">重置</a>
							</div>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<#-- 表单查询 结果DIV开始 --> 
		<div id="tt" class="grid-auto"></div>
 	 </div>
	 <div id="dlg" class="easyui-dialog" title="提示信息" closed="true" style="width:400px;height:200px;padding:5px;">
		  	   &nbsp;&nbsp;&nbsp;&nbsp;<div id="errorContent"></div>
	 </div>
	<!--引入js路径-->
	<script type="text/javascript" src="${r'${resRoot}'}/js/${entity.className}/${entity.className}List.js"></script>
  </body>
</html>











