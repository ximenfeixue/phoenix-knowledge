package com.ginkgocap.ywxt.knowledge.mapper;
 

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ginkgocap.ywxt.knowledge.model.ColumnVO;
/**
 * 栏目dao接口
 * <p>于2014-11-12 由 fuliwen 创建 </p>
 *
 */
public interface ColumnVOValueMapper {

    
    /**
     * 查询目录树
     * @param userId 用户id
     * @param pid 	父级id
     * @return 
     */
    List<ColumnVO>  selectColumnByPid(@Param("userId")long userId,@Param("pid")long pid);
   
}