/**
 * Copyright (C), 2011-2016, org.gy.sample
 *
 * @Author gy
 * @Date 2016-12-25 13:49:02
 */
package org.gy.framework.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.gy.framework.biz.WeixinReplyLogBiz;
import org.gy.framework.bo.WeixinReplyLogBo;
import org.gy.framework.model.WeixinReplyLog;
import org.gy.framework.util.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

/**
 * 功能描述：微信回复日志Controller
 * 
 * @Date 2016-12-25 13:49:02
 */
@Controller
@RequestMapping("/weixinReplyLog")
public class WeixinReplyLogController extends BaseController {

    private static final String RESPONSE = "response";

    @Autowired
    private WeixinReplyLogBiz   weixinReplyLogBiz;

    /**
     * 
     * 功能描述: 跳转到列表页面
     * 
     * @return ModelAndView
     * @Date 2016-12-25 13:49:02
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index() {
        return new ModelAndView("admin/weixinReplyLog/weixinReplyLogList.ftl");
    }

    /**
     * 功能描述: 分页查询记录
     * 
     * @param query
     * @return
     * @Date 2016-12-25 13:49:02
     */
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public ModelAndView queryWeixinReplyLog(HttpServletRequest request,
                                            WeixinReplyLogBo query) {
        ModelAndView mav = new ModelAndView(new MappingJacksonJsonView());
        initQuery(query, request);
        List<WeixinReplyLog> bos = weixinReplyLogBiz.queryForList(query);
        Integer total = weixinReplyLogBiz.queryForCount(query);
        mav.addObject("total", total);
        mav.addObject("rows", bos);
        return mav;
    }

    /**
     * 功能描述: 根据主键获取微信回复日志实体
     * 
     * @param id
     * @return
     * @Date 2016-12-25 13:49:02
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ModelAndView get(Long id) {
        ModelAndView mav = new ModelAndView(new MappingJacksonJsonView());
        Response<WeixinReplyLog> response = new Response<WeixinReplyLog>();
        mav.addObject(RESPONSE, response);
        if (id == null) {
            response.setSuccess(false);
            response.setMessage("主键不能为空");
            return mav;
        }
        WeixinReplyLog entity = weixinReplyLogBiz.selectByPrimaryKey(id);
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
     * @Date 2016-12-25 13:49:02
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView save(WeixinReplyLog entity) {
        ModelAndView mav = new ModelAndView(new MappingJacksonJsonView());
        Response<Long> result = new Response<Long>();
        mav.addObject(RESPONSE, result);
        if (entity.getId() != null && entity.getId() > 0) {
            // 更新
            try {
                weixinReplyLogBiz.updateByPrimaryKeySelective(entity);
                result.setResult(entity.getId());
            } catch (DuplicateKeyException e) {
                logger.error("更新异常：" + e.getMessage(), e);
                result.setSuccess(false);
                result.setMessage("唯一索引冲突异常");
                return mav;
            }
        } else {
            // 添加
            try {
                entity.setCreateTime(new Date());
                Long id = weixinReplyLogBiz.insertSelective(entity);
                result.setResult(id);
            } catch (DuplicateKeyException e) {
                logger.error("添加异常：" + e.getMessage(), e);
                result.setSuccess(false);
                result.setMessage("唯一索引冲突异常");
                return mav;
            }
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
     * @Date 2016-12-25 13:49:02
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ModelAndView del(String ids) {
        ModelAndView mav = new ModelAndView(new MappingJacksonJsonView());
        Response<Integer> result = new Response<Integer>();
        result.setSuccess(false);
        mav.addObject(RESPONSE, result);
        if (StringUtils.isBlank(ids)) {
            result.setMessage("请选择要删除的数据");
            return mav;
        }
        String[] idArr = ids.split(",");
        List<Long> list = new ArrayList<Long>();
        for (String id : idArr) {
            list.add(Long.valueOf(id));
        }
        Integer num = weixinReplyLogBiz.deleteByPrimaryKey(list);
        result.setSuccess(true);
        result.setMessage("总数：" + idArr.length + "，成功删除数：" + num);
        result.setResult(num);
        return mav;
    }

}
