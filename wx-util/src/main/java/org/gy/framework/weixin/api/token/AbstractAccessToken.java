package org.gy.framework.weixin.api.token;

import org.gy.framework.weixin.annotation.UrlParamName;
import org.gy.framework.weixin.api.ApiExecutor;
import org.gy.framework.weixin.api.MethodType;
import org.gy.framework.weixin.config.Configurable;

public abstract class AbstractAccessToken extends ApiExecutor {

    /**
     * 获取access_token填写client_credential
     */
    private String grantType;

    /**
     * 填写appid
     */
    private String appId;

    /**
     * 填写appsecret
     */
    private String secret;

    /**
     * 功能描述: 根据token超时时间刷新token
     * 
     * @return
     */
    public abstract TokenResponse refreshToken();

    public AbstractAccessToken(Configurable configurable) {
        super(configurable);
    }

    @Override
    public String getBodyContent() {
        return null;
    }

    @Override
    public MethodType getMethodType() {
        return MethodType.GET;
    }

    /**
     * 获取获取access_token填写client_credential
     * 
     * @return grantType 获取access_token填写client_credential
     */
    @UrlParamName("grant_type")
    public String getGrantType() {
        return grantType;
    }

    /**
     * 设置获取access_token填写client_credential
     * 
     * @param grantType 获取access_token填写client_credential
     */
    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    /**
     * 获取填写appid
     * 
     * @return appId 填写appid
     */
    @UrlParamName("appid")
    public String getAppId() {
        return appId;
    }

    /**
     * 设置填写appid
     * 
     * @param appId 填写appid
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * 获取填写appsecret
     * 
     * @return secret 填写appsecret
     */
    @UrlParamName("secret")
    public String getSecret() {
        return secret;
    }

    /**
     * 设置填写appsecret
     * 
     * @param secret 填写appsecret
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }

}
