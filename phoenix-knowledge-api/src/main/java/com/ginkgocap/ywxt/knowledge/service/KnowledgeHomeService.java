package com.ginkgocap.ywxt.knowledge.service;


import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.entity.Column;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStatics;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeColumn;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;

/** 
 * <p>知识首页操作接口</p>  
 * <p>于2014-8-19 由 bianzhiwei 创建 </p>
 * @author  <p>当前负责人 bianzhiwei</p>     
 * @since <p>1.2.1-SNAPSHO</p> 
 */
public interface KnowledgeHomeService {

    /**
     * 查询分类
     * @param userId 用户id
     * @param column 栏目
     * @param pid 父id
     * @return
     */
    public List<Column>getTypeList(long userId,long column,long pid);

    /**
     * 查询知识
     * @param t类型实例 
     * @param type 类型id
     * @param isBigColumn 是否为大栏目
     * @param columnid 栏目id
     * @param userid 用户id
     * @param page 当前页码
     * @param size 每页大小
     * @return
     */
    public <T> List<T> selectAllByParam(T t, long type, boolean isBigColumn, Long columnid, Long userid, int page, int size);

    /**
     * 查询排行
     * @param type 类型
     * @return
     */
    public List<KnowledgeStatics> getRankList(Short type);

}
