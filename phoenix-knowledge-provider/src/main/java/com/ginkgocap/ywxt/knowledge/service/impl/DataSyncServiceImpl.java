package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.ywxt.knowledge.dao.DataSyncMongoDao;
import com.ginkgocap.ywxt.knowledge.model.mobile.DataSync;
import org.springframework.beans.factory.annotation.Autowired;
import com.ginkgocap.ywxt.knowledge.service.DataSyncService;
import org.springframework.stereotype.Service;

import java.util.List;

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
