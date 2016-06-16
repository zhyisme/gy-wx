package org.gy.framework.weixin.config;

import java.util.HashMap;
import java.util.Map;

import org.gy.framework.weixin.config.support.SimpleConfigureSupport;

public class WeiXinConfigFactory {

    public static final String                     DEFAULT_LOCATION = "weixin.properties";

    private static final Map<String, Configurable> cacheMap         = new HashMap<String, Configurable>();

    public static Configurable getConfigurable(String configPath) {
        Configurable config = cacheMap.get(configPath);
        if (config == null) {
            synchronized (cacheMap) {
                if ((config = cacheMap.get(configPath)) == null) {
                    config = new SimpleConfigureSupport(configPath);
                    cacheMap.put(configPath, config);
                }
            }
        }
        return config;
    }

}
