package org.gy.framework.weixin.api.token;

import java.util.Map;

import org.gy.framework.util.json.JacksonMapper;
import org.gy.framework.weixin.annotation.UrlParamName;
import org.gy.framework.weixin.api.ApiExecutor;
import org.gy.framework.weixin.api.MethodType;
import org.gy.framework.weixin.config.Configurable;
import org.gy.framework.weixin.config.WeiXinConfig;

public class SimpleAccessToken extends ApiExecutor {

    public static final TokenStore tokenStore = new TokenStore();

    /**
     * 获取access_token填写client_credential
     */
    private String                 grantType;

    /**
     * 填写appid
     */
    private String                 appId;

    /**
     * 填写appsecret
     */
    private String                 secret;

    public SimpleAccessToken(Configurable configurable) {
        super(configurable);
    }

    @Override
    public MethodType getMethodType() {
        return MethodType.GET;
    }

    public String refreshToken() {
        WeiXinConfig config = getWeiXinConfig();
        int expireTime = config.getExpireTime();// 有效时间
        String token = tokenStore.getToken();
        if (tokenStore.isExpire(expireTime) || token == null) {
            synchronized (tokenStore) {
                token = tokenStore.getToken();
                if (tokenStore.isExpire(expireTime) || token == null) {
                    this.setAppId(config.getAppId());
                    this.setGrantType(config.getGrantType());
                    this.setSecret(config.getSecret());
                    String result = this.execute();
                    Map<String, String> map = JacksonMapper.jsonToBean(result, Map.class, String.class, String.class);
                    token = map.get("access_token");
                    tokenStore.restore(token);
                }
            }
        }
        return token;
    }

    @Override
    public String getBodyContent() {
        return null;
    }

    static class TokenStore {

        /**
         * 存储时间戳
         */
        private long   timestamp;

        /**
         * token值
         */
        private String token;

        /**
         * 是否过期，true表示过期，false未过期
         * 
         * @param second 有效期
         * @return
         */
        public boolean isExpire(int second) {
            return isExpire(System.currentTimeMillis(), second);
        }

        /**
         * 是否过期，true表示过期，false未过期
         * 
         * @param timestamp 参照时间
         * @param second 有效期
         * @return
         */
        public boolean isExpire(long timestamp,
                                int second) {
            return (timestamp - this.timestamp) >= second * 1000;
        }

        public TokenStore restore(String token) {
            this.setToken(token);
            this.setTimestamp(System.currentTimeMillis());
            return this;
        }

        /**
         * 获取存储时间戳
         * 
         * @return timestamp 存储时间戳
         */
        public long getTimestamp() {
            return timestamp;
        }

        /**
         * 设置存储时间戳
         * 
         * @param timestamp 存储时间戳
         */
        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        /**
         * 获取token值
         * 
         * @return token token值
         */
        public String getToken() {
            return token;
        }

        /**
         * 设置token值
         * 
         * @param token token值
         */
        public void setToken(String token) {
            this.token = token;
        }

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
