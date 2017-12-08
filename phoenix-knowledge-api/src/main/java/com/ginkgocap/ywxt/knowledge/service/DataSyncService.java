package com.ginkgocap.ywxt.knowledge.service;

import com.ginkgocap.ywxt.knowledge.model.mobile.DataSync;

import java.util.List;

/**
 * Created by gintong on 2016/7/9.
 */
public interface DataSyncService {
    long saveDataSync(DataSync data);
    boolean deleteDataSync(final long id);
    List<DataSync> getDataSyncList(long fromIndex);
    List<DataSync> getDataSyncListByTime(long time);
}
