package com.ginkgocap.ywxt.knowledge.service;

import com.ginkgocap.ywxt.knowledge.model.mobile.DataSync;
import com.ginkgocap.ywxt.knowledge.model.mobile.EActionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by gintong on 2016/7/9.
 */
public class DataSyncTask implements Runnable, InitializingBean
{
    private Logger logger = LoggerFactory.getLogger(DataSyncTask.class);
    private static final int MAX_QUEUE_NUM = 200;

    @Autowired
    DataSyncService dataSyncService;

    @Autowired
    DynamicNewsServiceLocal dynamicNewsServiceLocal;

    private BlockingQueue<DataSync> dataSyncQueue = new ArrayBlockingQueue<DataSync>(MAX_QUEUE_NUM);

    public DataSyncTask()
    {
        //First get 50
        List<DataSync> dataSyncList = dataSyncService.getDataSyncList();
        if (dataSyncList != null && dataSyncList.size() >0) {
            for (DataSync data : dataSyncList) {
                dataSyncQueue.offer(data);
            }
        }
    }

    public boolean saveDataNeedSync(DataSync data)
    {
        try {
            dataSyncService.saveDataSync(data);
            dataSyncQueue.offer(data);
        } catch (Exception ex) {
            logger.error("save sync data failed: dataSync: {}",data);
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        try {
            DataSync data = dataSyncQueue.peek();
            if (data.getAction() == EActionType.EAddDynamic.getValue()) {
                dynamicNewsServiceLocal.addDynamic(data.getContent(), data.getUserId());
            }
            /*
            else if (data.getAction() == EActionType.EDeleteKnowledgeDirectory.getValue()) {
                List<KnowledgeUnique> failedIdList = new ArrayList<KnowledgeUnique>();
                List<KnowledgeUnique> idList = data.getIds();
                if (idList != null && idList.size() >0) {
                    for (KnowledgeUnique ident : idList) {
                        boolean ret = knowledgeMongoDao.deleteKnowledgeDirectory(ident.getId(), ident.getColumnId(), data.getId());
                        if (!ret) {
                            logger.error("delete directory failed for: knowledgeId: {}, directory: {}", ident.getId(), data.getId());
                            failedIdList.add(ident);
                        }
                    }
                }
                if (failedIdList.size() > 0) {
                    data.setIds(failedIdList);
                    dataSyncMongoDao.saveDataSync(data);
                }
                else {
                    dataSyncMongoDao.deleteDataSync(data);
                }
            }*/
            else if (data.getAction() == EActionType.EDeleteKnowledgeTag.getValue()) {
                logger.info("Not support now..");
            }
            if (dataSyncQueue.size() == 0) {
                Thread.sleep(10);
            }
        } catch (Exception ex) {
            logger.error("Data sycn error: {}", ex.getMessage());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("DataSyncTask starting........");
        new Thread(new DataSyncTask()).start();
    }
}
