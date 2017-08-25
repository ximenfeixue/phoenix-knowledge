package com.ginkgocap.ywxt.knowledge.task;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeCollect;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollect;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeOtherService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeService;
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
    private KnowledgeService knowledgeService;

    @Autowired
    private KnowledgeOtherService knowledgeOtherService;

    @Autowired
    private PermissionServiceLocal permissionService;

    public void run() {
        //updateCollectKnowledgePermission();
        updateKnowledgeBase();
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

    private void updateKnowledgeBasePermission(KnowledgeBase base) {
        Permission perm = permissionService.getPermissionInfo(base.getId());
        final short privated = DataCollect.privated(perm);
        base.setPrivated(privated);
    }

    private void updateKnowledgeBase() {
        if (knowledgeService.countAllNotModified() <= 0) {
            logger.info("no knowledge need migrate..");
            return;
        }
        int total = 0;
        final int size = 30;
        List<KnowledgeBase> baseList = null;
        try {
            baseList = knowledgeService.getAllKnowledgeNotModified(0, size);
            while (CollectionUtils.isNotEmpty(baseList)) {
                for (KnowledgeBase base : baseList) {
                    if (base != null) {
                        if (base.getModifyDate() == 0) {
                            base.setModifyDate(base.getCreateDate());
                        }
                        updateKnowledgeBasePermission(base);
                        boolean result = knowledgeService.updateKnowledgeBase(base);
                        if (result) {
                            logger.info("update knolwedge base success. knowledgeId: " + base.getId());
                        } else {
                            logger.info("update knolwedge base failed. knowledgeId: " + base.getId());
                        }
                        Thread.sleep(10);
                    }
                }
                total += baseList.size();
                baseList = knowledgeService.getAllKnowledgeNotModified(0, size);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("knowledge base migrated size: " + total);
    }

    public void afterPropertiesSet() throws Exception {
        logger.info("DataMigrateTask begining...");
        new Thread(this).start();
        logger.info("DataMigrateTask complete...");
    }
}
