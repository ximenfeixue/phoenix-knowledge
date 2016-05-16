package com.ginkgocap.ywxt.knowledge.service;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeReport;
import com.ginkgocap.ywxt.knowledge.model.TagItems;
import com.gintong.frame.util.dto.InterfaceResult;

import java.util.List;

/**
 * Created by Admin on 2016/4/15.
 */
public interface KnowledgeOtherService
{
    public InterfaceResult collectKnowledge(long userId,long knowledgeId, short columnId) throws Exception;

    public InterfaceResult deleteCollectedKnowledge(long userId,long knowledgeId, short columnId) throws Exception;

    public InterfaceResult reportKnowledge(KnowledgeReport report) throws Exception;

    public InterfaceResult batchTags(List<TagItems> tagItems,long userId) throws Exception;

    public InterfaceResult batchCatalogs(List<TagItems> tagItems,long userId) throws Exception;
}
