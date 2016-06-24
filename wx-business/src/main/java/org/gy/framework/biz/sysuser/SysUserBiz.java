package org.gy.framework.biz.sysuser;

import java.util.Date;
import java.util.List;

import org.gy.framework.biz.BaseBiz;
import org.gy.framework.dao.SysUserMapper;
import org.gy.framework.model.SysUser;
import org.gy.framework.model.SysUserExample;
import org.springframework.stereotype.Service;

@Service
public class SysUserBiz extends BaseBiz {

    /**
     * 功能描述: 根据账号名获取用户信息(登录认证)
     * 
     * @param accountName
     * @return
     */
    public SysUser findSysUserByAccountName(String accountName) {
        SysUserExample example = new SysUserExample();
        example.createCriteria().andAccountNameEqualTo(accountName);
        List<SysUser> list = sqlSessionSlave.getMapper(SysUserMapper.class).selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 功能描述: 添加
     *
     * @param entity
     * @return
     */
    public Long insert(SysUser entity) {
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        sqlSessionMaster.getMapper(SysUserMapper.class).insert(entity);
        return entity.getId();
    }

}
