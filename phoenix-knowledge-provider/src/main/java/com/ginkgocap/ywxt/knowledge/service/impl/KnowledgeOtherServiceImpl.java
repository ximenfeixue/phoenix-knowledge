package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeCollectDao;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMongoDao;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMysqlDao;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeReportDao;
import com.ginkgocap.ywxt.knowledge.model.*;
import com.ginkgocap.ywxt.knowledge.model.common.Constant;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeOtherService;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeBaseService;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeCommonService;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by Chen Peifeng on 2016/4/15.
 */

@Service("knowledgeOtherService")
public class KnowledgeOtherServiceImpl implements KnowledgeOtherService, KnowledgeBaseService
{
    @Autowired
    private KnowledgeReportDao knowledgeReportDao;

    @Autowired
    private KnowledgeCollectDao knowledgeCollectDao;

    @Override
    public InterfaceResult collectKnowledge(long userId,long knowledgeId, int type) throws Exception {
        return knowledgeCollectDao.collectKnowledge(userId, knowledgeId, type);
    }

    @Override
    public InterfaceResult deleteCollectedKnowledge(long ownerId,long knowledgeId,int typeId) throws Exception
    {
        return knowledgeCollectDao.deleteCollectedKnowledge(ownerId, knowledgeId, typeId);
    }

    @Override
    public boolean isCollectedKnowledge(long userId,long knowledgeId, int typeId)
    {
        return knowledgeCollectDao.isCollectedKnowledge(userId, knowledgeId, typeId);
    }

    @Override
    public List<KnowledgeCollect> myCollectKnowledge(long userId,int typeId,int start, int size,String keyword) throws Exception
    {
        return knowledgeCollectDao.myCollectKnowledge(userId, typeId, start, size, keyword);
    }

    @Override
    public long myCollectKnowledgeCount(long userId) throws Exception
    {
        return knowledgeCollectDao.myCollectKnowledgeCount(userId);
    }

    @Override
    public InterfaceResult reportKnowledge(KnowledgeReport report) throws Exception
    {
        return knowledgeReportDao.reportKnowledge(report);
    }
}
