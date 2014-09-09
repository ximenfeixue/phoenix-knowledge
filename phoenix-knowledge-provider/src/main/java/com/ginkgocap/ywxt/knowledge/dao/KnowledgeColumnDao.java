package com.ginkgocap.ywxt.knowledge.dao;

import java.util.List;

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
    public long countByPidAndName(long parentColumnId,String columnName);
    
    /**
     * delById <p>(删除栏目)</p>  
     * @param id
     */
    public void delById(long id);
    
    /**
     * queryByParentId 根据栏目id查询其所有一级子栏目
     * @param parentColumnId 父栏目id
     * @param createUserId 用户id
     * @return 子栏目列表
     */
    public List<KnowledgeColumn> queryByParentId(long parentColumnId,long createUserId);
    
    /**
     * queryByUserId  查询用户创建的所有栏目
     * @param createUserId 用户id
     * @return 用户创建的所有栏目列表
     */
    public List<KnowledgeColumn> queryByUserId(long createUserId);
    
    /**
     * queryByUserId  查询用户创建的所有栏目和系统栏目
     * @param createUserId 用户id
     * @param systemId 系统用户id
     * @return 用户创建的所有栏目和系统栏目列表
     */
    public List<KnowledgeColumn> queryByUserIdAndSystem(long createUserId,long systemId);
    
    /**
     * queryAll 查询所有未删除的栏目
     * @return 所有栏目列表
     */
    public List<KnowledgeColumn> queryAll();
    
    /**
     * queryAll 查询所有已删除的栏目
     * @return 所有已删除栏目列表
     */
    public List<KnowledgeColumn> queryAllDel();
    
    /**
     * recoverOneKC 恢复一个已删除的栏目
     * @return none
     */
    public void recoverOneKC(long id);

}
