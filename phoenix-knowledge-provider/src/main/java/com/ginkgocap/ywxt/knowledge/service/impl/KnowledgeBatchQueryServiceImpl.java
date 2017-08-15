package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeBatchQueryDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.utils.HttpClientHelper;
import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;

import com.ginkgocap.ywxt.knowledge.service.KnowledgeService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeBatchQueryService;
import com.ginkgocap.ywxt.util.PageUtil;

/**
 * Created by gintong on 2016/7/21.
 */
@Service("knowledgeBatchQueryService")
public class KnowledgeBatchQueryServiceImpl implements KnowledgeBatchQueryService {
    @Autowired
    private KnowledgeBatchQueryDao knowledgeBatchQueryDao;

    @Override
    public long getKnowledgeCountByUserIdAndColumnId(String[] columnID, long userId, short type) {
        return knowledgeBatchQueryDao.getKnowledgeByUserIdAndColumnId(columnID, userId, type);
    }

    @Override
    public List<Knowledge> getKnowledgeDetailList(String[] columnId, long userIdd, short type, int start, int size) {
        return knowledgeBatchQueryDao.getKnowledge(columnId, userIdd, type, start, size);
    }

    @Override
    public List<Knowledge> selectPlatform(short type, int columnId, String columnPath,long userId, int start, int size)
    {
        return knowledgeBatchQueryDao.selectPlatform(type, columnId, columnPath, userId, start, size);
    }

    @Override
    public List<KnowledgeBase> getAllPublicByPage(short type, int columnId, String columnPath, long userId, int start, int size)
    {
        return knowledgeBatchQueryDao.getAllPublicByPage(type, columnId, columnPath, start, size);
    }

    public List<KnowledgeBase> getAllByType(final long userId, final short type, final short status, final String title, final int page, int size)
    {
        return knowledgeBatchQueryDao.getAllByType(userId, type, status, title, page, size);
    }
}
