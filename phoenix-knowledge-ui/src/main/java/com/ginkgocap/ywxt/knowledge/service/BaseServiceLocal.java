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

    protected List<Long> convertStToLong(List<Object> idList)
    {
        if (idList == null || idList.size() <= 0) {
            return null;
        }
        List<Long> newIdList =  new ArrayList<Long>(idList.size());
        for (int index = 0; index < idList.size(); index++) {
            try {
                long newId = 0L;
                Object id = idList.get(index);
                if (id instanceof String) {
                    newId = Long.valueOf((String)id);
                }
                else if (id instanceof Long) {
                    newId = Long.valueOf((Long)id);
                }
                newIdList.add(newId);
            } catch(NumberFormatException ex) {
                logger.error("Convert String to number failed: {}", idList.get(index));
            }
        }
        return newIdList;
    }
}
