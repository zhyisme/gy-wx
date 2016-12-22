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
import org.springframework.stereotype.Service;
import ${entity.javaPackage}.${entity.className};

/**
 * 功能描述：${entity.tableComment}Biz
 *
 * @version 2.0.0
 * @author guanyang/14050360
 */
@Service
public class ${entity.className}Biz extends BaseBiz {

    /**
     * 功能描述: 添加
     * 
     * @param entity
     * @return
     * @version 2.0.0
     * @author guanyang/14050360
     */
    public Long insert(${entity.className} entity) {
        return dalClient.persist(entity, Long.class);
    }

    /**
     * 功能描述: 根据主键更新
     * 
     * @param entity
     * @return
     * @version 2.0.0
     * @author guanyang/14050360
     */
    public int update(${entity.className} entity) {
        return dalClient.dynamicMerge(entity);
    }

    /**
     * 功能描述: 根据主键删除
     * 
     * @param id
     * @return
     * @version 2.0.0
     * @author guanyang/14050360
     */
    public int delete(Long id) {
        ${entity.className} entity = new ${entity.className}();
        entity.setId(id);
        return dalClient.remove(entity);
    }

    /**
     * 功能描述: 根据主键查询实体
     * 
     * @param id
     * @return
     * @version 2.0.0
     * @author guanyang/14050360
     */
    public ${entity.className} select(Long id) {
        ${entity.className} entity = new ${entity.className}();
        entity.setId(id);
        return dalClientRead.find(${entity.className}.class, entity);
    }
    
    /**
     * 功能描述: 查询满足条件的记录
     * 
     * @param id
     * @return
     * @version 2.0.0
     * @author panchengliang/15061461
     */
    public List<${entity.className}> queryRecordList(${entity.className}Bo query) {
        Map<String, Object> paramMap = BeanUtil.convertBean(query);
        return dalClientRead.queryForList("${entity.lowerClassName}.FIND_BY_PAGE", paramMap, ${entity.className}.class);
    }
	/**
     * 功能描述: 查询满足条件的记录条数
     * 
     * @param id
     * @return
     * @version 2.0.0
     * @author panchengliang/15061461
     */
    public Integer queryCount(${entity.className}Bo query) {
        Map<String, Object> paramMap = BeanUtil.convertBean(query);
        return dalClientRead.queryForObject("${entity.lowerClassName}.COUNT_RECORD", paramMap, Integer.class);
    }
    
    
    
}
