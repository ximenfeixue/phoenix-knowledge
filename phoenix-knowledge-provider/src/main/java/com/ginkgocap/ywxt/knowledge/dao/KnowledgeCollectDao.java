package com.ginkgocap.ywxt.knowledge.dao;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeCollect;
import com.gintong.frame.util.dto.InterfaceResult;

import java.util.List;

/**
 * Created by gintong on 2016/8/17.
 */
public interface KnowledgeCollectDao {
    public InterfaceResult collectKnowledge(long userId,long knowledgeId, int typeId) throws Exception;

    public InterfaceResult deleteCollectedKnowledge(long ownerId,long knowledgeId,int typeId) throws Exception;

    public boolean isCollectedKnowledge(long userId,long knowledgeId, int typeId);

    public List<KnowledgeCollect> myCollectKnowledge(long userId,int typeId,int start, int size, String keyword) throws Exception;

    public long myCollectKnowledgeCount(long userId) throws Exception;
}
