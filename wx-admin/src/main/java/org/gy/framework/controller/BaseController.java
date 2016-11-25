package org.gy.framework.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

public class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public ModelAndView errorView() {
        return new ModelAndView("redirect:/error.html");
    }

}
