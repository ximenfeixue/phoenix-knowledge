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
     * 查询子目录个数
     * @param id id
     * @return 个数
     */
    long selectChildCountById(long id);
    
    UserCategory selectByNameAndPid(String name,long pid);
    
    void checkNogroup(Long uid,List<Long> idtype);

    String selectUserCategoryTreeByParams(long userId, String sortId, Byte type, String columnType);
}
