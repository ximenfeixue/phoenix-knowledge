package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.ywxt.knowledge.dao.DataSyncMongoDao;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMongoDao;
import com.ginkgocap.ywxt.knowledge.model.DataSync;
import com.ginkgocap.ywxt.knowledge.model.EActionType;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUnique;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.ginkgocap.ywxt.knowledge.service.DataSyncService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by gintong on 2016/7/9.
 */
@Service("dataSyncServiceImpl")
public class DataSyncServiceImpl implements DataSyncService, Runnable
{
    private Logger logger = LoggerFactory.getLogger(DataSyncServiceImpl.class);
    @Autowired
    DataSyncMongoDao dataSyncMongoDao;

    @Autowired
    private KnowledgeMongoDao knowledgeMongoDao;

    private BlockingQueue<DataSync> dataSyncQueue = new LinkedBlockingQueue<DataSync>();

    public DataSyncServiceImpl()
    {
        //First get 50
        List<DataSync> dataSyncList = dataSyncMongoDao.getDataSyncList();
        if (dataSyncList != null && dataSyncList.size() >0) {
            for (DataSync data : dataSyncList) {
                dataSyncQueue.offer(data);
            }
        }
    }

    public boolean saveDataNeedSync(DataSync data)
    {
        try {
            dataSyncQueue.offer(data);
        } catch (Exception ex) {
            logger.error("save dataSycn failed: dataSync: {}",data);
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        try {
            DataSync data = dataSyncQueue.peek();
            if (data.getAction() == EActionType.EDeleteKnowledgeDirectory.getValue()) {
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
            }
            else if (data.getAction() == EActionType.EDeleteKnowledgeTag.getValue()) {
                logger.info("Not support now..");
            }
        } catch (Exception ex) {
            logger.error("Data sycn error: {}", ex.getMessage());
        }
    }
}
