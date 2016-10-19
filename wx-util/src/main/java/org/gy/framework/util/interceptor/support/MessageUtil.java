package org.gy.framework.util.interceptor.support;


/**
 * 功能描述：异常消息格式化输出
 * 
 */
public class MessageUtil {

    /**
     * 成功状态
     */
    public static final String SUCCESS_CODE = CodeType.SUCCESS_CODE.getCode();

    /**
     * 未知错误
     */
    public static final String UNKNOWN      = "Unknown";

    /**
     * 功能描述: 成功包装
     * 
     * @param result 结果对象
     */
    public static void wrapSuccess(BaseInterceptorDTO result) {
        wrapResponse(result, CodeType.SUCCESS_CODE, "");
    }

    /**
     * 功能描述: 包装返回结果
     *
     * @param result
     * @param e
     */
    public static void wrapResponse(BaseInterceptorDTO result,
                                    CommonException e) {
        CodeType type = e.getCodeType();
        Object[] placeholder = e.getPlaceholder();
        wrapResponse(result, type, placeholder);
    }

    /**
     * 功能描述: 包装返回结果
     *
     * @param result
     * @param type
     * @param placeholder
     */
    public static void wrapResponse(BaseInterceptorDTO result,
                                    CodeType type,
                                    Object... placeholder) {
        String message = buildMessage(type, placeholder);
        result.setCode(type.getCode());
        result.setMessage(message);
    }

    /**
     * 功能描述: 构建消息
     * 
     * @param type 异常码类型
     * @param placeholder 替换信息
     * @return
     */
    public static String buildMessage(CodeType type,
                                      Object... placeholder) {
        return buildMessage(false, type, placeholder);
    }

    /**
     * 功能描述: 构建消息
     * 
     * @param codeFlag 错误消息开头是否添加错误码，true添加，false不添加
     * @param type 异常码类型
     * @param placeholder 替换信息
     * @return 格式化错误消息
     */
    public static String buildMessage(boolean codeFlag,
                                      CodeType type,
                                      Object... placeholder) {
        if (null == type) {
            type = CodeType.E999999;
        }
        if (placeholder == null) {
            placeholder = new Object[] {
                UNKNOWN
            };
        }
        String formatMessage = null;
        if (codeFlag) {
            formatMessage = type.buildMessageWithCode(placeholder);
        } else {
            formatMessage = type.buildMessage(placeholder);
        }
        return formatMessage;

    }

}
