package com.ginkgocap.ywxt.knowledge.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ginkgocap.ywxt.knowledge.model.AdminUserCategory;

/**
 * 
 * @Title: KnowledgeTagValueMapper.java
 * @Package com.ginkgocap.ywxt.knowledge.mapper 
 * @Description:
 * @author caihe
 * @date 2015-10-28 上午11:10:31
 */
public interface KnowledgeTagValueMapper {

	int batchInsert(@Param("knowledgeid") long knowledgeid, @Param("list") List<Long> ids);
	
	 long deleteTAG(@Param("tagid") long tagid, @Param("knowledgeid") long knowledgeid); 

}