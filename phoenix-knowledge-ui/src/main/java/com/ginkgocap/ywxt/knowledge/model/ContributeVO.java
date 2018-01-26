package com.ginkgocap.ywxt.knowledge.model;

import java.util.List;

/**
 * Created by wang fei on 2018/1/24.
 */
public class ContributeVO {

    private List<ContributeData> dataList;

    private Long organId;

    public List<ContributeData> getDataList() {
        return dataList;
    }

    public void setDataList(List<ContributeData> dataList) {
        this.dataList = dataList;
    }

    public Long getOrganId() {
        return organId;
    }

    public void setOrganId(Long organId) {
        this.organId = organId;
    }
}
