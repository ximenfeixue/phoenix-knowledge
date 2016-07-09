package com.ginkgocap.ywxt.knowledge.service;

import com.ginkgocap.ywxt.knowledge.model.DataSync;

/**
 * Created by gintong on 2016/7/9.
 */
public interface DataSyncService {
    public boolean saveDataNeedSync(DataSync data);
}
