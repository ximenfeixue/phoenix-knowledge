package com.ginkgocap.ywxt.knowledge.dao;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;

import java.util.List;
import java.util.Map;

/**
 * Created by gintong on 2016/7/30.
 */
public interface KnowledgeIndexDao {

    long getKnowledgeByUserIdAndColumnId(String[] columnId,long userId,short type);

    List<Knowledge> selectPlatform(short type, int columnId, String columnPath,long userId, int start, int size);

    List<Knowledge> getAllByParam(short type, int columnId, String columnPath, long userId, int page, int size);

    List<Knowledge> getKnowledge(String[] columnId,long user_id, short type,int start,int size);

    List<KnowledgeBase> getAllPublicByPage(short columnType, int columnId, String columnPath, int start, int size);

    List<KnowledgeBase> getAllByType(long userId, short type, short status, String title, int page, int size);

    void saveKnowledgeIndex(KnowledgeBase base);

    void saveKnowledgeIndex(List<KnowledgeBase> baseList);

    boolean deleteKnowledgeIndex(long knowledgeId);

    List<KnowledgeBase> getKnowledgeIndexList(short columnType, int columnId, int page, int size);
}
