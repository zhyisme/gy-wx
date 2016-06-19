package org.gy.framework.weixin.api.menu;

import org.gy.framework.weixin.api.ApiExecutor;
import org.gy.framework.weixin.api.MethodType;
import org.gy.framework.weixin.config.Configurable;

/**
 * 功能描述：菜单创建
 * 
 */
public class MenuCreate extends ApiExecutor {

    /**
     * 菜单数据
     */
    private String menuJson;

    public MenuCreate(Configurable configurable) {
        super(configurable);
    }

    @Override
    public String getBodyContent() {
        return menuJson;
    }

    @Override
    public MethodType getMethodType() {
        return MethodType.POST;
    }

    public String getMenuJson() {
        return menuJson;
    }

    public void setMenuJson(String menuJson) {
        this.menuJson = menuJson;
    }

}
