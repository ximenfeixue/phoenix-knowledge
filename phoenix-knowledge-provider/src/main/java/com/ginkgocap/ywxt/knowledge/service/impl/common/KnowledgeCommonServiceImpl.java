package com.ginkgocap.ywxt.knowledge.service.impl.common;

import com.ginkgocap.ywxt.framework.cloud.id.DefaultIdGenerator;
import com.ginkgocap.ywxt.framework.service.IdService;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;

@Service("knowledgeCommonService")
public class KnowledgeCommonServiceImpl implements KnowledgeCommonService {
    private final static Logger logger = LoggerFactory.getLogger(KnowledgeCommonServiceImpl.class);

    @Autowired
    private IdService idService;

    @Override
    public Long getUniqueSequenceId()
    {
        return idService.getSequenceId("3");
    }

    @Override
    public Long getUniqueSequenceId(String prefix) {
        return idService.getSequenceId(prefix);
    }
}
