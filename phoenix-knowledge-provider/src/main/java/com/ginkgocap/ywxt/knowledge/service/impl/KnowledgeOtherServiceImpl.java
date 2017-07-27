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
    public InterfaceResult collectKnowledge(long userId,long knowledgeId, int typeId, short privated)
    {
        return knowledgeCollectDao.collectKnowledge(userId, knowledgeId, typeId, privated);
    }

    @Override
    public InterfaceResult deleteCollectedKnowledge(long ownerId,long knowledgeId,int typeId)
    {
        return knowledgeCollectDao.deleteCollectedKnowledge(ownerId, knowledgeId, typeId);
    }

    @Override
    public boolean updateCollectedKnowledge(final KnowledgeCollect collect)
    {
        return knowledgeCollectDao.updateCollectedKnowledge(collect);
    }

    public boolean updateCollectedKnowledgePrivate(final long knowledgeId, final int typeId, final short privated)
    {
        return knowledgeCollectDao.updateCollectedKnowledgePrivate(knowledgeId, typeId, privated);
    }

    @Override
    public boolean isCollectedKnowledge(long userId,long knowledgeId, int typeId)
    {
        return knowledgeCollectDao.isCollectedKnowledge(userId, knowledgeId, typeId);
    }

    @Override
    public List<KnowledgeCollect> myCollectedKnowledgeByPage(long userId, long total, int typeId, int page, int size,String keyword)
    {
        return knowledgeCollectDao.myCollectedKnowledgeByPage(userId, total, typeId, page, size, keyword);
    }

    @Override
    public List<KnowledgeCollect> myCollectedKnowledgeByIndex(final long userId, final int typeId, int index, int size, final String keyword)
    {
        return knowledgeCollectDao.myCollectedKnowledgeByIndex(userId, typeId, index, size, keyword);
    }

    @Override
    public long myCollectKnowledgeCount(long userId)
    {
        return knowledgeCollectDao.myCollectKnowledgeCount(userId);
    }

    public List<KnowledgeCollect> getAllCollectKnowledge(final int page, final int size)
    {
        return knowledgeCollectDao.getAllCollectKnowledge(page, size);
    }

    @Override
    public InterfaceResult reportKnowledge(KnowledgeReport report)
    {
        return knowledgeReportDao.reportKnowledge(report);
    }
}
