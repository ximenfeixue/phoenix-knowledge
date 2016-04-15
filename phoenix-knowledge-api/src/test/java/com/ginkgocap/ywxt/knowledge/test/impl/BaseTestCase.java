package com.ginkgocap.ywxt.knowledge.test.impl;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import junit.framework.TestCase;

/**
 * Created by Admin on 2016/4/15.
 */
public abstract class BaseTestCase extends TestCase {
    protected long userId = KnowledgeUtil.getDummyUser().getId();
    protected short columnId = 2;

    protected long knowledgeId()
    {
        long minInt = 1; long maxInt = 100000;
        return (Math.round(Math.random() * (maxInt - minInt) + minInt));
    }
}
