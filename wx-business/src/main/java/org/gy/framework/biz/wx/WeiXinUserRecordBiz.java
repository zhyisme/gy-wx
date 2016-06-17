package org.gy.framework.biz.wx;

import java.util.Date;

import org.gy.framework.biz.BaseBiz;
import org.gy.framework.dao.WeixinUserRecordMapper;
import org.gy.framework.model.WeixinUserRecord;
import org.gy.framework.model.WeixinUserRecordExample;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
public class WeiXinUserRecordBiz extends BaseBiz {

    /**
     * 功能描述: 添加
     * 
     * @param entity
     * @return
     */
    public Long insert(WeixinUserRecord entity) {
        sqlSessionMaster.getMapper(WeixinUserRecordMapper.class).insertSelective(entity);
        return entity.getId();
    }

    /**
     * 功能描述: 根据主键更新
     * 
     * @param entity
     * @return
     */
    public int update(WeixinUserRecord entity) {
        return sqlSessionMaster.getMapper(WeixinUserRecordMapper.class).updateByPrimaryKeySelective(entity);
    }

    /**
     * 功能描述: 根据openId更新
     * 
     * @param openId
     * @return
     */
    public int updateByOpenId(String openId) {
        WeixinUserRecordExample example = new WeixinUserRecordExample();
        example.createCriteria().andOpenIdEqualTo(openId);
        WeixinUserRecord entity = new WeixinUserRecord();
        // entity.setOpenId(openId);
        entity.setUpdateTime(new Date());
        return sqlSessionMaster.getMapper(WeixinUserRecordMapper.class).updateByExampleSelective(entity, example);
    }

    /**
     * 功能描述: 根据openId添加或更新一条记录
     * 
     * @param openId
     */
    public void addOrUpdateUserRecord(String openId) {
        int result = updateByOpenId(openId);
        if (result == 0) {
            WeixinUserRecord entity = new WeixinUserRecord();
            entity.setOpenId(openId);
            entity.setCreateTime(new Date());
            entity.setUpdateTime(new Date());
            try {
                insert(entity);
            } catch (DuplicateKeyException e) {
                // 并发时出现冲突异常，说明openId已经存在，此时只需要更新即可
                updateByOpenId(openId);
            }
        }
    }

}
