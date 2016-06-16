package org.gy.framework.weixin.message.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class EventRequestMessage extends BaseEventRequest {

    @XStreamAlias("EventKey")
    private String eventKey;

    /**
     * 获取eventKey
     * 
     * @return eventKey eventKey
     */
    public String getEventKey() {
        return eventKey;
    }

    /**
     * 设置eventKey
     * 
     * @param eventKey eventKey
     */
    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

}
