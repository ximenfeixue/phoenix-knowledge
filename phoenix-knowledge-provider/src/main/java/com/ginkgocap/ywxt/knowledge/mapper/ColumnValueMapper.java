package com.ginkgocap.ywxt.knowledge.mapper;
 

import java.util.List;

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
    List<Column>  selectCategoryTreeBySortId(long userId,String sortId);
}