package org.gy.framework.weixin.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.gy.framework.util.json.JacksonMapper;
import org.gy.framework.weixin.annotation.UrlParamName;
import org.gy.framework.weixin.api.ApiExecutable;
import org.gy.framework.weixin.config.Configurable;
import org.gy.framework.weixin.config.WeiXinConfig;
import org.gy.framework.weixin.config.WeiXinConfigFactory;
import org.gy.framework.weixin.exception.WeiXinException;
import org.gy.framework.weixin.message.xml.request.LinkNormalRequestMessage;
import org.gy.framework.weixin.message.xml.request.LocationNormalRequestMessage;
import org.gy.framework.weixin.message.xml.request.MenuClickEventRequestMessage;
import org.gy.framework.weixin.message.xml.request.MenuViewEventRequestMessage;
import org.gy.framework.weixin.message.xml.request.ScanEventRequestMessage;
import org.gy.framework.weixin.message.xml.request.SubscribeEventRequestMessage;
import org.gy.framework.weixin.message.xml.request.TemplateEventRequestMessage;
import org.gy.framework.weixin.message.xml.request.TextNormalRequestMessage;
import org.gy.framework.weixin.message.xml.request.UnSubscribeEventRequestMessage;
import org.gy.framework.weixin.message.xml.request.WeiXinRequest;
import org.gy.framework.weixin.message.xml.response.WeiXinResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

public class WeiXinUtil {

    private static final Logger                                      logger            = LoggerFactory.getLogger(WeiXinUtil.class);

    private static final Map<String, Class<? extends WeiXinRequest>> MESSAGE_TYPE_MAP  = new HashMap<String, Class<? extends WeiXinRequest>>();

    public static final String                                       MSG_TYPE_KEY      = "MsgType";

    public static final String                                       EVENT_TYPE_KEY    = "Event";

    private static final XStream                                     objectToXml;

    private static final Map<String, XStream>                        xmlToObjectMap    = new HashMap<String, XStream>();

    private static final String                                      DEFAULT_XML_ALIAS = "xml";

    private static final Configurable                                configurable;

    private static final WeiXinConfig                                weiXinConfig;

    private static final Properties                                  properties;

    static {
        objectToXml = new XStream(new SimpleXpp3Driver());
        objectToXml.autodetectAnnotations(true);

        // 初始化消息类型
        MESSAGE_TYPE_MAP.put(WeiXinConstantUtil.MESSAGE_TYPE_TEXT, TextNormalRequestMessage.class);
        MESSAGE_TYPE_MAP.put(WeiXinConstantUtil.MESSAGE_TYPE_LOCATION, LocationNormalRequestMessage.class);
        MESSAGE_TYPE_MAP.put(WeiXinConstantUtil.MESSAGE_TYPE_LINK, LinkNormalRequestMessage.class);
        MESSAGE_TYPE_MAP.put(WeiXinConstantUtil.EVENT_TYPE_SUBSCRIBE, SubscribeEventRequestMessage.class);
        MESSAGE_TYPE_MAP.put(WeiXinConstantUtil.EVENT_TYPE_UNSUBSCRIBE, UnSubscribeEventRequestMessage.class);
        MESSAGE_TYPE_MAP.put(WeiXinConstantUtil.EVENT_TYPE_SCAN, ScanEventRequestMessage.class);
        MESSAGE_TYPE_MAP.put(WeiXinConstantUtil.EVENT_TYPE_CLICK, MenuClickEventRequestMessage.class);
        MESSAGE_TYPE_MAP.put(WeiXinConstantUtil.EVENT_TYPE_VIEW, MenuViewEventRequestMessage.class);
        MESSAGE_TYPE_MAP.put(WeiXinConstantUtil.EVENT_TYPE_TEMPLATESENDJOBFINISH, TemplateEventRequestMessage.class);

        configurable = WeiXinConfigFactory.getConfigurable(WeiXinConfigFactory.DEFAULT_LOCATION);
        weiXinConfig = configurable.getWeiXinConfig();
        properties = weiXinConfig.getProperties();
    }

    private WeiXinUtil() {

    }

    public static Configurable getConfigurable() {
        return configurable;
    }

    public static WeiXinConfig getWeiXinConfig() {
        return weiXinConfig;
    }

    public static Properties getProperties() {
        return properties;
    }

    /**
     * 
     * 功能描述:
     * 
     * @return WeiXinMessage
     */
    public static WeiXinRequest parsingWeiXinMessage(String xml) {
        Map<String, String> messageMap = null;
        try {
            messageMap = parseXml(xml);
        } catch (Exception e) {
            throw new WeiXinException("解析xml异常：" + e.getMessage(), e);
        }
        String msgType = messageMap.get(MSG_TYPE_KEY);
        String eventType = messageMap.get(EVENT_TYPE_KEY);
        Class<? extends WeiXinRequest> clazz;
        if (WeiXinConstantUtil.MESSAGE_TYPE_EVENT.equals(msgType)) {
            clazz = MESSAGE_TYPE_MAP.get(eventType);
        } else {
            clazz = MESSAGE_TYPE_MAP.get(msgType);
        }
        if (clazz == null) {
            logger.warn("未实现的消息类型：" + JacksonMapper.beanToJson(messageMap));// 不需要抛异常，只处理实现的消息类型，有些消息类型不需要实现
            return null;
        }
        WeiXinRequest message = null;
        try {
            message = xml2bean(xml, clazz);
        } catch (Exception e) {
            throw new WeiXinException("xml转换为bean异常：" + e.getMessage(), e);
        }
        return message;
    }

    /**
     * 解析微信发来的请求（XML）
     * 
     * @param request
     * @return
     * @throws DocumentException
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> parseXml(String xml) throws DocumentException {
        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap<String, String>();

        Document document = DocumentHelper.parseText(xml);
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();

        // 遍历所有子节点
        for (Element e : elementList) {
            map.put(e.getName(), e.getText());
        }

        return map;
    }

    /**
     * 
     * 功能描述: 生成响应消息
     * 
     * @param requestMessage 用于交换from和to
     * @return WeiXinMessage
     */
    public static WeiXinResponse generateResponseMessage(WeiXinRequest requestMessage) {
        WeiXinResponse responseMessage = new WeiXinResponse();
        return packageResponseMessage(requestMessage, responseMessage);
    }

    /**
     * 
     * 功能描述: 生成响应消息
     * 
     * @param requestMessage 用于交换from和to
     * @return WeiXinMessage
     */
    public static WeiXinResponse changeFromUserToUser(WeiXinRequest requestMessage,
                                                      WeiXinResponse responseMessage) {

        responseMessage.setFromUserName(requestMessage.getToUserName());
        responseMessage.setToUserName(requestMessage.getFromUserName());

        return responseMessage;
    }

    /**
     * 
     * 功能描述: 打包消息--交换from to、添加创建时间
     * 
     */
    public static WeiXinResponse packageResponseMessage(WeiXinRequest request,
                                                        WeiXinResponse response) {

        changeFromUserToUser(request, response);
        response.setCreateTime(System.currentTimeMillis());
        return response;
    }

    /**
     * 
     * 功能描述: bean to xml 使用 xstream实现
     * 
     */
    public static String bean2xml(WeiXinResponse response) {
        return objectToXml.toXML(response);

    }

    /**
     * 
     * 功能描述: xml转换为bean
     * 
     */
    @SuppressWarnings("unchecked")
    public static <T> T xml2bean(String xml,
                                 Class<T> clazz) {
        XStream xStream = checkDuplicateAliasClassXStream(clazz);
        xStream.processAnnotations(clazz);
        return (T) xStream.fromXML(xml);
    }

    private static XStream checkDuplicateAliasClassXStream(Class<?> clazz) {
        XStream xStream;
        XStreamAlias classAlias = clazz.getAnnotation(XStreamAlias.class);
        if (classAlias != null && DEFAULT_XML_ALIAS.equals(classAlias.value())) {
            xStream = xmlToObjectMap.get(clazz.getName());
            if (xStream == null) {
                synchronized (xmlToObjectMap) {
                    if ((xStream = xmlToObjectMap.get(clazz.getName())) == null) {
                        xStream = new XStream();
                        xStream.autodetectAnnotations(true);
                        xmlToObjectMap.put(clazz.getName(), xStream);
                    }
                }
            }
        } else {
            xStream = new XStream();
            xStream.alias(DEFAULT_XML_ALIAS, clazz);
        }
        return xStream;
    }

    public static Properties loadProperties(String location) {
        Properties properties = new Properties();
        InputStream is = null;
        try {
            is = WeiXinUtil.class.getClassLoader().getResourceAsStream(location);
            properties.load(is);
        } catch (IOException e) {
            throw new WeiXinException("加载配置文件异常：" + e.getMessage(), e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return properties;
    }

    public static WeiXinConfig initWeiXinConfig(Properties properties) {
        WeiXinConfig config = new WeiXinConfig();
        config.setProperties(properties);
        config.setAppId(properties.getProperty(WeiXinConfig.CONFIG_APPID_KEY));// appId
        config.setSecret(properties.getProperty(WeiXinConfig.CONFIG_SECRET_KEY));// secret
        String time = properties.getProperty(WeiXinConfig.CONFIG_EXPIRE_TIME_KEY);
        int expireTime = WeiXinConfig.DEFAULT_EXPIRE_TIME;
        if (time != null) {
            expireTime = Integer.valueOf(time);
        }
        config.setExpireTime(expireTime);
        String grantType = properties.getProperty(WeiXinConfig.DEFAULT_GRANT_TYPE);
        if (grantType == null) {
            grantType = WeiXinConfig.DEFAULT_GRANT_TYPE;
        }
        config.setGrantType(grantType);
        return config;
    }

    public static WeiXinConfig initWeiXinConfig(String configLocation) {
        Properties properties = loadProperties(configLocation);
        return initWeiXinConfig(properties);
    }

    /**
     * 功能描述: 从提取api url参数
     * 
     * @param executor
     * @param ignoreFlag 是否忽略空值，true忽略，false不忽略
     * @return
     */
    public static String extractUrlParamFromApiExecutable(ApiExecutable executor,
                                                          boolean ignoreFlag) {
        StringBuilder buf = new StringBuilder();

        Method[] methods = executor.getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            UrlParamName urlParamName = method.getAnnotation(UrlParamName.class);
            if (urlParamName != null) {
                String name = urlParamName.value();
                String value = null;
                try {
                    value = (String) method.invoke(executor);
                } catch (Exception e) {
                    throw new WeiXinException("从对象提取url参数报错", e);
                }
                if (ignoreFlag && value == null) {
                    continue;
                }
                if (buf.length() > 0) {
                    buf.append("&");
                }
                buf.append(name).append("=").append(value);
            }
        }
        return buf.toString();
    }

    /**
     * 功能描述: 计算采用utf-8编码方式时字符串所占字节数
     * 
     * @param content
     * @return
     * @version 2.0.0
     * @author guanyang/14050360
     */
    public static int getByteSize(String content) {
        int size = 0;
        if (null != content) {
            // 汉字采用utf-8编码时占3个字节
            try {
                size = content.getBytes("utf-8").length;
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return size;
    }

}
