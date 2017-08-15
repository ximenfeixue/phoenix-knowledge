package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.ywxt.framework.service.IdService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeIdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("knowledgeIdService")
public class KnowledgeIdServiceImpl implements KnowledgeIdService {
    private final static Logger logger = LoggerFactory.getLogger(KnowledgeIdServiceImpl.class);

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
