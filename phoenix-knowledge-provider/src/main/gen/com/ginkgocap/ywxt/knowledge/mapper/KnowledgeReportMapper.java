package com.ginkgocap.ywxt.knowledge.mapper;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeReport;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeReportExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface KnowledgeReportMapper {
    int countByExample(KnowledgeReportExample example);

    int deleteByExample(KnowledgeReportExample example);

    int insert(KnowledgeReport record);

    int insertSelective(KnowledgeReport record);

    List<KnowledgeReport> selectByExample(KnowledgeReportExample example);

    int updateByExampleSelective(@Param("record") KnowledgeReport record, @Param("example") KnowledgeReportExample example);

    int updateByExample(@Param("record") KnowledgeReport record, @Param("example") KnowledgeReportExample example);
}