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
    public JSONObject searchKnowledge(long userId, String keyword, String tag, int scope, int pno, int psize, String qf, short type, String sort)
            throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        if (!qf.equals(""))
            params.put("qf", qf);
        if (!tag.equals(""))
            params.put("tag", tag);
        if (!sort.equals(""))
            params.put("sort", sort);
        params.put("pno", pno + "");
        params.put("type", type + "");
        params.put("psize", psize + "");
        params.put("scope", scope + "");
        if (!keyword.equals(""))
            params.put("keyword", keyword);
        if (userId != -2) // 考虑到全平台与金桐网的id，所以设置userid《-1
            params.put("userid", userId + "");
        String str = HttpClientHelper.post(dataUrl + "/knowledge/keyword/search.json", params);
        return JSONObject.fromObject(str);
    }

    @Override
    public Map<String, Object> selectPermissionByAllPermission(long userId, long columnId, int start, int size) {
        logger.info(":{},", columnId);
        logger.info(":{},", userId);
        logger.info(":{},", start);
        logger.info(":{},", size);
        int startPage = (start - 1) * size;
        int count = 0; //mobileKnowledgeMapper.selectKnowledgeCountByPermissionAllPlatform(userId, columnId);
        PageUtil p = new PageUtil(count, start, size);
        Map<String, Object> m = new HashMap<String, Object>();
        List<?> kcl = null; //mobileKnowledgeMapper.selectKnowledgeByPermissionAllPlatform(userId, columnId, startPage, size);
        m.put("page", p);
        m.put("list", kcl);
        return m;
    }

    @Override
    public Map<String, Object> selectPermissionByMyFriends(long userId, long columnId, int start, int size) {
        logger.info(":{},", columnId);
        logger.info(":{},", userId);
        logger.info(":{},", start);
        logger.info(":{},", size);
        int startPage = (start - 1) * size;
        int count = 0;//mobileKnowledgeMapper.selectKnowledgeCountByPermissionMyFriends(userId, columnId);
        PageUtil p = new PageUtil(count, start, size);
        Map<String, Object> m = new HashMap<String, Object>();
        List<?> kcl = null; // mobileKnowledgeMapper.selectKnowledgeByPermissionMyFriends(userId, columnId, startPage, size);
        m.put("page", p);
        m.put("list", kcl);
        return m;
    }

    @Override
    public Map<Long, Integer> selectKnowledgeByPermission(long userId, long columnId, int start, int size)
    {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> kcl = null; //mobileKnowledgeMapper.selectKnowledgeByPermission(userId, columnId, start, size);
        Map<Long, Integer> result = new HashMap<Long, Integer>();
        if (null != kcl) {
            int tempSize = kcl.size();
            if (tempSize > 0) {
                for (int i = 0; i < tempSize; i++) {
                    Map<String, Object> m = kcl.get(i);
                    result.put(Long.parseLong(m.get("knowledge_id").toString()), Integer.parseInt(m.get("type").toString()));
                }
            }
        }
        return result;
    }

    @Override
    public long getKnowledgeCountByUserIdAndColumnID(String[] columnID, long userId, short type) {
        return knowledgeBatchQueryDao.getKnowledgeByUserIdAndColumnID(columnID, userId, type);
    }

    @Override
    public List<Knowledge> getKnowledge(String[] columnID, long userIdd, short type, int start, int size) {
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

    @Override
    public int selectCountKnowledge(long userId, String keywords) {
        return 0;
    }

    @Override
    public int countPush(Long userid, String tag, String keywords) {
        return 0; //mobileKnowledgeMapper.selectCountKnowledgeByTagsAndKeyWords(userid, tag, keywords);
    }
}
