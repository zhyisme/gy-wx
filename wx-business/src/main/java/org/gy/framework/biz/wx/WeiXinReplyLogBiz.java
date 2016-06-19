package org.gy.framework.biz.wx;

import java.util.Date;

import org.gy.framework.biz.BaseBiz;
import org.gy.framework.dao.WeixinReplyLogMapper;
import org.gy.framework.model.WeixinReplyLog;
import org.springframework.stereotype.Service;

@Service
public class WeiXinReplyLogBiz extends BaseBiz {

    /**
     * 功能描述: 添加
     * 
     * @param entity
     * @return
     */
    public Long insert(WeixinReplyLog entity) {
        entity.setCreateTime(new Date());
        sqlSessionMaster.getMapper(WeixinReplyLogMapper.class).insertSelective(entity);
        return entity.getId();
    }

}
