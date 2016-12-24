package org.gy.framework.biz.wx;

import java.util.List;

import org.gy.framework.biz.BaseBiz;
import org.gy.framework.bo.WeixinReplyLogBo;
import org.gy.framework.dao.WeixinReplyLogMapper;
import org.gy.framework.model.WeixinReplyLog;
import org.gy.framework.model.WeixinReplyLogExample;
import org.springframework.stereotype.Service;

/**
 * 功能描述：微信回复日志Biz
 * 
 * @Date 2016-12-25 00:27:23
 */
@Service
public class WeixinReplyLogBiz extends BaseBiz {


    /**
     * 功能描述: 添加非空字段，返回主键
     * 
     * @param entity 实体
     * @return 主键
     * @Date 2016-12-25 00:27:23
     */
    public Long insertSelective(WeixinReplyLog entity) {
        sqlSessionMaster.getMapper(WeixinReplyLogMapper.class).insertSelective(entity);
        return entity.getId();
    }
    

    /**
     * 功能描述: 根据主键更新非空字段
     * 
     * @param entity 实体
     * @return 成功的条数
     * @Date 2016-12-25 00:27:23
     */
    public int updateByPrimaryKeySelective(WeixinReplyLog entity) {
        return sqlSessionMaster.getMapper(WeixinReplyLogMapper.class).updateByPrimaryKeySelective(entity);
    }
    
    /**
     * 功能描述: 根据主键查询
     *
     * @param id 主键
     * @return 实体
     * @Date 2016-12-25 00:27:23
     */
    public WeixinReplyLog selectByPrimaryKey(Long id) {
        return sqlSessionSlave.getMapper(WeixinReplyLogMapper.class).selectByPrimaryKey(id);
    }
    
    /**
     * 功能描述: 根据主键批量删除
     * 
     * @param ids 主键集合
     * @return 成功的条数
     * @Date 2016-12-25 00:27:23
     */
    public int deleteByPrimaryKey(List<Long> ids) {
        WeixinReplyLogExample example = new WeixinReplyLogExample();
        example.createCriteria().andIdIn(ids);
        return sqlSessionMaster.getMapper(WeixinReplyLogMapper.class).deleteByExample(example);
    }
    
    /**
     * 功能描述: 根据主键删除
     * 
     * @param id 主键
     * @return 成功的条数
     * @Date 2016-12-25 00:27:23
     */
	public int deleteByPrimaryKey(Long id){
        return sqlSessionMaster.getMapper(WeixinReplyLogMapper.class).deleteByPrimaryKey(id);
    }        
    
    /**
     * 功能描述: 获取满足条件的记录列表，带分页
     * 
     * @param query
     * @return
     * @Date 2016-12-25 00:27:23
     */
    public List<WeixinReplyLog> queryForList(WeixinReplyLogBo query) {
        return sqlSessionSlave.selectList("WEIXIN_REPLY_LOG.QUERY_FOR_LIST", query);
    }
    /**
     * 功能描述: 获取满足条件的记录数量
     * 
     * @param query
     * @return
     * @Date 2016-12-25 00:27:23
     */
    public int queryForCount(WeixinReplyLogBo query) {
        return sqlSessionSlave.selectOne("WEIXIN_REPLY_LOG.QUERY_FOR_COUNT", query);
    }
    
    
    
}
