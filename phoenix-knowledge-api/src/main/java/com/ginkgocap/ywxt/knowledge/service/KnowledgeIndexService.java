package com.ginkgocap.ywxt.knowledge.service;

import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import net.sf.json.JSONObject;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;

/**
 * Created by gintong on 2016/7/21.
 */
public interface KnowledgeIndexService
{
    public List<Knowledge> getKnowledgeDetailList(String[] columnID,long user_id,short type,int offset,int limit);

    public long getKnowledgeCountByUserIdAndColumnId(String[] columnID,long user_id,short type);

    public List<Knowledge> selectPlatform(short type, int columnId, String columnPath,long userId, int start, int size);

    public List<KnowledgeBase> getAllPublicByPage(short type, int columnId, String columnPath, long userId, int start, int size);

    public List<KnowledgeBase> getAllByType(final long userId, final short columnType, final short status, final String title, final int page, int size);
}
