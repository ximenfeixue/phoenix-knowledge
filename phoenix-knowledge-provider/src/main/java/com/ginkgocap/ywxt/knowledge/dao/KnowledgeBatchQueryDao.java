package com.ginkgocap.ywxt.knowledge.dao;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;

import java.util.List;
import java.util.Map;

/**
 * Created by gintong on 2016/7/30.
 */
public interface KnowledgeBatchQueryDao {

    public long getKnowledgeByUserIdAndColumnID(String[] columnID,long userId,short type);

    public List<Knowledge> selectPlatform(short type, int columnId, String columnPath,long userId, int start, int size);

    public List<Knowledge> getAllByParam(short type, int columnId, String columnPath, long userId, int page, int size);

    public List<KnowledgeBase> selectPlatformBase(short type, int columnId, String columnPath,long userId, int start, int size);

    public List<KnowledgeBase> getAllByParamBase(short columnType, int columnId, String columnPath, long userId, int start, int size);

    public List<Knowledge> getKnowledge(String[] columnID,long user_id, short type,int start,int size);

    public List<Knowledge> selectIndexByParam(short type,int page, int size, List<Long> ids);
}
