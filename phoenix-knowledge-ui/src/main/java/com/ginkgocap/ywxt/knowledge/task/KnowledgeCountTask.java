package com.ginkgocap.ywxt.knowledge.task;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeCount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by oem on 1/17/17.
 */
@Repository("knowledgeCountTask")
public class KnowledgeCountTask implements Runnable, InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(KnowledgeCountTask.class);

    private BlockingQueue<KnowledgeCount> knowQueue = new ArrayBlockingQueue<KnowledgeCount>(1000);

    public void run() {

    }

    public void afterPropertiesSet() throws Exception {

    }
}
