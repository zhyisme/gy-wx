package org.gy.framework.weixin.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.gy.framework.weixin.annotation.UrlParamName;
import org.gy.framework.weixin.api.ApiExecutable;
import org.gy.framework.weixin.config.WeiXinConfig;
import org.gy.framework.weixin.exception.WeiXinException;
import org.gy.framework.weixin.message.xml.request.EventRequestMessage;
import org.gy.framework.weixin.message.xml.request.LinkNormalRequestMessage;
import org.gy.framework.weixin.message.xml.request.LocationNormalRequestMessage;
import org.gy.framework.weixin.message.xml.request.TextNormalRequestMessage;
import org.gy.framework.weixin.message.xml.request.WeiXinRequest;
import org.gy.framework.weixin.message.xml.response.WeiXinResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.Xpp3Driver;

public class WeiXinUtil {

    private static final Logger                                     logger           = LoggerFactory.getLogger(WeiXinUtil.class);

    public static final Map<String, Class<? extends WeiXinRequest>> MESSAGE_TYPE_MAP = new HashMap<String, Class<? extends WeiXinRequest>>();

    public static final String                                      MSG_TYPE_KEY     = "MsgType";

    public static final String                                      EVENT_TYPE_KEY   = "Event";

    private static final XStream                                    objectToXml;

    private static final Map<String, XStream>                       xmlToObjectMap   = new HashMap<String, XStream>();

    static {
        objectToXml = new XStream(new Xpp3Driver() {
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new PrettyPrintWriter(out) {
                    // 对字符xml节点的转换都增加CDATA标记
                    boolean cdata = false;

                    @SuppressWarnings("unchecked")
                    public void startNode(String name, @SuppressWarnings("rawtypes") Class clazz) {
                        if (clazz.isAssignableFrom(String.class)) {
                            cdata = true;
                        } else {
                            cdata = false;
                        }
                        super.startNode(name, clazz);
                    }

                    protected void writeText(QuickWriter writer, String text) {
                        if (cdata) {
                            writer.write("<![CDATA[");
                            writer.write(text);
                            writer.write("]]>");
                        } else {
                            writer.write(text);
                        }
                    }
                };
            }
        });
        objectToXml.autodetectAnnotations(true);

        // 初始化消息类型
        MESSAGE_TYPE_MAP.put(WeiXinConstantUtil.MESSAGE_TYPE_TEXT, TextNormalRequestMessage.class);
        MESSAGE_TYPE_MAP.put(WeiXinConstantUtil.MESSAGE_TYPE_LOCATION, LocationNormalRequestMessage.class);
        MESSAGE_TYPE_MAP.put(WeiXinConstantUtil.EVENT_TYPE_SUBSCRIBE, EventRequestMessage.class);
        MESSAGE_TYPE_MAP.put(WeiXinConstantUtil.EVENT_TYPE_UNSUBSCRIBE, EventRequestMessage.class);
        MESSAGE_TYPE_MAP.put(WeiXinConstantUtil.EVENT_TYPE_CLICK, EventRequestMessage.class);
        MESSAGE_TYPE_MAP.put(WeiXinConstantUtil.EVENT_TYPE_VIEW, EventRequestMessage.class);
        MESSAGE_TYPE_MAP.put(WeiXinConstantUtil.MESSAGE_TYPE_LINK, LinkNormalRequestMessage.class);
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
        Class<? extends WeiXinRequest> clazz = null;
        if (WeiXinConstantUtil.MESSAGE_TYPE_EVENT.equals(msgType)) {
            clazz = MESSAGE_TYPE_MAP.get(eventType);
        } else {
            clazz = MESSAGE_TYPE_MAP.get(msgType);
        }
        if (clazz == null) {
            throw new WeiXinException("未实现的消息类型：msgType=" + msgType + "|eventType=" + eventType);
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
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> parseXml(String xml) throws Exception {
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
    public static WeiXinResponse changeFromUserToUser(WeiXinRequest requestMessage, WeiXinResponse responseMessage) {

        responseMessage.setFromUserName(requestMessage.getToUserName());
        responseMessage.setToUserName(requestMessage.getFromUserName());

        return responseMessage;
    }

    /**
     * 
     * 功能描述: 打包消息--交换from to、添加创建时间
     * 
     */
    public static WeiXinResponse packageResponseMessage(WeiXinRequest request, WeiXinResponse response) {

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
    public static <T> T xml2bean(String xml, Class<T> clazz) {
        XStream xStream = checkDuplicateAliasClassXStream(clazz);
        xStream.processAnnotations(clazz);
        return (T) xStream.fromXML(xml);
    }

    private static XStream checkDuplicateAliasClassXStream(Class<?> clazz) {
        XStream xStream = xmlToObjectMap.get(clazz.getName());
        if (xStream == null) {
            synchronized (xmlToObjectMap) {
                if ((xStream = xmlToObjectMap.get(clazz.getName())) == null) {
                    xStream = new XStream();
                    xmlToObjectMap.put(clazz.getName(), xStream);
                }
            }
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
            throw new RuntimeException("加载配置文件异常：" + e.getMessage(), e);
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
    public static String extractUrlParamFromApiExecutable(ApiExecutable executor, boolean ignoreFlag) {
        StringBuffer buf = new StringBuffer();

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
                    throw new RuntimeException("从对象提取url参数报错", e);
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

}
