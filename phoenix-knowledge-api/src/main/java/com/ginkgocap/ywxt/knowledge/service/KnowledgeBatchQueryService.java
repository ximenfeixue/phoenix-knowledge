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

    public JSONObject searchKnowledge(long userid, String keyword,
                                      String tag, int scope, int pno, int psize, String qf, short type,
                                      String sort) throws Exception;

    public Map<String, Object> selectPermissionByAllPermission(long userId,long columnId,int start,int size);

    public Map<String, Object> selectPermissionByMyFriends(long userId,long columnId,int start,int size);

    public List<Knowledge> getKnowledge(String[] columnID,long user_id,short type,int offset,int limit);

    public Map<Long,Integer> selectKnowledgeByPermission(long userId,long columnId,int start,int size);

    long getKnowledgeCountByUserIdAndColumnID(String[] columnID,long user_id,short type);

    List<Knowledge> fetchFriendKw(long[] kid,short type,int offset,int limit);

    long fetchFriendKwCount(long[] kid,short type) ;

    public int selectCountKnowledge(long userid,String keywords);

    public int countPush(Long userid,String tag,String keywords);

    public List<Knowledge> selectPlatform(short type, int columnId, String columnPath,long userId, int start, int size);

    public List<Knowledge> getAllByParam(short type, int columnId, String columnPath, long userId, int start, int size);

    public List<KnowledgeBase> selectPlatformBase(short type, int columnId, String columnPath, long userId, int start, int size);

    public List<KnowledgeBase> getAllByParamBase(short type, int columnId, String columnPath, long userId, int start, int size);
}
