package com.ginkgocap.ywxt.knowledge.service;

import com.ginkgocap.ywxt.knowledge.model.common.DataCollection;
import com.ginkgocap.ywxt.user.model.User;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chen Peifeng on 2016/6/15.
 */
public abstract class BaseServiceLocal {

    private final Logger logger = LoggerFactory.getLogger(BaseServiceLocal.class);
    protected String tagLimitErrorMsg = "，标签数量超过限制，最多添加10个 ；";

    protected long getUserId(User user)
    {
        //This return Id or Uid
        return user.getId();
    }

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

    protected String convertLongValueListToString(List<Long> ids,String existTag)
    {
        if (ids == null || ids.size() <= 0) {
            return existTag;
        }

        StringBuffer strBuffer = existTag != null ? new StringBuffer(existTag+",") : new StringBuffer();
        for (Long id : ids) {
            String tagId = String.valueOf(id);
            if (strBuffer.toString().length() + tagId.length() < 255) {
                strBuffer.append(tagId);
            } else {
                break;
            }
            strBuffer.append(",");
        }

        String tagList = strBuffer.toString();
        if (tagList.length() > 1) {
            tagList = tagList.substring(0, tagList.length()-1);
        }
        return tagList;
    }

    protected String convertLongValueListToString(List<Long> ids)
    {
        return DataCollection.convertLongListToBase(ids);
    }

    protected InterfaceResult batchResult(int success,int failed,boolean overMaxLimit)
    {
        InterfaceResult result = null;
        if (failed == 0) {
            result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
            result.setResponseData("添加成功");
        }
        else if (success ==0) {
            result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
            String errorMessage = "添加失败";
            if (overMaxLimit) {
                errorMessage = "添加失败，标签数量超过限制，最多添加10个；";
            }
            result.setResponseData(errorMessage);
        } else {
            result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
            result.setResponseData("部分失败：" + success + "条成功，" + failed + "条失败，标签数量超过限制，最多添加10个");
        }

        return result;
    }
}
