package org.gy.framework.weixin.service;

import javax.servlet.http.HttpServletRequest;

/**
 * 功能描述：核心业务处理接口
 * 
 */
public interface WeiXinCoreService {

    String VALIDATE_TOKEN_KEY = WeiXinCoreService.class.getName() + ".tokenConfig";

    /**
     * 核心业务处理
     */
    String processRequest(HttpServletRequest request);

    /**
     * 获取验证token
     */
    String getValidateToken();

}
