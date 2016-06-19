package org.gy.framework.weixin.api.custom;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.gy.framework.weixin.api.token.SimpleAccessToken;
import org.gy.framework.weixin.config.Configurable;
import org.gy.framework.weixin.config.WeiXinConfigFactory;
import org.gy.framework.weixin.message.json.custom.CustomArticle;
import org.gy.framework.weixin.message.json.custom.CustomNews;
import org.gy.framework.weixin.message.json.custom.CustomNewsMessage;
import org.gy.framework.weixin.util.WeiXinConstantUtil;

public class CustomServiceTest extends TestCase {

    public void testSendMessage() {
        Configurable configurable = WeiXinConfigFactory.getConfigurable("weixin.properties");
        SimpleAccessToken token = new SimpleAccessToken(configurable);
        String result = token.refreshToken();
        System.out.println(result);

        CustomNewsMessage message = new CustomNewsMessage();
        CustomService customService = new CustomService(configurable);
        customService.setAccessToken(result);
        customService.setCustomBaseMessage(message);

        message.setTouser("oGi2lwIAazamKW1tRZil5eeMrLrs");
        message.setMsgtype(WeiXinConstantUtil.MESSAGE_TYPE_NEWS);
        CustomNews news = new CustomNews();
        message.setNews(news);
        List<CustomArticle> list = new ArrayList<CustomArticle>();
        news.setArticles(list);
        CustomArticle article = new CustomArticle();
        article.setTitle("标题标题标题标题标题");
        article.setDescription("描述描述描述描述");
        article.setUrl("http://www.baidu.com");
        article.setPicurl("http://mp.weixin.qq.com/debug/zh_CN/htmledition/images/bg/bg_logo1f2fc8.png");
        list.add(article);
        list.add(article);
        list.add(article);
        list.add(article);
        list.add(article);

        result = customService.execute();

        System.out.println(result);

        assertTrue(result.contains("ok"));

    }

}
