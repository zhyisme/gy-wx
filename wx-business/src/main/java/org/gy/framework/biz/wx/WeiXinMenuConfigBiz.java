package org.gy.framework.biz.wx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gy.framework.biz.BaseBiz;
import org.gy.framework.bo.WeixinMenuConfigBo;
import org.gy.framework.util.json.JacksonMapper;
import org.gy.framework.weixin.api.menu.MenuCreate;
import org.gy.framework.weixin.config.Configurable;
import org.gy.framework.weixin.util.GeneralResponse;
import org.gy.framework.weixin.util.WeiXinConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeiXinMenuConfigBiz extends BaseBiz {

    @Autowired
    private WeiXinBiz weiXinBiz;

    /**
     * 功能描述: 创建菜单
     * 
     * @return
     */
    public GeneralResponse createMenu() {
        Configurable configurable = weiXinBiz.getConfigurable();
        // 获取token
        String token = weiXinBiz.getToken();
        MenuCreate menuCreate = new MenuCreate(configurable);
        menuCreate.setAccessToken(token);
        menuCreate.setMenuJson(getMenuJson());
        String result = menuCreate.execute();

        return JacksonMapper.jsonToBean(result, GeneralResponse.class);
    }

    public List<WeixinMenuConfigBo> selectMenuList() {
        return sqlSessionSlave.selectList("WEIXIN_MENU_CONFIG.SELECT_MENU_LIST");
    }

    public String getMenuJson() {
        List<WeixinMenuConfigBo> list = selectMenuList();
        Map<Long, WeixinMenuConfigBo> parentMap = new HashMap<Long, WeixinMenuConfigBo>();
        Map<Long, List<WeixinMenuConfigBo>> childMap = new HashMap<Long, List<WeixinMenuConfigBo>>();
        for (WeixinMenuConfigBo bo : list) {
            if (bo.getParentId() == null || bo.getParentId() == -1) {
                parentMap.put(bo.getId(), bo);
            } else {
                if (childMap.get(bo.getParentId()) == null) {
                    List<WeixinMenuConfigBo> childList = new ArrayList<WeixinMenuConfigBo>();
                    childList.add(bo);
                    childMap.put(bo.getParentId(), childList);
                } else {
                    childMap.get(bo.getParentId()).add(bo);
                }
            }
        }
        List<Object> MenuList = new ArrayList<Object>();
        for (Map.Entry<Long, WeixinMenuConfigBo> entry : parentMap.entrySet()) {
            WeixinMenuConfigBo bo = entry.getValue();
            String name = bo.getMenuName();
            if (childMap.get(entry.getKey()) == null) {
                // 没有子菜单
                Map<String, Object> parentMenuContent = new HashMap<String, Object>();
                String type = bo.getMenuType();
                parentMenuContent.put("type", type);
                parentMenuContent.put("name", name);
                if (WeiXinConstantUtil.MENU_TYPE_CLICK.equals(type)) {
                    Long key = bo.getReplyId();
                    parentMenuContent.put("key", key);
                    MenuList.add(parentMenuContent);
                } else if (WeiXinConstantUtil.MENU_TYPE_VIEW.equals(type)) {
                    String url = bo.getReplyLink();
                    parentMenuContent.put("url", url);
                    MenuList.add(parentMenuContent);
                }
            } else {
                // 有子菜单
                List<WeixinMenuConfigBo> subMenuList = childMap.get(entry.getKey());
                Map<String, Object> parentMenuContent = new HashMap<String, Object>();
                parentMenuContent.put("name", name);
                List<Object> subMenuContentList = new ArrayList<Object>();
                for (WeixinMenuConfigBo subMenu : subMenuList) {
                    String subMenuType = subMenu.getMenuType();
                    String subMenuname = subMenu.getMenuName();
                    Map<String, Object> subMenuContent = new HashMap<String, Object>();
                    subMenuContent.put("type", subMenuType);
                    subMenuContent.put("name", subMenuname);
                    if (WeiXinConstantUtil.MENU_TYPE_CLICK.equals(subMenuType)) {
                        Long subMenuKey = subMenu.getReplyId();
                        subMenuContent.put("key", subMenuKey);
                        subMenuContentList.add(subMenuContent);
                    } else if (WeiXinConstantUtil.MENU_TYPE_VIEW.equals(subMenuType)) {
                        String subMenuUrl = subMenu.getReplyLink();
                        subMenuContent.put("url", subMenuUrl);
                        subMenuContentList.add(subMenuContent);
                    }
                }
                if (subMenuContentList.size() > 0) {
                    parentMenuContent.put("sub_button", subMenuContentList);
                    MenuList.add(parentMenuContent);
                }
            }
        }
        Map<String, Object> menuMap = new HashMap<String, Object>();
        menuMap.put("button", MenuList);
        return JacksonMapper.beanToJson(menuMap);
    }

}
