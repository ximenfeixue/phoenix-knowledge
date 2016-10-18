package com.ginkgocap.ywxt.knowledge.service;

import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import net.sf.json.JSONObject;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;

/**
 * Created by gintong on 2016/7/21.
 */
public interface KnowledgeBatchQueryService
{
    public List<Knowledge> getKnowledgeDetailList(String[] columnID,long user_id,short type,int offset,int limit);

    long getKnowledgeCountByUserIdAndColumnID(String[] columnID,long user_id,short type);

    List<Knowledge> fetchFriendKw(long[] kid,short type,int offset,int limit);

    long fetchFriendKwCount(long[] kid,short type) ;

    public List<Knowledge> selectPlatform(short type, int columnId, String columnPath,long userId, int start, int size);

    public List<Knowledge> getAllByParam(short type, int columnId, String columnPath, long userId, int start, int size);

    public List<KnowledgeBase> selectPlatformBase(short type, int columnId, String columnPath, long userId, int start, int size);

    public List<KnowledgeBase> getAllByParamBase(short type, int columnId, String columnPath, long userId, int start, int size);
}
