package com.ginkgocap.ywxt.knowledge.service;


import com.ginkgocap.ywxt.knowledge.model.KnowledgeColumn;

/** 
 * <p>知识栏目操作接口</p>  
 * <p>于2014-8-19 由 bianzhiwei 创建 </p>
 * @author  <p>当前负责人 bianzhiwei</p>     
 * @since <p>1.2.1-SNAPSHO</p> 
 */
public interface KnowledgeColumnService {

    /**
     * saveOrUpdate <p>(保存知识栏目)</p>    
     * @param kc
     * @return knowledgeColumn
     */
    public KnowledgeColumn saveOrUpdate(KnowledgeColumn kc);
    
    /**
     * queryById <p>(查询栏目)</p>      
     * @param id id
     * @return 知识栏目
     */
    public KnowledgeColumn queryById(long id);
    
    /**
     * isExist <p>(是否存在)</p>      
     * @param parentColumnId 上级栏目id
     * @param columnName 栏目名称
     * @return true:存在 false:不存在
     */
    public boolean isExist(int parentColumnId,String columnName);
    
    /**
     * delById <p>(删除栏目)</p>  
     * @param id
     */
    public void delById(long id);
    
}
