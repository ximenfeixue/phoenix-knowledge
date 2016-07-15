package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.ywxt.knowledge.dao.DataSyncMongoDao;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMongoDao;
import com.ginkgocap.ywxt.knowledge.model.DataSync;
import com.ginkgocap.ywxt.knowledge.model.EActionType;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUnique;
import com.ginkgocap.ywxt.user.service.DynamicNewsService;
import com.ginkgocap.ywxt.user.service.FriendsRelationService;
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
@Service("dataSyncService")
public class DataSyncServiceImpl implements DataSyncService
{
    @Autowired
    DataSyncMongoDao dataSyncMongoDao;

    @Override
    public boolean saveDataSync(DataSync data)
    {
        return dataSyncMongoDao.saveDataSync(data);
    }

    @Override
    public boolean deleteDataSync(DataSync data)
    {
        return dataSyncMongoDao.deleteDataSync(data);
    }

    @Override
    public List<DataSync> getDataSyncList()
    {
        return dataSyncMongoDao.getDataSyncList();
    }
}
