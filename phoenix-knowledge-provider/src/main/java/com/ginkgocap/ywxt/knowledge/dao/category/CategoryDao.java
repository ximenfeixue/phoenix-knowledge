package com.ginkgocap.ywxt.knowledge.dao.category;


import java.util.List;

import com.ginkgocap.ywxt.knowledge.model.Category;

/**
 * 知识管理分类的Dao接口
 * @author lk
 * @创建时间：2013-03-29 10:40
 */
public interface CategoryDao {

    /**
     * 通过主键获得反馈记录
     * @param id
     * @return
     */
    Category selectByPrimaryKey(long id);
    /**
     * 通过phoenix_user.tb_user.id得到此用户下经过树形结构排序的所有分类
     * @param uid
     * @param state 分类状态 0:正常   1:删除 为空将查询出用户下所有分类记录
     * @return
     */
    List<Category> selectTreeOfSortByUserid(long uid,String state);
    /**
     * 插入类型
     * @param category
     * @return
     */
    Category insert(Category category);
    /**
     * 修改类型
     * @param category
     */
    void update(Category category);
    /**
     * 删除类型
     * @param id
     */
    void delete(long id);
    /**
     * 通过sortId得到此用户的分类
     * @param uid
     * @param sortId
     * @return
     */
	Category selectBySortId(long uid, String sortId);
    /**
     * 通过id得到子分类的个数
     * @param id
     * @return
     */
	long selectChildCountById(long id);
    /**
     * 通过父类的sortId得到某用户下某及的分类最大的sortId
     * @param uid
     * @param parentSortId
     * @return
     */
	String selectMaxSortId(long uid,String parentSortId);
    /**
     * 通过phoenix_user.tb_user.id得到此用户sortId下经过树形结构排序的所有分类
     * @param uid
     * @param sortId
     * @return
     */
	List<Category> selectChildBySortId(long uid,String sortId);
}
