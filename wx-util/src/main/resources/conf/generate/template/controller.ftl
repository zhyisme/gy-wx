/*
 * Copyright (C), 2002-2016, 苏宁易购电子商务有限公司
 * FileName: ${entity.fileName}.java
 * Author:   guanyang/14050360
 * Date:     ${entity.createDate?string("yyyy-MM-dd HH:mm:ss")}
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package ${entity.javaPackage};
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.suning.pai.bussiness.${entity.className}Biz;
import com.suning.pai.bussiness.bo.${entity.className}Bo;

/**
 * 
 * 功能描述: ${entity.className}报表
 * 
 * @version 2.0.0
 * @author panChengLiang/15061461
 */
@Controller
@RequestMapping("/${entity.lowerClassName}")
public class ${entity.className}Controller extends BaseController{
    
    @Autowired
    private ${entity.className}Biz           ${entity.lowerClassName}Biz;
    
    /**
     * 
     * 功能描述: 跳转到列表页面
     * 
     * @return ModelAndView
     * @version 2.0.0
     * @author panchengliang/15061461
     */
    @RequestMapping(value ="/index", method = RequestMethod.GET)
    public ModelAndView weiXinBuyList() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("${entity.lowerClassName}/${entity.lowerClassName}List.ftl");
        return mav;
    }

    /**
     * 
     * 功能描述: 分页查询记录
     * 
     * @param query
     * @return ModelAndView
     * @version 2.0.0
     * @author panchengliang/15061461
     */
    @RequestMapping(value = "/query${entity.className}List", method = RequestMethod.POST)
    public ModelAndView query${entity.className}(${entity.className}Bo query) {
        ModelAndView mav = new ModelAndView(new MappingJacksonJsonView());
        query.setIndex((query.getPage() - 1) * query.getRows());
        List<${entity.className}> bos =  ${entity.lowerClassName}Biz.queryRecordList(query);
        Integer total =${entity.lowerClassName}Biz.queryCount(query);
        mav.addObject("total", total);
        mav.addObject("rows",bos);
        return mav;
    }
    
    
    
}
