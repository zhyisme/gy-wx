package org.gy.framework.biz.wx;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.gy.framework.biz.BaseBiz;
import org.gy.framework.model.WeixinReplyLog;
import org.gy.framework.weixin.api.token.SimpleAccessToken;
import org.gy.framework.weixin.config.Configurable;
import org.gy.framework.weixin.config.WeiXinConfig;
import org.gy.framework.weixin.config.WeiXinConfigFactory;
import org.gy.framework.weixin.exception.WeiXinException;
import org.gy.framework.weixin.message.xml.request.EventRequestMessage;
import org.gy.framework.weixin.message.xml.request.LocationNormalRequestMessage;
import org.gy.framework.weixin.message.xml.request.TextNormalRequestMessage;
import org.gy.framework.weixin.message.xml.request.WeiXinRequest;
import org.gy.framework.weixin.message.xml.response.TextResponseMessage;
import org.gy.framework.weixin.message.xml.response.WeiXinResponse;
import org.gy.framework.weixin.service.WeiXinCoreService;
import org.gy.framework.weixin.util.WeiXinConstantUtil;
import org.gy.framework.weixin.util.WeiXinUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeiXinBiz extends BaseBiz implements WeiXinCoreService {

    @Autowired
    private ThreadPoolExecutor       executor;
    @Autowired
    private WeiXinUserRecordBiz      weiXinUserRecordBiz;

    @Autowired
    private WeiXinReplyLogBiz        weiXinReplyLogBiz;

    private static SimpleAccessToken simpleAccessToken;

    private static Configurable      configurable;

    static {
        configurable = WeiXinConfigFactory.getConfigurable(WeiXinConfigFactory.DEFAULT_LOCATION);
        simpleAccessToken = new SimpleAccessToken(configurable);
    }

    /**
     * 获取全局配置
     */
    public Configurable getConfigurable() {
        return configurable;
    }

    /**
     * 功能描述: 获取微信配置
     * 
     */
    public WeiXinConfig getWeiXinConfig() {
        return configurable.getWeiXinConfig();
    }

    /**
     * 功能描述: 获取token
     * 
     */
    public String getToken() {
        return simpleAccessToken.refreshToken();
    }

    @Override
    public String getValidateToken() {
        WeiXinConfig config = getWeiXinConfig();
        Properties properties = config.getProperties();
        String validateToken = properties.getProperty(WeiXinCoreService.VALIDATE_TOKEN_KEY);
        if (validateToken == null) {
            throw new WeiXinException("开发者中心token未配置");
        }
        return validateToken;
    }

    @Override
    public String processRequest(HttpServletRequest request) {
        String xml = null;
        InputStream is = null;
        try {
            is = request.getInputStream();
            xml = IOUtils.toString(is, "UTF-8");
        } catch (IOException e) {
            logger.error("读取数据错误：" + e.getMessage(), e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        if (StringUtils.isNotBlank(xml)) {
            // 解析成消息对象
            try {
                WeiXinRequest message = WeiXinUtil.parsingWeiXinMessage(xml);
                // 生成响应对象
                WeiXinResponse responseMessage = dispatch(message);
                if (responseMessage != null) {
                    return WeiXinUtil.bean2xml(responseMessage);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * 
     * 功能描述: 分发消息
     * 
     */
    public WeiXinResponse dispatch(WeiXinRequest requestMessage) {

        WeiXinResponse responseMessage = null;
        if (requestMessage instanceof TextNormalRequestMessage) {
            responseMessage = dealMessage((TextNormalRequestMessage) requestMessage);
        } else if (requestMessage instanceof LocationNormalRequestMessage) {
            responseMessage = dealMessage((LocationNormalRequestMessage) requestMessage);
        } else if (requestMessage instanceof EventRequestMessage) {
            responseMessage = dealMessage((EventRequestMessage) requestMessage);
        }
        final String openId = requestMessage.getFromUserName();
        // 记录用户操作
        addUserRecord(openId);
        return responseMessage;

    }

    private void addUserRecord(final String openId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    weiXinUserRecordBiz.addOrUpdateUserRecord(openId);
                } catch (Exception e) {
                    logger.error("记录微信用户记录异常：" + e.getMessage(), e);
                }
            }
        });
    }

    private void addReplyLog(final WeixinReplyLog entity) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    weiXinReplyLogBiz.insert(entity);
                } catch (Exception e) {
                    logger.error("添加微信日志异常：" + e.getMessage(), e);
                }
            }
        });
    }

    private WeixinReplyLog wrapWeixinReplyLog(WeiXinRequest requestMessage) {
        WeixinReplyLog log = new WeixinReplyLog();
        log.setOpenId(requestMessage.getFromUserName());
        log.setType(requestMessage.getMsgType());
        return log;
    }

    /**
     * 文本消息处理
     */
    private WeiXinResponse dealMessage(TextNormalRequestMessage requestMessage) {

        String keywords = requestMessage.getContent();// 根据关键字分发处理
        // 记录日志
        WeixinReplyLog log = wrapWeixinReplyLog(requestMessage);
        log.setContent(keywords);
        addReplyLog(log);

        return dealText(requestMessage, "您发送的是文本消息！" + keywords);
    }

    /**
     * 位置消息处理
     * 
     */
    private WeiXinResponse dealMessage(LocationNormalRequestMessage requestMessage) {

        // 记录日志
        WeixinReplyLog log = wrapWeixinReplyLog(requestMessage);
        StringBuilder builder = new StringBuilder();
        builder.append(requestMessage.getLocationX()).append(WeiXinConstantUtil.WEIXIN_LOG_SEPARATOR);
        builder.append(requestMessage.getLocationY()).append(WeiXinConstantUtil.WEIXIN_LOG_SEPARATOR);
        builder.append(requestMessage.getScale()).append(WeiXinConstantUtil.WEIXIN_LOG_SEPARATOR);
        builder.append(requestMessage.getLabel());
        log.setContent(builder.toString());
        addReplyLog(log);
        return dealText(requestMessage, "您发送的是位置信息！" + requestMessage.getLabel());
    }

    /**
     * 事件推送处理
     * 
     */
    private WeiXinResponse dealMessage(EventRequestMessage requestMessage) {
        // 事件消息
        String event = requestMessage.getEvent();

        if (WeiXinConstantUtil.EVENT_TYPE_VIEW.equals(event)) {
            // 查看事件,跳转到具体的网页，不需要处理
        } else if (WeiXinConstantUtil.EVENT_TYPE_CLICK.equals(event)) {
            // 菜单点击事件
            return dealText(requestMessage, "您点击了菜单！" + requestMessage.getEventKey());
        } else if (WeiXinConstantUtil.EVENT_TYPE_SUBSCRIBE.equals(event)) {
            // 订阅
            return dealText(requestMessage, "谢谢您的关注！");

        } else if (WeiXinConstantUtil.EVENT_TYPE_UNSUBSCRIBE.equals(event)) {
            // 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息

        }
        return null;
    }

    private WeiXinResponse dealText(WeiXinRequest requestMessage, String message) {
        TextResponseMessage responseMessage = new TextResponseMessage();
        responseMessage.setContent(message);
        responseMessage.setMsgType(WeiXinConstantUtil.MESSAGE_TYPE_TEXT);

        WeiXinUtil.packageResponseMessage(requestMessage, responseMessage);
        return responseMessage;
    }

}
