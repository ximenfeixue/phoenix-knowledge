package com.ginkgocap.ywxt.knowledge.dao.impl;

/**
 * Created by gintong on 2016/7/30.
 */
import static org.springframework.data.mongodb.core.query.Criteria.where;

import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.Resource;

import com.ginkgocap.ywxt.cache.Cache;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeBatchQueryDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.utils.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.model.common.Constant;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollect;
import com.ginkgocap.ywxt.knowledge.utils.HtmlToText;
import com.ginkgocap.ywxt.knowledge.utils.KnowledgeConstant;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;

@Component("knowledgeBatchQueryDao")
public class KnowledgeBatchQueryDaoImpl implements KnowledgeBatchQueryDao {

    private final Logger logger = LoggerFactory.getLogger(KnowledgeBatchQueryDaoImpl.class);

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private Cache cache;

    private final int maxSize = 20;
    private final int maxQuerySize = 200;
    private final int cacheTTL = 60 * 60 * 2;

    private static final Map<String, Boolean> loadingMap = new ConcurrentHashMap<String, Boolean>();
    //private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    @Override
    public List<Knowledge> getKnowledge(String[] columnID,long user_id, short type,int offset,int limit) {
        String collectionName = getCollectionName(type);
        List<String> list = new ArrayList<String>(columnID.length);
        for(int i = 0; i < columnID.length; i++) {
            list.add(columnID[i]);
        }
        Query query = query(where("uid").is(user_id).and("columnid").in(list).and("status").is(4)).skip(offset).limit(limit);
        //qu.sort().on("createtime", Order.DESCENDING);
        query.with(new Sort(Sort.Direction.DESC, Constant._ID));
        return mongoTemplate.find(query, Knowledge.class, collectionName);
    }

    @Override
    public long getKnowledgeByUserIdAndColumnID(String[] columnID,long userId, short type) {
        String collectionName = KnowledgeUtil.getKnowledgeCollectionName(type);
        List<String> list = new ArrayList<String>(columnID.length);
        for(int i = 0; i < columnID.length; i++) {
            list.add(columnID[i]);
        }
        return mongoTemplate.count(query(where("uid").is(userId).and("columnid").in(list).and("status").is(4)), collectionName);
    }

    @Override
    public List<Knowledge> selectPlatform(short type, int columnId, String columnPath,long userId, int start, int size)
    {
        return getAllByParam(type, columnId, columnPath, -1, start, size);
    }

    @Override
    public List<Knowledge> getAllByParam(short columnType, int columnId, String columnPath, long userId, int start, int size)
    {
        logger.info("columnType: " + columnType + " columnId: " + columnId + " userId: " + userId + " columnPath： " + columnPath);
        final String collectionName = KnowledgeUtil.getKnowledgeCollectionName(columnType);
        return getMongoIds(columnType, columnId, columnPath, userId, collectionName, start, size);
    }

    private List<Knowledge> getMongoIds(final short columnType, final int columnId, final String columnPath, final long userId, final String tableName, final int start, int size) {
        if (start < 0 || size < 0) {
            logger.error("param is invalidated. start: " + start + ", size: {}" + size);
            return null;
        }
        final String key = getKey(columnType, columnId, userId, tableName);
        List<Long> knowledgeIds = (List<Long>) getFromCache(key);
        size = size > maxSize ? maxSize : size;
        List<Knowledge> result = null;
        boolean bLoading = loadingMap.get(key) == null ? false : loadingMap.get(key);
        if (!bLoading) {
            try {
                loadingMap.put(key, Boolean.TRUE);
                logger.info("First query begin... key: " + key);
                // 查询栏目类型
                Criteria criteria = Criteria.where("status").is(4);
                // 金桐脑知识条件
                if (userId > 0) {
                    criteria.and("cid").is(userId);
                }
                // 查询栏目目录为当前分类下的所有数据
                final String reful = columnPath;
                // 该栏目路径下的所有文章条件
                criteria.and("cpathid").regex("^" + reful + ".*$");
                criteria.and("privated").ne(1);
                Query query = new Query(criteria);
                query.with(new Sort(Sort.Direction.DESC, Constant._ID));
                query.limit(maxQuerySize);
                query.skip(0);

                final List<Knowledge> knowledgeList = mongoTemplate.find(query, Knowledge.class, tableName);
                if (knowledgeList != null && knowledgeList.size() > 0 ) {
                    List<Long> ids = new ArrayList<Long>(maxQuerySize);
                    result = new ArrayList<Knowledge>(size);
                    int skip = 0;
                    int toIndex = start + size;
                    for (Knowledge knowledge : knowledgeList) {
                        if (knowledge != null) {
                            ids.add(knowledge.getId());
                            if (skip >= start && skip < toIndex) {
                                result.add(filterKnowledge(knowledge));
                            }
                        }
                        skip++;
                    }
                    saveToCache(key, ids);
                    final String knowledgeKey = getKey(columnType, columnId, userId, tableName, start, size);
                    saveKnowledgeToCache(result, knowledgeKey);
                }
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        } else if (knowledgeIds !=null && knowledgeIds.size() > 0) {
            final String knowledgeKey = getKey(columnType, columnId, userId, tableName, start, size);
            result = (List<Knowledge>)getFromCache(knowledgeKey);
            if (result != null) {
                logger.info("This list have cached before, so return it directly..");
                return result;
            }

            int fromIndex = start;
            int toIndex = start + size;
            if (toIndex > knowledgeIds.size() - 1) {
                toIndex = knowledgeIds.size() - 1;
            }
            List<Long> ids = null;
            if (knowledgeIds != null && fromIndex < toIndex) {
                logger.info("fromIndex: " + fromIndex + " toIndex: " + toIndex + " size: " + knowledgeIds.size());
                ids = knowledgeIds.subList(fromIndex, toIndex);
            }
            if (CollectionUtils.isNotEmpty(ids)) {
                long begin = System.currentTimeMillis();
                Criteria criteria = new Criteria();
                criteria.and("_id").in(ids);
                Query query = new Query(criteria);
                query.with(new Sort(Sort.Direction.DESC, Constant._ID));
                result = mongoTemplate.find(query, Knowledge.class, tableName);
                if (result != null && result.size() > 0) {
                    logger.info("get Knowledge size: " + result.size());
                    for(Knowledge detail : result) {
                        filterKnowledge(detail);
                    }
                    saveKnowledgeToCache(result, knowledgeKey);
                }
                else {
                    logger.info("can't get Knowledge by Ids: " + ids.toString());
                }
                long end = System.currentTimeMillis();
                System.out.println("Time: " + (end-begin));
            }
            // 执行更新
            //executorService.execute(new TakeRecordTask(columnId, (short) 4, userId, tableName, columnPath,));
        }
        else {
            //if no data got need query again.
            loadingMap.remove(key);
        }
        return result;
    }

    @Override
    public List<KnowledgeBase> selectPlatformBase(short type, int columnId, String columnPath,long userId, int start, int size)
    {
        return getAllByParamBase(type, columnId, columnPath, -1, start, size);
    }

    @Override
    public List<KnowledgeBase> getAllByParamBase(final short columnType, final int columnId, final String columnPath, final long userId, final int start, int size)
    {
        logger.info("columnType: " + columnType + " columnId: " + columnId + " userId: " + userId + " columnPath： " + columnPath);
        final String collectionName = KnowledgeUtil.getKnowledgeCollectionName(columnType);
        return getKnowledgeBase(columnType, columnId, columnPath, userId, collectionName, start, size);
    }

    @Override
    public List<KnowledgeBase> getAllByPage(final long userId, final short columnType, final short status, final String title, final int page, int size)
    {
        int start = page * size;
        size = size > maxSize ? maxSize : size;
        Query query = new Query();
        Criteria criteria = new Criteria();

        if (userId > 0) {
            criteria.and("cid").is(userId);
        }

        if (status >= 0) {
            criteria.and("status").is(status);
        }
        if (StringUtils.isNotBlank(title) && !"null".equals(title)) {
            criteria.and("title").regex("^" + title + ".*$");
        }
        query.addCriteria(criteria);
        query.with(new Sort(Sort.Direction.DESC, Constant._ID));
        query.skip(start);
        query.limit(size);


        final String collectionName = KnowledgeUtil.getKnowledgeCollectionName(columnType);
        final List<Knowledge> detailList = mongoTemplate.find(query, Knowledge.class, collectionName);
        return DataCollect.convertDetailToBaseList(detailList, columnType, true);
    }


    private List<KnowledgeBase> getKnowledgeBase(final short columnType, final int columnId, final String columnPath, final long userId, final String tableName, final int start, int size) {
        if (start < 0 || size < 0) {
            logger.error("param is invalidated. start: " + start + ", size: " + size);
            return null;
        }
        final String key = getBaseKey(columnType, columnId, userId, tableName);
        List<Long> knowledgeIds = (List<Long>) getFromCache(key);
        size = size > maxSize ? maxSize : size;
        List<KnowledgeBase> result = null;
        boolean bLoading = loadingMap.get(key) == null ? false : loadingMap.get(key);
        if (!bLoading) {
            try {
                loadingMap.put(key, Boolean.TRUE);
                logger.info("First query begin... key: {}", key);
                // 查询栏目类型
                Criteria criteria = Criteria.where("status").is(4);
                // 金桐脑知识条件
                if (userId > 0) {
                    criteria.and("cid").is(userId);
                }
                // 查询栏目目录为当前分类下的所有数据
                final String reful = columnPath;
                // 该栏目路径下的所有文章条件
                criteria.and("cpathid").regex("^" + reful + ".*$");
                criteria.and("privated").ne(1);
                Query query = new Query(criteria);
                query.with(new Sort(Sort.Direction.DESC, Constant._ID));
                query.limit(maxQuerySize);
                query.skip(0);

                final List<Knowledge> knowledgeList = mongoTemplate.find(query, Knowledge.class, tableName);
                if (knowledgeList != null && knowledgeList.size() > 0 ) {
                    List<Long> ids = new ArrayList<Long>(maxQuerySize);
                    List<Knowledge> detailList = new ArrayList<Knowledge>(size);
                    int skip = 0;
                    int toIndex = start + size;
                    for (Knowledge knowledge : knowledgeList) {
                        if (knowledge != null) {
                            ids.add(knowledge.getId());
                            if (skip >= start && skip < toIndex) {
                                detailList.add(filterKnowledge(knowledge));
                            }
                        }
                        skip++;
                    }
                    saveToCache(key, ids);
                    final String knowledgeKey = getBaseKey(columnType, columnId, userId, tableName, start, size);
                    result = saveKnowledgeBaseToCache(detailList, columnType, knowledgeKey);
                }
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        } else if (knowledgeIds !=null && knowledgeIds.size() > 0) {
            final String knowledgeKey = getBaseKey(columnType, columnId, userId, tableName, start, size);
            result = (List<KnowledgeBase>)getFromCache(knowledgeKey);
            if (result != null) {
                logger.info("This list have cached before, so return it directly..");
                return result;
            }

            int fromIndex = start;
            int toIndex = start + size;
            if (toIndex > knowledgeIds.size() - 1) {
                toIndex = knowledgeIds.size() - 1;
            }
            List<Long> ids = null;
            if (knowledgeIds != null && fromIndex < toIndex) {
                logger.info("fromIndex: " + fromIndex + " toIndex: " + toIndex + " size: " + knowledgeIds.size());
                ids = knowledgeIds.subList(fromIndex, toIndex);
            }
            if (CollectionUtils.isNotEmpty(ids)) {
                long begin = System.currentTimeMillis();
                Criteria criteria = new Criteria();
                criteria.and("_id").in(ids);
                Query query = new Query(criteria);
                query.with(new Sort(Sort.Direction.DESC, Constant._ID));
                final List<Knowledge> detailList = mongoTemplate.find(query, Knowledge.class, tableName);
                if (CollectionUtils.isNotEmpty(detailList)) {
                    for(Knowledge detail : detailList) {
                        filterKnowledge(detail);
                    }
                    result = saveKnowledgeBaseToCache(detailList, columnType, knowledgeKey);
                    logger.info("get Knowledge size: " + (result != null ? result.size() : 0));
                }
                else {
                    logger.info("can't get Knowledge by Ids: +", ids.toString());
                }
                long end = System.currentTimeMillis();
                System.out.println("Time: " + (end-begin));
            }
        }
        else {
            //if no data got need query again.
            loadingMap.remove(key);
        }
        return result;
    }

    // 首页主页
    @SuppressWarnings("unchecked")
    @Override
    public List<Knowledge> selectIndexByParam(short type,int page, int size,List<Long> ids)
    {
        logger.info("com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService", type);
        String collectionName = getCollectionName(type);
        Criteria criteria = Criteria.where("status").is(4);
        Criteria criteriaPj = new Criteria();
        Criteria criteriaUp = new Criteria();
        Criteria criteriaGt = new Criteria();

        criteriaGt.and("uid").is(KnowledgeConstant.SOURCE_GINTONG_BRAIN_ID);
        // 查询栏目大类下的数据：全平台
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
        String str = "" + KnowledgeUtil.writeObjectToJson(criteria);
        logger.info("MongoObject:" + collectionName + ",Query:" + str);
        query.with(new Sort(Sort.Direction.DESC, Constant._ID));
        long count;
        try {
            // count = mongoTemplate.count(query, names[length - 1]);
            // PageUtil p = new PageUtil((int) count, page, size);
            query.limit(size);
            query.skip(0);
            return mongoTemplate.find(query, Knowledge.class, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void saveKnowledgeToCache(final List<Knowledge> result, final String key)
    {
        if (CollectionUtils.isEmpty(result)) {
            logger.error("The knowledge is null, so skip..");
            return;
        }
        if (StringUtils.isBlank(key)) {
            logger.error("The knowledge key is null, so skip..");
            return;
        }
        logger.error("Save the query result to catch, key: " + key);
        saveToCache(key, result);
    }

    private List<KnowledgeBase> saveKnowledgeBaseToCache(final List<Knowledge> detailList, short columnType, final String key)
    {
        if (CollectionUtils.isEmpty(detailList)) {
            logger.error("The knowledge is null, so skip..");
        }
        if (StringUtils.isEmpty(key)) {
            logger.error("The knowledge key is null, so skip..");
        }
        logger.error("Save the query result to catch, key: {}", key);
        List<KnowledgeBase> baseList = DataCollect.convertDetailToBaseList(detailList, columnType, true);
        saveToCache(key, baseList);
        return baseList;
    }

    private Knowledge filterKnowledge(Knowledge detail)
    {
        if (detail == null) {
            logger.error("Knowledge detail is null, please check it again...");
            return null;
        }
        String knowledgeContent = HtmlToText.htmlToText(detail.getContent());
        int contentLen = knowledgeContent.length();
        int maxLen = contentLen > 250 ? 250 : contentLen;
        knowledgeContent = knowledgeContent.substring(0, maxLen);
        detail.setContent(knowledgeContent);
        return detail;
    }


    private List<String> fillList(List<Long> cls) {
        List<String> clstr = new ArrayList<String>();
        for (Long id : cls) {
            clstr.add(String.valueOf(id));
        }
        return clstr;
    }

    private String getCollectionName(short type)
    {
        return KnowledgeUtil.getKnowledgeCollectionName(type);
    }

    private static String getKey(Object... params) {
        if (ArrayUtils.isEmpty(params)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            sb.append(i > 0 ? "-" : "").append(params[i]);
        }
        return sb.toString();
    }

    private static String getBaseKey(Object... params) {
        if (ArrayUtils.isEmpty(params)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            sb.append(i > 0 ? "-" : "").append(params[i]);
        }
        return sb.toString();
    }

    private void saveToCache(String key, Object value) {
        cache.setByRedis(key, value, cacheTTL);
    }

    private void saveToCache(String key, Integer expiredTime, Object value) {
        cache.setByRedis(key, value, expiredTime);
    }

    private Object getFromCache(String key) {
        return cache.getByRedis(key);
    }

    private class TakeRecordTask implements Runnable {
        private int columnId;
        private short status;
        private long uid;
        private String name;
        private String key;
        private String columnPath;
        private int start;

        public TakeRecordTask(int columnId, short status, long uid, String name,String columnPath,int start) {
            this.columnId = columnId;
            this.status = status;
            this.uid = uid;
            this.name = name;
            this.columnPath = columnPath;
            this.key = getKey(columnId, status, uid, name, start);
            this.start = start;
        }

        @Override
        public void run() {
            boolean bLoading = loadingMap.get(key) == null ? false : loadingMap.get(key);
            if (!bLoading) {
                try {
                    loadingMap.put(key, Boolean.TRUE);
                    // 查询栏目类型
                    Criteria criteria = Criteria.where("status").is(4);
                    // 金桐脑知识条件
                    criteria.and("uid").is(uid);
                    // 查询栏目目录为当前分类下的所有数据
                    String reful = columnPath;
                    // 该栏目路径下的所有文章条件
                    criteria.and("cpathid").regex("^" + reful + ".*$");
                    Query query = new Query(criteria);
                    query.with(new Sort(Sort.Direction.DESC, Constant._ID));
                    query.limit(maxQuerySize);
                    query.skip(0);

                    List<Knowledge> knowledgeList = mongoTemplate.find(query, Knowledge.class, name);
                    List<Long> ids = new CopyOnWriteArrayList<Long>();
                    if (knowledgeList != null && knowledgeList.size() > 0 ) {
                        for (Knowledge knowledge : knowledgeList) {
                            if (knowledge != null) {
                                ids.add(knowledge.getId());
                            }
                        }
                    }
                    saveToCache(key, ids);
                } finally {
                    loadingMap.remove(key);
                }
            }
        }
    }
}
