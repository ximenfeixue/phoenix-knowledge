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
    public long saveDataSync(DataSync data)
    {
        return dataSyncMongoDao.saveDataSync(data);
    }

    @Override
    public boolean deleteDataSync(final long id)
    {
        return dataSyncMongoDao.deleteDataSync(id);
    }

    @Override
    public List<DataSync> getDataSyncList(long fromIndex)
    {
        return dataSyncMongoDao.getDataSyncList(fromIndex);
    }

    @Override
    public List<DataSync> getDataSyncListByTime(long time)
    {
        return dataSyncMongoDao.getDataSyncListByTime(time);
    }
}
