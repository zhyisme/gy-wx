package org.gy.framework.weixin.config.support;

import org.gy.framework.weixin.config.Configurable;
import org.gy.framework.weixin.config.WeiXinConfig;
import org.gy.framework.weixin.util.WeiXinUtil;

public class SimpleConfigureSupport implements Configurable {

    /**
     * 常用配置
     */
    private WeiXinConfig weiXinConfig;

    /**
     * 配置文件路径
     */
    private String       configLocation;

    public SimpleConfigureSupport(String configLocation) {
        this.configLocation = configLocation;
        this.weiXinConfig = WeiXinUtil.initWeiXinConfig(configLocation);
    }


    @Override
    public WeiXinConfig getWeiXinConfig() {
        return weiXinConfig;
    }

    /**
     * 获取配置文件路径
     * 
     * @return configLocation 配置文件路径
     */
    public String getConfigLocation() {
        return configLocation;
    }

}
