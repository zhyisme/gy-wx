package org.gy.framework.controller;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.gy.framework.biz.WeiXinBiz;
import org.gy.framework.util.httpclient.HttpClientUtil;
import org.gy.framework.util.json.JacksonMapper;
import org.gy.framework.util.response.Response;
import org.gy.framework.weixin.config.WeiXinConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 * 功能描述：
 * 
 * @version 2.0.0
 * @author guanyang/14050360
 */
@Controller
@RequestMapping("/wx")
public class WeixinController extends BaseController {

    private static final String DEFAULT_AUTH_KEY         = "wx.authorize.url";
    private static final String DEFAULT_REDIRECT_KEY     = "wx.authorize.redirect";
    private static final String DEFAULT_ACCESS_TOKEN_KEY = "wx.accessToken.url";
    private static final String DEFAULT_USERINFO_KEY     = "wx.userinfo.url";
    public static final String  DEFAULT_CHARSET          = "utf-8";

    @Autowired
    private WeiXinBiz           weiXinBiz;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(String scope,
                              HttpServletResponse response) {
        if (!"snsapi_base ".equals(scope) && !"snsapi_userinfo".equals(scope)) {
            scope = "snsapi_userinfo";
        }
        WeiXinConfig config = weiXinBiz.getWeiXinConfig();
        Properties properties = config.getProperties();
        String authPattern = properties.getProperty(DEFAULT_AUTH_KEY);
        String redirectPath = properties.getProperty(DEFAULT_REDIRECT_KEY);
        try {
            redirectPath = URLEncoder.encode(redirectPath + "/wx/auth.htm", DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            logger.error("url转码异常：" + e.getMessage(), e);
            return errorView();
        }
        String state = UUID.randomUUID().toString().replace("-", "");
        String url = MessageFormat.format(authPattern, config.getAppId(), redirectPath, scope, state);
        return new ModelAndView("redirect:" + url);
    }

    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public ModelAndView auth(String code) {
        ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
        Response<Serializable> response = new Response<Serializable>();
        mav.addObject("response", response);
        if ((StringUtils.isNotEmpty(code)) && (!"authdeny".equals(code))) {
            WeiXinConfig config = weiXinBiz.getWeiXinConfig();
            Properties properties = config.getProperties();
            String pattern = properties.getProperty(DEFAULT_ACCESS_TOKEN_KEY);
            String url = MessageFormat.format(pattern, config.getAppId(), config.getSecret(), code);

            // 通过code换取网页授权access_token，获取token接口没有调用频次限制
            String result = HttpClientUtil.get(url, null);
            HashMap<String, Object> map = JacksonMapper.jsonToBean(result, HashMap.class, String.class, Object.class);
            String accessToken = (String) map.get("access_token");
            String openId = (String) map.get("openid");
            if (StringUtils.isBlank(accessToken) || StringUtils.isBlank(openId)) {
                // 获取token失败
                response.setSuccess(false);
                response.setResult(map);
                return mav;
            }

            // 拉取用户信息(需scope为 snsapi_userinfo)
            pattern = properties.getProperty(DEFAULT_USERINFO_KEY);
            url = MessageFormat.format(pattern, accessToken, openId);
            result = HttpClientUtil.get(url, null);
            map = JacksonMapper.jsonToBean(result, Map.class, String.class, Object.class);
            response.setSuccess(true);
            response.setResult(map);
        } else {
            response.setSuccess(false);
            response.setMessage("获取code失败");
            response.setResult(code);
        }
        return mav;
    }

}
