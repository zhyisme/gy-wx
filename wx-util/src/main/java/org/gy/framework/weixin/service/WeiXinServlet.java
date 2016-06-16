package org.gy.framework.weixin.service;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gy.framework.weixin.util.SignUtil;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 功能描述：请求处理类
 * 
 */
public class WeiXinServlet extends HttpServlet {

    private static final long  serialVersionUID      = 4133082469608256850L;

    public static final String DEFAULT_ENCODING      = "UTF-8";

    /**
     * 微信推荐异常回复
     */
    public static final String DEFAULT_WEIXIN_RETURN = "success";

    private String             token;

    private WeiXinCoreService  weiXinCoreService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
        weiXinCoreService = applicationContext.getBean(WeiXinCoreService.class);
        token = weiXinCoreService.getValidateToken();
    }

    /**
     * 确认请求来自微信服务器
     */
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");

        PrintWriter out = response.getWriter();
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        if (SignUtil.checkSignature(token, signature, timestamp, nonce)) {
            out.print(echostr);
        }
        out.close();
        out = null;
    }

    /**
     * 处理微信服务器发来的消息
     */
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
        request.setCharacterEncoding(DEFAULT_ENCODING);
        response.setCharacterEncoding(DEFAULT_ENCODING);

        // 调用核心业务类接收消息、处理消息
        String respMessage = weiXinCoreService.processRequest(request);
        if (respMessage == null) {
            respMessage = DEFAULT_WEIXIN_RETURN;
        }
        // 响应消息
        PrintWriter out = response.getWriter();
        out.print(respMessage);
        out.close();
    }

}
