package com.ginkgocap.ywxt.knowledge.mapper;
 

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.ginkgocap.ywxt.knowledge.entity.Column;
/**
 * 栏目dao接口
 * <p>于2014-9-18 由 bianzhiwei 创建 </p>
 * @author  <p>当前负责人 bianzhiwei</p>   
 *
 */
public interface ColumnValueMapper {
    /**
     * 查询目录树
     * @param userId 用户id
     * @param sortId 排序id :为空表示所有
     * @return 
     */
    List<Column>  selectCategoryTreeBySortId(@Param("userId")long userId,@Param("sortId")String sortId);
    /**
     * 查询用户订阅的所有栏目
     * @param userId 用户id
     * @return 用户订阅的所有栏目列表
     */
    public List<Column> selectSubByUserId(long userId);
    
    /**
     * 根据属性查询栏目
     * @param pid 父id
     * @param column 栏目id
     * @param userId 用户id
     * @return
     */
    public List<Column> selectByParam(@Param("pid")long pid ,@Param("column")long column ,@Param("userId")long userId);
}