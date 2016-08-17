package com.ginkgocap.ywxt.knowledge.dao;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeReport;
import com.gintong.frame.util.dto.InterfaceResult;

/**
 * Created by gintong on 2016/8/17.
 */
public interface KnowledgeReportDao {
    public InterfaceResult reportKnowledge(KnowledgeReport report) throws Exception;
}
