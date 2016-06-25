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

    protected List<Long> convertObjectListToLongList(List<Object> idList)
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
                    newIdList.add(newId);
                }
                else if (id instanceof Long) {
                    newId = Long.valueOf((Long)id);
                    newIdList.add(newId);
                }
                else {
                    logger.error("this is an invalidated Id: {}, so skip to add new id list",id);
                }
            } catch(NumberFormatException ex) {
                logger.error("Convert String to number failed: {}", idList.get(index));
            }
        }
        return newIdList;
    }

    protected String convertLongValueListToString(List<Long> ids)
    {
        if (ids == null || ids.size() <= 0) {
            return null;
        }

        StringBuffer strBuffer = new StringBuffer();
        for (Long id : ids) {
            String tagId = String.valueOf(id);
            if (strBuffer.length() + tagId.length() < 255) {
                strBuffer.append(tagId);
            } else {
                break;
            }
            strBuffer.append(",");
        }
        if (strBuffer.length() > 1) {
            strBuffer.setLength(strBuffer.length() - 1);
        }
        return strBuffer.toString();
    }
}
