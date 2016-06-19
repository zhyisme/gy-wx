package org.gy.framework.weixin.api.custom;

import org.gy.framework.util.json.JacksonMapper;
import org.gy.framework.weixin.api.ApiExecutor;
import org.gy.framework.weixin.api.MethodType;
import org.gy.framework.weixin.config.Configurable;
import org.gy.framework.weixin.message.json.custom.CustomBaseMessage;

public class CustomService extends ApiExecutor {

    /**
     * 客服消息
     */
    private CustomBaseMessage customBaseMessage;

    public CustomService(Configurable configurable) {
        super(configurable);
    }

    @Override
    public String getBodyContent() {
        return JacksonMapper.beanToJson(customBaseMessage);
    }

    @Override
    public MethodType getMethodType() {
        return MethodType.POST;
    }

    /**
     * 获取客服消息
     * 
     * @return customBaseMessage 客服消息
     */
    public CustomBaseMessage getCustomBaseMessage() {
        return customBaseMessage;
    }

    /**
     * 设置客服消息
     * 
     * @param customBaseMessage 客服消息
     */
    public void setCustomBaseMessage(CustomBaseMessage customBaseMessage) {
        this.customBaseMessage = customBaseMessage;
    }

}
