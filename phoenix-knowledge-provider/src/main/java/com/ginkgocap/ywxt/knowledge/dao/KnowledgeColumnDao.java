package com.ginkgocap.ywxt.knowledge.dao;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeColumn;

public interface KnowledgeColumnDao {
    /**
     * insert <p>(新增知识栏目)</p>    
     * @param kc
     * @return knowledgeColumn
     */
    public KnowledgeColumn insert(KnowledgeColumn kc);
    
    /**
     * update <p>(更新知识栏目)</p>  
     * @param kc
     * @return knowledgeColumn
     */
    public KnowledgeColumn update(KnowledgeColumn kc);
    /**
     * queryById <p>(查询栏目)</p>      
     * @param id id
     * @return 知识栏目
     */
    public KnowledgeColumn queryById(long id);
    
    
    /**
     * countByPidAndName <p>(统计个数【根据上级栏目id和栏目名称】)</p>      
     * @param parentColumnId 上级栏目id
     * @param columnName 栏目名称
     * @return 
     */
    public long countByPidAndName(int parentColumnId,String columnName);
    
    /**
     * delById <p>(删除栏目)</p>  
     * @param id
     */
    public void delById(long id);

}
