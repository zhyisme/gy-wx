/**
* Copyright (C), 2011-2016, org.gy.sample
* ProjectName:	cpx-admin
* FileName: ${entity.fileName}.java
*
* @Date ${entity.createDate?string("yyyy-MM-dd HH:mm:ss")}
*/
package com.suning.pai.admin.web.controller;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import ${entity.javaPackage}.biz.${entity.className}Biz;
import ${entity.javaPackage}.bo.${entity.className}Bo;
import ${entity.javaPackage}.model.${entity.className};
import com.cpxbuy.cpx.util.response.Response;

/**
 * 功能描述：${entity.tableComment}Controller
 * 
 * @Date ${entity.createDate?string("yyyy-MM-dd HH:mm:ss")}
 */
@Controller
@RequestMapping("/${entity.lowerClassName}")
public class ${entity.className}Controller extends BaseController{
    
    @Autowired
    private ${entity.className}Biz           ${entity.lowerClassName}Biz;
    
    /**
     * 
     * 功能描述: 跳转到列表页面
     * 
     * @return ModelAndView
     * @Date ${entity.createDate?string("yyyy-MM-dd HH:mm:ss")}
     */
    @RequestMapping(value ="/index", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("admin/${entity.lowerClassName}/${entity.lowerClassName}List.ftl");
        return mav;
    }

    /**
     * 功能描述: 分页查询记录
     *
     * @param query
     * @return
     * @Date ${entity.createDate?string("yyyy-MM-dd HH:mm:ss")}
     */
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public ModelAndView query${entity.className}(${entity.className}Bo query) {
        ModelAndView mav = new ModelAndView(new MappingJacksonJsonView());
        query.setIndex((query.getPage() - 1) * query.getRows());
        List<${entity.className}> bos =  ${entity.lowerClassName}Biz.queryForList(query);
        Integer total =${entity.lowerClassName}Biz.queryForCount(query);
        mav.addObject("total", total);
        mav.addObject("rows",bos);
        return mav;
    }
    
    /**
     * 功能描述: 根据主键获取${entity.tableComment}实体
     *
     * @param id
     * @return
     * @Date ${entity.createDate?string("yyyy-MM-dd HH:mm:ss")}
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ModelAndView get(Long id) {
        ModelAndView mav = getJsonModelAndView(true, true);
        Response<${entity.className}> response = new Response<${entity.className}>();
        mav.addObject("response", response);
        if (id == null) {
            response.setSuccess(false);
            response.setMessage("主键不能为空");
            return mav;
        }
        ${entity.className} entity = ${entity.lowerClassName}Biz.select(id);
        if (entity == null) {
            response.setSuccess(false);
            response.setMessage("实体信息不存在");
            return mav;
        }
        response.setSuccess(true);
        response.setMessage("操作成功");
        response.setResult(entity);
        return mav;
    }
    
    /**
     * 功能描述: 保存或更新(细节字段自行调整)
     *
     * @param entity
     * @return
     * @Date ${entity.createDate?string("yyyy-MM-dd HH:mm:ss")}
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView save(${entity.className} entity) {
        ModelAndView mav = new ModelAndView(new MappingJacksonJsonView());
        Response<Long> result = new Response<Long>();
        mav.addObject("response", result);
        <#list entity.properties as property>
        <#if property.pk>
        if (entity.get${property.propertyName?cap_first}() != null && entity.get${property.propertyName?cap_first}() > 0) {
        	// 更新
            //entity.setUpdateTime(new Date());
            ${entity.lowerClassName}Biz.update(entity);
            result.setResult(entity.get${property.propertyName?cap_first}());
        <#break>
        </#if>
        </#list>       
        } else {
            //entity.setCreateTime(new Date());
            //entity.setUpdateTime(new Date());
            Long id = ${entity.lowerClassName}Biz.insert(entity);
            result.setResult(id);
        }
        result.setSuccess(true);
        result.setMessage("操作成功");
        return mav;
    }    
    
    /**
     * 功能描述: 根据主键批量删除
     *
     * @param ids
     * @return
     * @Date ${entity.createDate?string("yyyy-MM-dd HH:mm:ss")}
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ModelAndView del(String ids) {
        ModelAndView mav = new ModelAndView(new MappingJacksonJsonView());
        Response<Integer> result = new Response<Integer>();
        result.setSuccess(false);
        mav.addObject("response", result);
        if (StringUtils.isBlank(ids)) {
            result.setMessage("请选择要删除的数据");
            return mav;
        }
        String[] idArr = ids.split(",");
        List<Long> list = new ArrayList<Long>();
        for (String id : idArr) {
            list.add(Long.valueOf(id));
        }
        Integer num = ${entity.lowerClassName}Biz.delete(list);
        result.setSuccess(true);
        result.setMessage("总数：" + idArr.length + "，成功删除数：" + num);
        result.setResult(num);
        return mav;
    }    
    
    
    
}
