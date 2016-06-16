package org.gy.framework.weixin.message.response;

import org.gy.framework.weixin.message.response.WeiXinResponse;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 功能描述: 响应消息
 * 
 */
@XStreamAlias("xml")
public class TextResponseMessage extends WeiXinResponse {

    /**
     * 多条图文消息信息，默认第一个item为大图,注意，如果图文数超过10，则将会无响应
     */
    @XStreamAlias("Content")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
