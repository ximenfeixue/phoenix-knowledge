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

    private static final Logger logger = LoggerFactory.getLogger(KnowledgeBatchQueryServiceImpl.class);

    @Autowired
    private KnowledgeBatchQueryDao knowledgeBatchQueryDao;

    private final String dataUrl = "http://192.168.130.119:8090";

    @Override
    public long getKnowledgeCountByUserIdAndColumnID(String[] columnID, long userId, short type) {
        return knowledgeBatchQueryDao.getKnowledgeByUserIdAndColumnID(columnID, userId, type);
    }

    @Override
    public List<Knowledge> getKnowledgeDetailList(String[] columnID, long userIdd, short type, int start, int size) {
        return knowledgeBatchQueryDao.getKnowledge(columnID, userIdd, type, start, size);
    }

    public List<Knowledge> selectPlatform(short type, int columnId, String columnPath,long userId, int start, int size)
    {
        return knowledgeBatchQueryDao.selectPlatform(type, columnId, columnPath, userId, start, size);
    }

    @Override
    public List<Knowledge> getAllByParam(short type, int columnId, String columnPath, long userId, int start, int size)
    {
        return knowledgeBatchQueryDao.getAllByParam(type, columnId, columnPath, userId, start, size);
    }

    public List<KnowledgeBase> selectPlatformBase(short type, int columnId, String columnPath, long userId, int start, int size)
    {
        return knowledgeBatchQueryDao.getAllByParamBase(type, columnId, columnPath, userId, start, size);
    }

    @Override
    public List<KnowledgeBase> getAllByParamBase(short type, int columnId, String columnPath, long userId, int start, int size)
    {
        return knowledgeBatchQueryDao.getAllByParamBase(type, columnId, columnPath, userId, start, size);
    }

    @Override
    public List<Knowledge> fetchFriendKw(long[] kid, short type, int offset, int limit) {
        return knowledgeBatchQueryDao.fetchFriendKw(kid, type, offset, limit);
    }

    @Override
    public long fetchFriendKwCount(long[] kid, short type) {
        return knowledgeBatchQueryDao.fetchFriendKwCount(kid, type);
    }
}
