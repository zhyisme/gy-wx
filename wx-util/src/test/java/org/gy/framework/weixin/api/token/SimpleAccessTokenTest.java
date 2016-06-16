package org.gy.framework.weixin.api.token;

import junit.framework.TestCase;

import org.gy.framework.weixin.config.Configurable;
import org.gy.framework.weixin.config.WeiXinConfigFactory;

public class SimpleAccessTokenTest extends TestCase {

    public void testAccessToken() {
        Configurable configurable = WeiXinConfigFactory.getConfigurable("weixin.properties");
        SimpleAccessToken token = new SimpleAccessToken(configurable);
        String result = token.refreshToken();
        System.out.println(result);
    }
}
