package com.ginkgocap.ywxt.knowledge.dao;

import com.ginkgocap.ywxt.knowledge.model.mobile.DataSync;

import java.util.List;

/**
 * Created by gintong on 2016/7/9.
 */
public interface DataSyncMongoDao {
    public long saveDataSync(DataSync data);
    public boolean deleteDataSync(final long id);
    public List<DataSync> getDataSyncList();
}
