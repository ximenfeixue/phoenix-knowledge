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
     * 通过父类的sortId得到某用户下某及的分类最大的sortId
     * @param uid
     * @param parentSortId
     * @param type 
     * @return
     */
    String selectMaxSortId(@Param("uid")long uid,@Param("parentSortId")String parentSortId, @Param("type")Byte type);
    /**
     * 通过phoenix_user.tb_user.id得到此用户sortId下经过树形结构排序的所有分类
     * @param uid
     * @param sortId
     * @return
     */
    List<UserCategory> selectChildBySortId(@Param("uid")long uid,@Param("sortId")String sortId,@Param("type")Byte type);
}