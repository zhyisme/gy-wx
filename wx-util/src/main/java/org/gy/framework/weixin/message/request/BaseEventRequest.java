package org.gy.framework.weixin.message.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 功能描述: 微信文本请求消息
 * 
 */
public class BaseEventRequest extends WeiXinRequest {

    @XStreamAlias("Event")
    private String event;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

}
