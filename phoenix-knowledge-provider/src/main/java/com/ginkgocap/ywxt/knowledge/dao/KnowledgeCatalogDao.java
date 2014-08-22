package com.ginkgocap.ywxt.knowledge.dao;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeCatalog;

public interface KnowledgeCatalogDao {
    /**
     * insert <p>(新增知识文章目录)</p>    
     * @param kc
     * @return knowledgeCatalog
     */
    public KnowledgeCatalog insert(KnowledgeCatalog kc);
    
    /**
     * update <p>(更新知识文章目录)</p>  
     * @param kc
     * @return knowledgeCatalog
     */
    public KnowledgeCatalog update(KnowledgeCatalog kc);
    /**
     * queryById <p>(查询文章目录)</p>      
     * @param id id
     * @return 知识文章目录
     */
    public KnowledgeCatalog queryById(long id);
    

}
