package org.gy.framework.controller;

import org.gy.framework.biz.wx.WeiXinMenuConfigBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

@Controller
@RequestMapping("/menuConfig")
public class WeiXinMenuConfigController extends BaseController {

    @Autowired
    private WeiXinMenuConfigBiz weiXinMenuConfigBiz;

    @RequestMapping(value = "/createMenu", method = RequestMethod.GET)
    public ModelAndView createMenu() {
        ModelAndView mav = new ModelAndView(new MappingJacksonJsonView());
        mav.addObject("response", weiXinMenuConfigBiz.createMenu());
        return mav;
    }

}
