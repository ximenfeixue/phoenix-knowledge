package com.ginkgocap.ywxt.knowledge.service;


import java.util.List;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeColumn;

/** 
 * <p>知识栏目操作接口</p>  
 * <p>于2014-8-19 由 bianzhiwei 创建 </p>
 * @author  <p>当前负责人 guangyuan</p>     
 * @since <p>1.2.1-SNAPSHOT</p> 
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
    
    /**
     * queryByParentId根据上级栏目查询子栏目
     * @param parentColumnId 父栏目id
     * @param createUserId 创建者名称
     * @return 栏目列表
     */
    public List<KnowledgeColumn> queryByParentId(int parentColumnId,int createUserId);
    /**
     * queryByUserId  查询用户创建的所有栏目
     * @param createUserId 用户id
     * @return 用户创建的所有栏目列表
     */
    public List<KnowledgeColumn> queryByUserId(long createUserId);
    /**
     * queryByUserId  查询用户订阅的所有栏目
     * @param createUserId 用户id
     * @return 用户订阅的所有栏目列表
     */
    public List<KnowledgeColumn> querySubByUserId(long createUserId);
    /**
     * queryByUserId  查询系统所有可订阅的栏目
     * @param systemId 系统用户id
     * @return 系统栏目列表
     */
    public List<KnowledgeColumn> querySubBySystem(long systemId);
    
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
    public void recoverOneKC(Long id);
    
    /**
     * clearById <p>(删除栏目, 物理删除 ,数据库中将清除此条数据，慎用！！！)</p>  
     * <p>只有已删除状态的数据才能被清空</p>  
     * @param id
     */
    public void clearById(long id);

    /**
     * 查询目录树
     * @param userId 用户id
     * @param sortId 排序id :为空表示所有
     * @param status 状态:0正常 1删除
     * @return 
     */
    String selectColumnTreeBySortId(long userId,String sortId,String status);
}
