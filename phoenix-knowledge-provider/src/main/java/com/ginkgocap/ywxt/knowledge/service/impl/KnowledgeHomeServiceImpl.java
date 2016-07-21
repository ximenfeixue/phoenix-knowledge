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

    @Override
    public Map<String, Object> getAllByParam(short type,String columnPath,int columnId, Long userId, int page, int size)
    {
        return knowledgeMongoDao.getAllByParam(type, columnPath, columnId, userId, page, size);
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
    public <T> List<T> selectIndexByParam(int columnId, int page, int size) {
        /*
        logger.info("com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.selectIndexByParam:{},", ty);
        String[] names = ty.obj().split("\\.");
        int length = names.length;
        Criteria criteria = Criteria.where("status").is(4);
        Criteria criteriaPj = new Criteria();
        Criteria criteriaUp = new Criteria();
        Criteria criteriaGt = new Criteria();
        List<Long> ids = new ArrayList<Long>();

        criteriaGt.and("uid").is(0L); // 0 金桐脑
        // 查询栏目大类下的数据：全平台
        ids = userPermissionValueMapper.selectByParamsSingle(null, (long) ty.v());
        // 查询资讯
        Query query = null;
        if (ids != null && ids.size() > 0) {
            criteriaUp.and("_id").in(ids);
            criteriaPj.orOperator(criteriaUp, criteriaGt);
            criteriaPj.andOperator(criteria);
            query = new Query(criteriaPj);
        } else {
            criteria.andOperator(criteriaGt);
            query = new Query(criteria);
        }
        String str = "" + JSONObject.fromObject(criteria);
        logger.info("MongoObject:" + ty.obj() + ",Query:" + str);
        query.sort().on("_id", Order.DESCENDING);
        long count;
        try {
            // count = mongoTemplate.count(query, names[length - 1]);
            // PageUtil p = new PageUtil((int) count, page, size);
            query.limit(size);
            query.skip(0);
            //return (List<T>) mongoTemplate.find(query, KnowledgeVO.class, names[length - 1]);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return null;
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
