package com.ginkgocap.ywxt.knowledge.mapper;

import com.ginkgocap.ywxt.knowledge.entity.KnowledgeConnectInfo;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeConnectInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface KnowledgeConnectInfoMapper {
    int countByExample(KnowledgeConnectInfoExample example);

    int deleteByExample(KnowledgeConnectInfoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(KnowledgeConnectInfo record);

    int insertSelective(KnowledgeConnectInfo record);

    List<KnowledgeConnectInfo> selectByExample(KnowledgeConnectInfoExample example);

    KnowledgeConnectInfo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") KnowledgeConnectInfo record, @Param("example") KnowledgeConnectInfoExample example);

    int updateByExample(@Param("record") KnowledgeConnectInfo record, @Param("example") KnowledgeConnectInfoExample example);

    int updateByPrimaryKeySelective(KnowledgeConnectInfo record);

    int updateByPrimaryKey(KnowledgeConnectInfo record);
}