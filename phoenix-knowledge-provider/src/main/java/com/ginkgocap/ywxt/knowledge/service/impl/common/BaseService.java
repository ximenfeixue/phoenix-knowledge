package com.ginkgocap.ywxt.knowledge.service.impl.common;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeCollectDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by oem on 8/15/17.
 */
public class BaseService {
    @Autowired
    protected KnowledgeCollectDao knowledgeCollectDao;

}
