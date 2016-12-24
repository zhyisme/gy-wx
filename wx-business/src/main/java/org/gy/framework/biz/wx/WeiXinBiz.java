package org.gy.framework.biz.wx;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.gy.framework.biz.BaseBiz;
import org.gy.framework.model.WeixinReplyLog;
import org.gy.framework.util.httpclient.HttpClientUtil;
import org.gy.framework.util.json.JacksonMapper;
import org.gy.framework.weixin.api.token.SimpleAccessToken;
import org.gy.framework.weixin.config.Configurable;
import org.gy.framework.weixin.config.WeiXinConfig;
import org.gy.framework.weixin.exception.WeiXinException;
import org.gy.framework.weixin.message.xml.request.LinkNormalRequestMessage;
import org.gy.framework.weixin.message.xml.request.LocationNormalRequestMessage;
import org.gy.framework.weixin.message.xml.request.MenuClickEventRequestMessage;
import org.gy.framework.weixin.message.xml.request.MenuViewEventRequestMessage;
import org.gy.framework.weixin.message.xml.request.ScanEventRequestMessage;
import org.gy.framework.weixin.message.xml.request.SubscribeEventRequestMessage;
import org.gy.framework.weixin.message.xml.request.TextNormalRequestMessage;
import org.gy.framework.weixin.message.xml.request.UnSubscribeEventRequestMessage;
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
    private ThreadPoolExecutor             executor;
    @Autowired
    private WeiXinUserRecordBiz            weiXinUserRecordBiz;

    @Autowired
    private WeixinReplyLogBiz              weiXinReplyLogBiz;

    private static final SimpleAccessToken simpleAccessToken;

    static {
        simpleAccessToken = new SimpleAccessToken(WeiXinUtil.getConfigurable());
    }

    /**
     * 获取全局配置
     */
    public Configurable getConfigurable() {
        return WeiXinUtil.getConfigurable();
    }

    /**
     * 功能描述: 获取微信配置
     * 
     */
    public WeiXinConfig getWeiXinConfig() {
        return WeiXinUtil.getWeiXinConfig();
    }

    /**
     * 功能描述: 获取token
     * 
     */
    public String getToken() {
        return simpleAccessToken.refreshToken().getAsscessToken();
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
                    logger.error(e.getMessage(), e);
                }
            }
        }
        if (StringUtils.isNotBlank(xml)) {
            // 解析成消息对象
            try {
                WeiXinRequest message = WeiXinUtil.parsingWeiXinMessage(xml);
                if (message == null) {
                    // 消息类型未实现时，返回空值，不需要处理，返回空即可
                    return null;
                }
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
        // 记录用户操作
        final String openId = requestMessage.getFromUserName();

        if (requestMessage instanceof TextNormalRequestMessage) {
            // 文本消息
            responseMessage = dealMessage((TextNormalRequestMessage) requestMessage);
        } else if (requestMessage instanceof LocationNormalRequestMessage) {
            // 位置消息
            responseMessage = dealMessage((LocationNormalRequestMessage) requestMessage);
        } else if (requestMessage instanceof LinkNormalRequestMessage) {
            // 链接消息
            responseMessage = dealMessage((LinkNormalRequestMessage) requestMessage);
        } else if (requestMessage instanceof SubscribeEventRequestMessage) {
            // 订阅、未关注扫描带参数二维码事件
            SubscribeEventRequestMessage message = (SubscribeEventRequestMessage) requestMessage;
            responseMessage = dealMessage(message);
        } else if (requestMessage instanceof UnSubscribeEventRequestMessage) {
            // 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
        } else if (requestMessage instanceof ScanEventRequestMessage) {
            // 用户已关注时扫描带参数二维码事件
        } else if (requestMessage instanceof MenuClickEventRequestMessage) {
            // 菜单拉取消息事件
            responseMessage = dealMessage((MenuClickEventRequestMessage) requestMessage);
        } else if (requestMessage instanceof MenuViewEventRequestMessage) {
            // 菜单跳转链接事件
            // 查看事件,跳转到具体的网页，不需要处理
        }

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
                    weiXinReplyLogBiz.insertSelective(entity);
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
        if ("0000".equals(keywords)) {
            String accessToken = getToken();
            String openId = requestMessage.getFromUserName();
            Map<String, Object> map = getUserInfo(accessToken, openId);
            return dealText(requestMessage, JacksonMapper.beanToJson(map));
        }

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
     * 链接消息处理
     */
    private WeiXinResponse dealMessage(LinkNormalRequestMessage requestMessage) {

        return dealText(requestMessage, "您发送的是链接消息！" + requestMessage.getUrl());
    }

    /**
     * 订阅、未关注扫码事件处理
     */
    private WeiXinResponse dealMessage(SubscribeEventRequestMessage requestMessage) {
        String accessToken = getToken();
        String openId = requestMessage.getFromUserName();
        Map<String, Object> map = getUserInfo(accessToken, openId);
        return dealText(requestMessage, JacksonMapper.beanToJson(map));

    }

    /**
     * 菜单拉取消息事件处理
     */
    private WeiXinResponse dealMessage(MenuClickEventRequestMessage requestMessage) {
        // 菜单点击事件
        return dealText(requestMessage, "您点击了菜单！" + requestMessage.getEventKey());
    }

    private Map<String, Object> getUserInfo(String accessToken,
                                            String openId) {
        WeiXinConfig config = getWeiXinConfig();
        Properties properties = config.getProperties();
        String pattern = properties.getProperty("wx.account.user.url");
        String url = MessageFormat.format(pattern, accessToken, openId);
        String result = HttpClientUtil.get(url, null);
        return JacksonMapper.jsonToBean(result, Map.class, String.class, Object.class);
    }

    private WeiXinResponse dealText(WeiXinRequest requestMessage,
                                    String message) {
        TextResponseMessage responseMessage = new TextResponseMessage();
        responseMessage.setContent(message);
        responseMessage.setMsgType(WeiXinConstantUtil.MESSAGE_TYPE_TEXT);

        WeiXinUtil.packageResponseMessage(requestMessage, responseMessage);
        return responseMessage;
    }

}
