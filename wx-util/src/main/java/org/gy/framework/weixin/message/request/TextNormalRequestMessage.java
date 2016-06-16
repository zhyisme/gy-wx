package org.gy.framework.weixin.message.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 功能描述: 微信文本请求消息
 * 
 */
@XStreamAlias("xml")
public class TextNormalRequestMessage extends BaseNormalRequest {

    @XStreamAlias("Content")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
