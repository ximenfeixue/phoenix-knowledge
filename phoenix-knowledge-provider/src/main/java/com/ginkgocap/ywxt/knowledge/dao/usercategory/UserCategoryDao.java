package com.ginkgocap.ywxt.knowledge.dao.usercategory;


import java.util.List;

import com.ginkgocap.ywxt.knowledge.model.UserCategory;

/**
 * 知识目录左树  
 * <p>于2014-9-16 由 bianzhiwei 创建 </p>
 * @author  <p>当前负责人 bianzhiwei</p>   
 *
 */
public interface UserCategoryDao {

    /**
     * 通过主键获得反馈记录
     * @param id
     * @return
     */
    UserCategory selectByPrimaryKey(long id);

    /**
     * 插入类型
     * @param UserCategory
     * @return
     */
    UserCategory insert(UserCategory UserCategory);
    /**
     * 修改类型
     * @param UserCategory
     */
    void update(UserCategory UserCategory);
    /**
     * 删除类型
     * @param id
     */
    void delete(long id);

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
	List<UserCategory> selectChildBySortId(long uid,String sortId);

}
