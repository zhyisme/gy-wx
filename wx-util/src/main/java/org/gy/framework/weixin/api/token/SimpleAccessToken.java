package org.gy.framework.weixin.api.token;

import org.gy.framework.util.json.JacksonMapper;
import org.gy.framework.weixin.config.Configurable;
import org.gy.framework.weixin.config.WeiXinConfig;

public class SimpleAccessToken extends AbstractAccessToken {

    private static final TokenStore tokenStore = new TokenStore();

    public SimpleAccessToken(Configurable configurable) {
        super(configurable);
    }

    @Override
    public TokenResponse refreshToken() {
        WeiXinConfig config = getWeiXinConfig();
        String token = tokenStore.getToken();
        if (tokenStore.validateExpire() || token == null) {
            synchronized (tokenStore) {
                token = tokenStore.getToken();
                if (tokenStore.validateExpire() || token == null) {
                    this.setAppId(config.getAppId());
                    this.setGrantType(config.getGrantType());
                    this.setSecret(config.getSecret());
                    String result = this.execute();
                    TokenResponse response = JacksonMapper.jsonToBean(result, TokenResponse.class);
                    token = response.getAsscessToken();
                    tokenStore.restore(token, response.getExpireTime());
                }
            }
        }
        TokenResponse result = new TokenResponse();
        result.setAsscessToken(token);
        result.setExpireTime(tokenStore.getExpireTime());
        return result;
    }

    static class TokenStore {

        /**
         * token
         */
        private String token;

        /**
         * 有效期
         */
        private int    expireTime;
        /**
         * 存储时间戳
         */
        private long   storeTime;

        /**
         * 功能描述: 是否过期，true表示过期，false未过期
         * 
         * @return
         */
        public boolean validateExpire() {
            long current = System.currentTimeMillis();
            int timeNum = (int) ((current - this.storeTime) / 1000);
            storeTime = current;
            expireTime -= timeNum;
            return expireTime < 0;
        }

        public TokenStore restore(String token,
                                  int expireTime) {
            this.setToken(token);
            this.setStoreTime(System.currentTimeMillis());
            this.setExpireTime(expireTime);
            return this;
        }

        /**
         * 获取token
         * 
         * @return token token
         */
        public String getToken() {
            return token;
        }

        /**
         * 设置token
         * 
         * @param token token
         */
        public void setToken(String token) {
            this.token = token;
        }

        /**
         * 获取有效期
         * 
         * @return expireTime 有效期
         */
        public int getExpireTime() {
            return expireTime;
        }

        /**
         * 设置有效期
         * 
         * @param expireTime 有效期
         */
        public void setExpireTime(int expireTime) {
            this.expireTime = expireTime;
        }

        /**
         * 获取存储时间戳
         * 
         * @return storeTime 存储时间戳
         */
        public long getStoreTime() {
            return storeTime;
        }

        /**
         * 设置存储时间戳
         * 
         * @param storeTime 存储时间戳
         */
        public void setStoreTime(long storeTime) {
            this.storeTime = storeTime;
        }

    }

}
