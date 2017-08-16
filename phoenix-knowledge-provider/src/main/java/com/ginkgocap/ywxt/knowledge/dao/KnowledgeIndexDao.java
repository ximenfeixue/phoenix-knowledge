package com.ginkgocap.ywxt.knowledge.dao;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;

import java.util.List;
import java.util.Map;

/**
 * Created by gintong on 2016/7/30.
 */
public interface KnowledgeIndexDao {

    long getKnowledgeByUserIdAndColumnId(String[] columnID,long userId,short type);

    List<Knowledge> selectPlatform(short type, int columnId, String columnPath,long userId, int start, int size);

    List<Knowledge> getAllByParam(short type, int columnId, String columnPath, long userId, int page, int size);

    List<Knowledge> getKnowledge(String[] columnID,long user_id, short type,int start,int size);

    List<KnowledgeBase> getAllPublicByPage(final short columnType, final int columnId, final String columnPath, final int start, int size);

    List<KnowledgeBase> getAllByType(final long userId, final short type, final short status, final String title, final int page, int size);
}
