package com.ginkgocap.ywxt.knowledge.service;

import com.ginkgocap.ywxt.knowledge.model.mobile.DataSync;

import java.util.List;

/**
 * Created by gintong on 2016/7/9.
 */
public interface DataSyncService {
    public boolean saveDataSync(DataSync data);
    public boolean deleteDataSync(DataSync data);
    public List<DataSync> getDataSyncList();
}
