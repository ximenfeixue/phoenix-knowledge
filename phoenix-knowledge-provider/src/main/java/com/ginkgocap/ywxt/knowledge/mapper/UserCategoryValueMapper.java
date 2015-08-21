package com.ginkgocap.ywxt.knowledge.mapper;

import com.ginkgocap.ywxt.knowledge.entity.UserCategory;  
import java.util.List;

import org.apache.ibatis.annotations.Param;
/**
 * 左树dao接口 
 * <p>于2014-9-18 由 bianzhiwei 创建 </p>
 * @author  <p>当前负责人 bianzhiwei</p>   
 *
 */
public interface UserCategoryValueMapper {
    /**
     * 通过id得到子分类的个数
     * @param id
     * @return
     */
    long selectChildCountById(long id);
    
    /**
     * 删除目录
     * @param id
     * @return
     */
    long del(@Param("userId")long userId,@Param("categoryType")long categoryType,@Param("sortid")String sortid);
    
    /**
     * 删除目录时删除目录知识
     * @param userId 用户id
     * @param categoryType 目录类型
     * @param cid 目录id
     * @param sortid 排序id
     * @return long
     */
    long delk(@Param("userId")long userId,@Param("categoryType")long categoryType,@Param("cid")long cid,@Param("sortid")String sortid);
    
    /**
     * 删除目录时删除收藏知识
     * @param userId 用户id
     * @param categoryType 目录类型
     * @param cid 目录id
     * @param sortid 排序id
     * @return long
     */
    long delc(@Param("userId")long userId,@Param("categoryType")long categoryType,@Param("cid")long cid,@Param("sortid")String sortid);
    
    /**
     * 通过父类的sortId得到某用户下某及的分类最大的sortId
     * @param uid
     * @param parentSortId
     * @param type 
     * @return
     */
    String selectMaxSortId(@Param("uid")long uid,@Param("parentSortId")String parentSortId, @Param("type")Short type);
    /**
     * 通过phoenix_user.tb_user.id得到此用户sortId下经过树形结构排序的所有分类
     * @param uid
     * @param sortId
     * @return
     */
    List<UserCategory> selectChildBySortId(@Param("uid")long uid,@Param("sortId")String sortId,@Param("type")Byte type);
    
    List<UserCategory> selectChildByParentId(@Param("parentId") long parentId);
    
    long batchDeleteCategory(@Param("ids") String []ids); 
}