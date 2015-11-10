package com.ginkgocap.ywxt.knowledge.service;

import java.util.List; 

import com.ginkgocap.ywxt.knowledge.entity.UserCategory;
 
/**
 * 知识目录左树  
 * <p>于2014-9-16 由 bianzhiwei 创建 </p>
 * @author  <p>当前负责人 bianzhiwei</p>    
 *
 */
public interface UserCategoryService {
    /**
     * 通过主键获得类型
     * @param id
     * @return
     */
    UserCategory selectByPrimaryKey(long id);
 
    /**
     * 删除类型
     * @param id
     * @return String
     */
    String delete(long id);

    /**
     * 插入类型
     * @param UserCategory
     * @return
     */
    String insert(UserCategory category);

    /**
     * 修改类型
     * @param category
     */
    void update(UserCategory category);

    /**
     * 通过phoenix_user.tb_user.id得到此用户sortId下经过树形结构排序的所有分类
     * @param userid
     * @param sortId
     * @return
     */
    List<UserCategory> selectChildBySortId(long userId, String sortId,Byte type);

    /**
     * 查询目录树
     * @param userId 用户id
     * @param sortId 排序id :为空表示所有
     * @param status 状态:0正常 1删除
     * @return 
     */
    String selectUserCategoryTreeBySortId(long userId, String sortId,Byte type);
    
    /**
     * 查询编辑目录树
     * @param userId 用户id
     * @param sortId 排序id :为空表示所有
     * @param status 状态:0正常 1删除
     * @return 
     */
    String selecteidtUserCategoryTreeBySortId(long knowledgeid);
    
    /**
     * 查询子目录个数
     * @param id id
     * @return 个数
     */
    long selectChildCountById(long id);
    
    UserCategory selectByNameAndPid(String name,long pid,long userid);
    
    void checkNogroup(Long uid,List<Long> idtype);

    String selectUserCategoryTreeByParams(long userId, String sortId, Byte type, String columnType);
    
    /**
     * 根据条件查询目录
     * @param uid 用户id
     * @param pid 父id
     * @param type 目录类型
     * @param categoryname 目录名称
     * @return list
     */
    List<UserCategory> selectUserCategoryByParams(Long uid, long pid,long type, String categoryname);
    
    /**
     * 根据条件查询目录
     * @param uid 用户id
     * @param pid 父id
     * @param type 目录类型
     * @param sortid 排序id
     * @param categoryname 目录名称
     * @return list
     */
    List<UserCategory> selectUserCategoryByParam(Long uid, Long pid,int type, String sortid,String categoryname);

    /**
     *  查询目录中未分组ID
    * @param userid
    * @param sortId
    * @return
    */
   List<UserCategory> selectNoGroup(long userId, String sortId,Byte type);
   
   
   /** zhangzhen 加入
    * 默认目录id查询项
    * */
   String getDefaultCategoryId(long userId);
   
   
   List<UserCategory> selectUserCategoryList(long userId, String sortId, Byte type);
   
   
   List<UserCategory> getKnowledgeCategory(long knowledgeId);
   
   
   /**
	 * 批量删除目录
	 * @param ids
	 */
	String batchDeleteCategory(long userId,List<Long> ids);
	
	/**
	 * 查询用户最新目录
	 */
	List<UserCategory> selectUserCategoryByUserId(long userid);
	
	/**
	 * 通过PID分页查询用户目录
	 * @param userId
	 * @param pid
	 * @param page
	 * @param size
	 * @return
	 */
	List<UserCategory> selectUserCategoryByPid(long userId,long pid,int page,int size);
}
