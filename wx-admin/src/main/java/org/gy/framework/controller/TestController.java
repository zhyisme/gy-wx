package org.gy.framework.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.gy.framework.biz.wx.WeiXinBiz;
import org.gy.framework.weixin.api.custom.CustomService;
import org.gy.framework.weixin.api.token.SimpleAccessToken;
import org.gy.framework.weixin.config.Configurable;
import org.gy.framework.weixin.message.response.custom.CustomArticle;
import org.gy.framework.weixin.message.response.custom.CustomNews;
import org.gy.framework.weixin.message.response.custom.CustomNewsMessage;
import org.gy.framework.weixin.util.WeiXinConstantUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

@Controller
@RequestMapping("/test")
public class TestController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WeiXinBiz      weiXinBiz;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView test() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("sample/test.ftl");
        mav.addObject("time", new Date());
        return mav;
    }

    @RequestMapping(value = "/json", method = RequestMethod.GET)
    public ModelAndView json() {
        ModelAndView mav = new ModelAndView(new MappingJacksonJsonView());
        mav.addObject("time", new Date());
        logger.debug("测试日志");
        return mav;
    }

    @RequestMapping(value = "/customService", method = RequestMethod.GET)
    public ModelAndView customService(String openId) {
        ModelAndView mav = new ModelAndView(new MappingJacksonJsonView());

        Configurable configurable = weiXinBiz.getConfigurable();

        SimpleAccessToken token = new SimpleAccessToken(configurable);
        String result = token.refreshToken();

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
