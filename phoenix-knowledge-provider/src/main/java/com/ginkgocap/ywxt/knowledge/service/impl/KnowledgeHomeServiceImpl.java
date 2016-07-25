package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMongoDao;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMysqlDao;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.cache.Cache;
import com.ginkgocap.ywxt.file.model.FileIndex;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeStatics;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeHomeService;


/**
 * Created by gintong on 2016/7/13.
 */
@Service("knowledgeHomeService")
public class KnowledgeHomeServiceImpl implements KnowledgeHomeService
{
    private final static Logger logger = LoggerFactory.getLogger(KnowledgeHomeServiceImpl.class);

    @Autowired
    KnowledgeMongoDao knowledgeMongoDao;

    @Autowired
    KnowledgeMysqlDao knowledgeMysqlDao;

    @Override
    public List<Knowledge> getAllByParam(short type,String columnPath,int columnId, long userId, int start, int size)
    {
        return knowledgeMongoDao.getAllByParam(type, columnPath, columnId, userId, start, size);
    }

    @Override
    public long getKnowledgeCountByUserIdAndColumnID(String[] columnID, long userId, short type)
    {
        return knowledgeMongoDao.getKnowledgeCountByUserIdAndColumnID(columnID, userId, type);
    }

    @Override
    public List<Knowledge> getKnowledge(String[] columnID, long user_id, short type, int start, int size) {
        return knowledgeMongoDao.getKnowledge(columnID, user_id, type, start, size);
    }

    @Override
    public Map<String, Object> selectAllKnowledgeCategoryByParam(String tid, String lid, int state, String sortId, long userId, String keyword, int page, int size) {
        return null;
    }

    @Override
    public List<KnowledgeStatics> getRankList(int columnId) {
        return null;
    }

    @Override
    public List<KnowledgeStatics> getRankHotList(int columnId) {
        return null;
    }

    @Override
    public KnowledgeStatics getPl(long id) {
        return null;
    }

    @Override
    public List<Knowledge> selectIndexByParam(short type, int page, int size)
    {
        List<Long> ids = null;
        try {
            ids = knowledgeMysqlDao.getKnowledgeIdsByType(type, size);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return knowledgeMongoDao.selectIndexByParam(type, page, size, ids);
    }

    @Override
    public int beRelation(long id, int t, long kid, Long userId) {
        return 0;
    }

    @Override
    public Map<String, Object> selectKnowledgeCategoryForImport(long userId, List<Long> groupId, int page, int size) {
        return null;
    }

    @Override
    public int countKnowledgeIds(String tid, String lId, int state, String sortId, long userId, String keyword) {
        return 0;
    }

    @Override
    public List<KnowledgeBase> selectPlatform(int columnId, long userId, int page, int size) {
        return null;
    }

    //@Override
    public List<Knowledge> selectPlatform(short type, int columnId, String columnPath,long userId, int page, int size) {
        long start = System.currentTimeMillis();
        // 栏目id
        /*
        Criteria criteria = Criteria.where("status").is(4);
        // 权限条件过滤
        Criteria criteriaUp = null;
        // 路径过滤
        Criteria criteriaPath = new Criteria();
        // 定义查询条件
        // 查询权限表，获取可见文章ID列表
        List<Long> ids = userPermissionValueMapper.selectPlatform(userid, type);
        List<Long> cls = userPermissionValueMapper.selectVisbleColumnid(userid, type);
        if (ids != null && ids.size() > 0) {
            criteriaUp = new Criteria();
            criteriaUp.and("_id").in(ids);
        }
        // 查询栏目目录为当前分类下的所有数据
        String reful = columnPath;
        // 该栏目路径下的所有文章条件
        criteriaPath.and("cpathid").regex("^" + reful + ".*$");
        // 汇总条件
        Criteria criteriaAll = new Criteria();
        if (criteriaUp == null) {
            return null;
        } else {
            criteriaAll.andOperator(criteriaUp, criteriaPath, criteria);
        }
        if (cls != null && cls.size() > 0) {// 判断定制
            List<String> clstr = fillList(cls);
            criteriaAll.and("columnid").nin(clstr);
        }
        // 查询知识
        Query query = new Query(criteriaAll);
        if (type == 10) {
            query.sort().on("createtime", Order.DESCENDING);
        } else {
            query.sort().on("_id", Order.DESCENDING);
        }
        query.limit(size);
        query.skip((page - 1) * size);
        String collectionName = KnowledgeUtil.getKnowledgeCollectionName(type);
        List<Knowledge> list = (List<Knowledge>) mongoTemplate.find(query, Knowledge.class, collectionName);
        logger.info("总消耗时间为  end= " + (System.currentTimeMillis() - start));
        return list;*/
        return null;
    }

    @Override
    public Map<String, Object> getAggregationRead(long userId, String[] columnIds, int page, int size) {
        return null;
    }

    @Override
    public Map<String, Object> selectRecommendedKnowledge(long userId, int page, int size) {
        return null;
    }

    @Override
    public Map<String, Object> addUserStar(List<Long> knowledgeIds, long userId, int star) {
        return null;
    }
}
