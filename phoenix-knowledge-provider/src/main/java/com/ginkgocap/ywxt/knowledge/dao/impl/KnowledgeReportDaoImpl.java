package com.ginkgocap.ywxt.knowledge.dao.impl;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeReportDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeReport;
import com.ginkgocap.ywxt.knowledge.model.common.Constant;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created by gintong on 2016/8/17.
 */
@Repository("knowledgeReportDao")
public class KnowledgeReportDaoImpl extends BaseDao implements KnowledgeReportDao
{
    private final Logger logger = LoggerFactory.getLogger(KnowledgeReportDaoImpl.class);
    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public InterfaceResult reportKnowledge(KnowledgeReport report)
    {
        Query query = knowledgeColumnIdAndOwnerId(report.getUserId(), report.getKnowledgeId(), report.getColumnId());
        if (mongoTemplate.findOne(query, KnowledgeReport.class, Constant.Collection.KnowledgeReport) == null) {
            if (report.getCreateTime() <= 0) {
                report.setCreateTime(System.currentTimeMillis());
            }
            mongoTemplate.save(report, Constant.Collection.KnowledgeReport);
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
        } else {
            logger.info("This knowledge had reported..");
            return InterfaceResult.getSuccessInterfaceResultInstance("该知识已经被举报!");
        }
    }
}
