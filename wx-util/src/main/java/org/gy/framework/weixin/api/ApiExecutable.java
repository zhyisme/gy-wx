package org.gy.framework.weixin.api;

public interface ApiExecutable {

    /**
     * 执行api
     * 
     * @return
     */
    String execute();

    /**
     * 获取api路径
     * 
     * @return
     */
    String getApiUrl();

    /**
     * 获取请求数据json包，没有的话，设置成空值
     * 
     * @return
     */
    String getBodyContent();
}
