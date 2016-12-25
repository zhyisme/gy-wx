package org.gy.framework.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.gy.framework.biz.WeiXinBiz;
import org.gy.framework.biz.WeiXinMenuConfigBiz;
import org.gy.framework.biz.WeixinReplyLogBiz;
import org.gy.framework.bo.WeixinReplyLogBo;
import org.gy.framework.model.WeixinReplyLog;
import org.gy.framework.weixin.api.custom.CustomService;
import org.gy.framework.weixin.config.Configurable;
import org.gy.framework.weixin.message.json.custom.CustomArticle;
import org.gy.framework.weixin.message.json.custom.CustomNews;
import org.gy.framework.weixin.message.json.custom.CustomNewsMessage;
import org.gy.framework.weixin.util.WeiXinConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

@Controller
@RequestMapping("/test")
public class TestController extends BaseController {

    @Autowired
    private WeiXinBiz           weiXinBiz;

    @Autowired
    private WeiXinMenuConfigBiz weiXinMenuConfigBiz;

    @Autowired
    private WeixinReplyLogBiz   weiXinReplyLogBiz;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView test() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("admin/test.ftl");
        mav.addObject("time", new Date());
        return mav;
    }

    @RequestMapping(value = "/json", method = RequestMethod.GET)
    public ModelAndView json() {
        ModelAndView mav = new ModelAndView(new MappingJacksonJsonView());
        mav.addObject("time", new Date());
        logger.debug("测试日志debug");
        logger.info("测试日志info");
        logger.error("测试日志error");
        return mav;
    }

    @RequestMapping(value = "/replyLog", method = RequestMethod.GET)
    public ModelAndView replyLog() {
        ModelAndView mav = new ModelAndView(new MappingJacksonJsonView());
        WeixinReplyLog entity = new WeixinReplyLog();
        entity.setContent(UUID.randomUUID().toString());
        entity.setOpenId("test");
        entity.setType("text");
        entity.setCreateTime(new Date());
        Long id = weiXinReplyLogBiz.insertSelective(entity);
        mav.addObject("insert", entity);
        WeixinReplyLog entity1 = new WeixinReplyLog();
        entity1.setId(id);
        entity1.setOpenId(UUID.randomUUID().toString().replace("-", ""));
        int num = weiXinReplyLogBiz.updateByPrimaryKeySelective(entity1);
        mav.addObject("update", num);
        WeixinReplyLog data = weiXinReplyLogBiz.selectByPrimaryKey(id);
        mav.addObject("select", data);
        List<Long> ids = new ArrayList<Long>();
        ids.add(id);
        int result = weiXinReplyLogBiz.deleteByPrimaryKey(ids);
        mav.addObject("delete", result);
        WeixinReplyLogBo query = new WeixinReplyLogBo();
        query.setPageSize(5);
        query.setPageNo(2);
        List<WeixinReplyLog> list = weiXinReplyLogBiz.queryForList(query);
        mav.addObject("queryForList", list);
        int count =weiXinReplyLogBiz.queryForCount(query);
        mav.addObject("queryForCount", count);
        return mav;
    }

    @RequestMapping(value = "/getMenu", method = RequestMethod.GET)
    public ModelAndView getMenu() {
        ModelAndView mav = new ModelAndView(new MappingJacksonJsonView());
        mav.addObject("menu", weiXinMenuConfigBiz.selectMenuList());
        mav.addObject("menuJson", weiXinMenuConfigBiz.getMenuJson());
        return mav;
    }

    @RequestMapping(value = "/token", method = RequestMethod.GET)
    public ModelAndView token() {
        ModelAndView mav = new ModelAndView(new MappingJacksonJsonView());
        mav.addObject("token", weiXinBiz.getToken());
        return mav;
    }

    @RequestMapping(value = "/customService", method = RequestMethod.GET)
    public ModelAndView customService(String openId) {
        ModelAndView mav = new ModelAndView(new MappingJacksonJsonView());

        Configurable configurable = weiXinBiz.getConfigurable();
        String result = weiXinBiz.getToken();

        CustomNewsMessage message = new CustomNewsMessage();
        CustomService customService = new CustomService(configurable);
        customService.setAccessToken(result);
        customService.setCustomBaseMessage(message);

        message.setTouser(openId);
        message.setMsgtype(WeiXinConstantUtil.MESSAGE_TYPE_NEWS);
        CustomNews news = new CustomNews();
        message.setNews(news);
        List<CustomArticle> list = new ArrayList<CustomArticle>();
        news.setArticles(list);
        CustomArticle article = new CustomArticle();
        article.setTitle("标题标题标题标题标题");
        article.setDescription("描述描述描述描述");
        article.setUrl("https://mp.weixin.qq.com/");
        article.setPicurl("https://mmbiz.qlogo.cn/mmbiz/dGgPndsiaHgwB8iaQmbrHcSUMsqib1kp4CZG5GI5ZZblcUT9MEL9n0ObUuQI2aJAcWL5eNWzjsFusLJnu6sWNhsVw/0?wx_fmt=jpeg");
        list.add(article);
        list.add(article);
        list.add(article);
        list.add(article);
        list.add(article);

        String customResult = customService.execute();

        mav.addObject("accessToken", result);
        mav.addObject("customResult", customResult);
        mav.addObject("time", System.currentTimeMillis());
        return mav;
    }

}
