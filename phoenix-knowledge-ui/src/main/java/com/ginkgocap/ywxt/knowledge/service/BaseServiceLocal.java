package com.ginkgocap.ywxt.knowledge.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gintong on 2016/6/15.
 */
public abstract class BaseServiceLocal {

    private Logger logger = LoggerFactory.getLogger(BaseServiceLocal.class);

    protected List<Long> convertStToLong(List<String> idList)
    {
        if (idList == null || idList.size() <= 0) {
            return null;
        }
        List<Long> newIdList =  new ArrayList<Long>(idList.size());
        for (String id : idList) {
            try {
                long newId = Long.parseLong(id);
                newIdList.add(newId);
            } catch(NumberFormatException ex) {
                logger.error("Convert String to number failed: {}", id);
            }
        }
        return newIdList;
    }
}
