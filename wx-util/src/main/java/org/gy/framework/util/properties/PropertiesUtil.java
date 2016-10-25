/**
 * Copyright (C), 2011-2016, org.gy.sample
 * ProjectName:	cpx-util
 * FileName: PropertiesUtil.java
 *
 * @Author gy
 * @Date 2016年7月17日下午3:23:57
 */
package org.gy.framework.util.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 功能描述：读取配置信息
 * 
 * @Author gy
 * @Date 2016年7月17日下午3:23:57
 */
public class PropertiesUtil {

    /** 记录日志的变量 */
    private static Logger             logger   = LoggerFactory.getLogger(PropertiesUtil.class);
    /** 类实例变量 */
    private static PropertiesUtil     instance = null;
    /** 系统的默认自定义properties */
    public static Map<String, String> config   = null;

    /** 私有构造函数 */
    private PropertiesUtil() {
    }

    static {
        config = PropertiesUtil.getInstance().getProperties("/conf/main-setting.properties");
    }

    /**
     * 获取实例变量
     * 
     * @return PropertieUtil实例
     */
    public static PropertiesUtil getInstance() {
        if (instance == null) {
            instance = new PropertiesUtil();
        }
        return instance;
    }

    /**
     * 根据<code>Properties</code>文件名取得所有的键值
     * 
     * @param filename 类路径下的<code>Properties</code>文件名
     * @return
     */
    public Map<String, String> getProperties(String filename) {
        Map<String, String> map = new HashMap<String, String>();
        InputStream is = null;
        try {
            is = getClass().getResourceAsStream(filename);
            if (is == null) {
                return map;
            }
            Properties pro = new Properties();
            pro.load(is);
            Enumeration<?> e = pro.propertyNames();
            String key = null;
            String value = null;
            while (e.hasMoreElements()) {
                key = (String) e.nextElement();
                value = pro.getProperty(key);
                map.put(key, value == null ? "" : value.trim());
            }
        } catch (IOException ie) {
            logger.error(ie.getMessage(), ie);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ie) {
                }
            }
        }
        return map;
    }

    /**
     * 根据<code>Properties</code>文件名取得所有的键值
     * 
     * @param filename 文件绝对路径<code>Properties</code>文件名
     * @return
     */
    public Map<String, String> getPropertiesByFilePath(String filename) {
        Map<String, String> map = new HashMap<String, String>();
        InputStream is = null;
        try {
            is = new FileInputStream(filename);
            Properties pro = new Properties();
            pro.load(is);
            Enumeration<?> e = pro.propertyNames();
            String key = null;
            String value = null;
            while (e.hasMoreElements()) {
                key = (String) e.nextElement();
                value = pro.getProperty(key);
                map.put(key, value == null ? "" : value.trim());
            }
        } catch (IOException ie) {
            logger.error(ie.getMessage(), ie);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ie) {
                }
            }
        }
        return map;
    }

}
