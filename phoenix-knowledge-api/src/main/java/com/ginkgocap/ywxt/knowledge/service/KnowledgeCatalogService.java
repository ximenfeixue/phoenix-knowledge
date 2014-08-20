package com.ginkgocap.ywxt.knowledge.service;

import java.util.List;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeCatalog;

/** 
 * <p>知识目录操作接口</p>  
 * <p>于2014-8-19 由 bianzhiwei 创建 </p>
 * @author  <p>当前负责人 bianzhiwei</p>     
 * @since <p>1.2.1-SNAPSHO</p> 
 */
public interface KnowledgeCatalogService {

    /**
     * saveOrUpdate <p>(保存知识目录)</p>    
     * @param kc
     * @return knowledgeCatalog
     */
    public KnowledgeCatalog saveOrUpdate(KnowledgeCatalog kc);
    
    /**
     * queryById <p>(查询目录)</p>      
     * @param id id
     * @return 知识目录
     */
    public KnowledgeCatalog queryById(long id);
    
}
