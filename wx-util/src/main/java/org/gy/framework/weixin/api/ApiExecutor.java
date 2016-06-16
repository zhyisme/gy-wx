package org.gy.framework.weixin.api;

import java.util.Properties;

import org.gy.framework.util.httpclient.HttpClientUtil;
import org.gy.framework.weixin.annotation.UrlParamName;
import org.gy.framework.weixin.config.Configurable;
import org.gy.framework.weixin.config.WeiXinConfig;
import org.gy.framework.weixin.exception.WeiXinException;
import org.gy.framework.weixin.util.WeiXinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ApiExecutor implements ApiExecutable {

    protected final Logger     logger  = LoggerFactory.getLogger(getClass());

    public static final String CONTEXT = ".context";

    /**
     * 微信配置
     */
    private WeiXinConfig       weiXinConfig;

    /**
     * 访问token
     */
    private String             accessToken;

    public ApiExecutor(Configurable configurable) {
        this.weiXinConfig = configurable.getWeiXinConfig();
    }

    /**
     * 获取方法类型
     */
    public abstract MethodType getMethodType();

    public String getContextPath() {
        return getClass().getName() + CONTEXT;
    }

    @Override
    public String getApiUrl() {
        Properties properties = weiXinConfig.getProperties();
        // 获取请求路径
        String path = properties.getProperty(getContextPath());
        if (path == null) {
            throw new WeiXinException("unknown contextPath:" + getContextPath());
        }
        StringBuffer url = new StringBuffer(path);
        // 组织参数
        String param = extractUrlParam();
        if (param != null && param.length() > 0) {
            url.append("?");
            url.append(param);
        }
        return url.toString();
    }

    @Override
    public String execute() {
        MethodType type = getMethodType();
        if (MethodType.POST.equals(type)) {
            return HttpClientUtil.defaultPost(getApiUrl(), getBodyContent());
        } else if (MethodType.GET.equals(type)) {
            return HttpClientUtil.defaultGet(getApiUrl());
        } else {
            throw new WeiXinException("unknown type：" + type);
        }
    }

    /**
     * 
     * 功能描述: 提取url参数
     * 
     * @return String
     * @version 2.0.0
     * @author yanchangyou
     */
    public String extractUrlParam() {

        return WeiXinUtil.extractUrlParamFromApiExecutable(this, true);

    }

    /**
     * 获取微信配置
     * 
     * @return weiXinConfig 微信配置
     */
    public WeiXinConfig getWeiXinConfig() {
        return weiXinConfig;
    }

    /**
     * 获取访问token
     * 
     * @return accessToken 访问token
     */
    @UrlParamName("access_token")
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * 设置访问token
     * 
     * @param accessToken 访问token
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
