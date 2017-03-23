package com.ginkgocap.ywxt.knowledge.task;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeCollect;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollect;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeOtherService;
import com.ginkgocap.ywxt.knowledge.service.PermissionServiceLocal;
import com.gintong.common.phoenix.permission.entity.Permission;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by oem on 3/23/17.
 */
public class DataMigrateTask implements Runnable, InitializingBean {
    private final Logger logger = LoggerFactory.getLogger(DataMigrateTask.class);

    @Autowired
    private KnowledgeOtherService knowledgeOtherService;

    @Autowired
    private PermissionServiceLocal permissionService;

    public void run() {
        updateCollectKnowledgePermission();
    }

    private void updateCollectKnowledgePermission() {
        int total = 0;
        int page = 0;
        final int size = 30;
        List<KnowledgeCollect> collectKnowList = knowledgeOtherService.getAllCollectKnowledge(page++, size);
        while (CollectionUtils.isNotEmpty(collectKnowList)) {
            for (KnowledgeCollect collect : collectKnowList) {
                if (collect != null) {
                    Permission perm = permissionService.getPermissionInfo(collect.getKnowledgeId());
                    final short privated = DataCollect.privated(perm);
                    collect.setPrivated(privated);
                    knowledgeOtherService.updateCollectedKnowledge(collect);
                    logger.info("update collected knolwedge permision. knowledgeId: " + collect.getKnowledgeId());
                }
            }
            total += collectKnowList.size();
            collectKnowList = knowledgeOtherService.getAllCollectKnowledge(page++, size);
        }
        logger.info("collectd knowledge migrated size: " + total);
    }

    private void updateKnowledgeBasePermission() {
        int total = 0;
        int page = 0;
        final int size = 30;
        List<KnowledgeCollect> collectKnowList = knowledgeOtherService.getAllCollectKnowledge(page++, size);
        while (CollectionUtils.isNotEmpty(collectKnowList)) {
            for (KnowledgeCollect collect : collectKnowList) {
                if (collect != null) {
                    Permission perm = permissionService.getPermissionInfo(collect.getKnowledgeId());
                    final short privated = DataCollect.privated(perm);
                    collect.setPrivated(privated);
                    knowledgeOtherService.updateCollectedKnowledge(collect);
                    logger.info("update collected knolwedge permision. knowledgeId: " + collect.getKnowledgeId());
                }
            }
            total += collectKnowList.size();
            collectKnowList = knowledgeOtherService.getAllCollectKnowledge(page++, size);
        }
        logger.info("collectd knowledge migrated size: " + total);
    }

    public void afterPropertiesSet() throws Exception {
        logger.info("DataMigrateTask begining...");
        new Thread(this).start();
        logger.info("DataMigrateTask complete...");
    }
}
