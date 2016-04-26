package com.ginkgocap.ywxt.knowledge.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;

/**
 * Created by Chen Peifeng on 2016/4/26.
 */
public class MyObjectMapper extends ObjectMapper {
    /**
     * A default version UID to use when serializing an instance of this class.
     */
    private static final long serialVersionUID = 1L;

    public MyObjectMapper() {
        setFilters(KnowledgeUtil.assoSimpleFilterProvider(null));
    }
}