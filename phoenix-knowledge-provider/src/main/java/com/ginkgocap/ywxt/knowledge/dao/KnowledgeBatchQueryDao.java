package com.ginkgocap.ywxt.knowledge.dao;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;

import java.util.List;
import java.util.Map;

/**
 * Created by gintong on 2016/7/30.
 */
public interface KnowledgeBatchQueryDao {

    long getKnowledgeByUserIdAndColumnID(String[] columnID,long userId,short type) ;

    /**
     * 获取 金桐和自己  混合
     */
    List<Knowledge> getMixKnowledge(String columnID,long userId,short type,int offset,int limit);

    long getMixKnowledgeCount(String columnID,long userId,short type);




    /**
     * 从MySQL中查询出的knowledge_id和type  填充相应的knowledge 形成List
     */
    List<Knowledge>  fileKnowledge(Map<Long,Integer> map);


    List<Knowledge> fetchFriendKw(long[] kid,short type,int offset,int limit);

    long fetchFriendKwCount(long[] kid, short type) ;

    /**
     * @date 2016年1月13日 上午10:54:58
     * @param userId
     * @param start
     * @param size
     * @throws Exception
     */
    public List<Knowledge> getKnowledgeByUserIdAndColumnIds(int[] columnIds,long userId, short type,int start,int size);

    public List<Knowledge> selectPlatform(short type, int columnId, String columnPath,long userId, int start, int size);

    public List<Knowledge> getAllByParam(short type, int columnId, String columnPath, long userId, int page, int size);

    public long getKnowledgeCountByUserIdAndColumnID(String[] columnID,long userId, short type);

    public List<Knowledge> getKnowledge(String[] columnID,long user_id, short type,int start,int size);

    public List<Knowledge> selectIndexByParam(short type,int page, int size, List<Long> ids);
}
