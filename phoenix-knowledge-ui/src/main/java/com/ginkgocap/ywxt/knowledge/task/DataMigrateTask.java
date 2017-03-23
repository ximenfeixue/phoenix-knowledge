package com.ginkgocap.ywxt.knowledge.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by oem on 3/23/17.
 */
public class DataMigrateTask implements Runnable, InitializingBean {
    private final Logger logger = LoggerFactory.getLogger(DataMigrateTask.class);

    public void run() {

    }

    public void afterPropertiesSet() throws Exception {
        logger.info("DataMigrateTask begining...");
        new Thread(this).start();
        logger.info("DataMigrateTask complete...");
    }
}
