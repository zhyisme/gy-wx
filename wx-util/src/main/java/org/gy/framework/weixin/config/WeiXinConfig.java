package org.gy.framework.weixin.config;

import java.io.Serializable;
import java.util.Properties;

/**
 * 微信配置
 */
public class WeiXinConfig implements Serializable {

    private static final long  serialVersionUID       = 7524964169167796587L;

    /**
     * 微信appID配置标识
     */
    public static final String CONFIG_APPID_KEY       = WeiXinConfig.class.getName() + ".appId";
    /**
     * 微信appsecret配置标识
     */
    public static final String CONFIG_SECRET_KEY      = WeiXinConfig.class.getName() + ".secret";
    /**
     * 有效时间配置标识
     */
    public static final String CONFIG_EXPIRE_TIME_KEY = WeiXinConfig.class.getName() + ".expireTime";

    /**
     * 授权类型配置标识
     */
    public static final String CONFIG_GRANT_TYPE_KEY  = WeiXinConfig.class.getName() + ".grantType";

    /**
     * 授权类型默认值，获取access_token填写client_credential
     */
    public static final String DEFAULT_GRANT_TYPE     = "client_credential";

    /**
     * 默认超时时间
     */
    public static final int    DEFAULT_EXPIRE_TIME    = 7200;

    /**
     * 全量配置
     */
    private Properties         properties;

    /**
     * 微信appId
     */
    private String             appId;
    /**
     * 微信appSecret
     */
    private String             secret;
    /**
     * 有效时间
     */
    private Integer            expireTime;

    /**
     * 授权类型
     */
    private String             grantType;

    /**
     * 获取微信appId
     * 
     * @return appId 微信appId
     */
    public String getAppId() {
        return appId;
    }

    /**
     * 设置微信appId
     * 
     * @param appId 微信appId
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * 获取微信appSecret
     * 
     * @return secret 微信appSecret
     */
    public String getSecret() {
        return secret;
    }

    /**
     * 设置微信appSecret
     * 
     * @param secret 微信appSecret
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
     * 获取有效时间
     * 
     * @return expireTime 有效时间
     */
    public Integer getExpireTime() {
        return expireTime;
    }

    /**
     * 设置有效时间
     * 
     * @param expireTime 有效时间
     */
    public void setExpireTime(Integer expireTime) {
        this.expireTime = expireTime;
    }

    /**
     * 获取授权类型
     * 
     * @return grantType 授权类型
     */
    public String getGrantType() {
        return grantType;
    }

    /**
     * 设置授权类型
     * 
     * @param grantType 授权类型
     */
    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    /**
     * 获取全量配置
     * 
     * @return properties 全量配置
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * 设置全量配置
     * 
     * @param properties 全量配置
     */
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

}
