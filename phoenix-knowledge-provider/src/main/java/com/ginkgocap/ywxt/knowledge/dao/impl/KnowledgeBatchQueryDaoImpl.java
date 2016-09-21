package com.ginkgocap.ywxt.knowledge.dao.impl;

/**
 * Created by gintong on 2016/7/30.
 */
import static org.springframework.data.mongodb.core.query.Criteria.where;

import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import com.ginkgocap.ywxt.cache.Cache;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeBatchQueryDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.model.common.Constant;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollect;
import com.ginkgocap.ywxt.knowledge.utils.HtmlToText;
import com.ginkgocap.ywxt.knowledge.utils.KnowledgeConstant;
import net.sf.json.JSONArray;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
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
    private final int maxQuerySize = 150;
    private final int cacheTTL = 60 * 60 * 24;

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
    public List<Knowledge> getMixKnowledge(String columnID, long userId, short type, int offset, int limit) {
        String collectionName = KnowledgeUtil.getKnowledgeCollectionName(type);
        return mongoTemplate.find(query(where("columnid").is(columnID).and("uid").in(Arrays.asList(new Long[]{0L,userId}))).skip(offset).limit(limit), Knowledge.class,collectionName);

    }

    @Override
    public long getMixKnowledgeCount(String columnID, long userId, short type) {
        String collectionName = getCollectionName(type);
        return mongoTemplate.count(query(where("columnid").is(columnID).and("uid").in(Arrays.asList(new Long[]{0L,userId}))), collectionName);
    }

    @Override
    public List<Knowledge> fileKnowledge(Map<Long, Integer> map) {
        if(map == null || map.size() == 0) return null;
        Set<Entry<Long,Integer>> set = map.entrySet();
        Iterator<Entry<Long,Integer>> it = set.iterator();
        List<Knowledge> result = new ArrayList<Knowledge>();
        while(it.hasNext()) {
            Entry<Long,Integer> entry = it.next();
            long kid = entry.getKey();
            int value = entry.getValue();
            String collectionName = getCollectionName((short)value);
            Knowledge knowledge = mongoTemplate.findOne(query(where("_id").is(kid)),Knowledge.class, collectionName);
            if(knowledge != null) result.add(knowledge);

        }
        return result;
    }

    @Override
    public List<Knowledge> fetchFriendKw(long[] kid, short type,int offset,int limit) {
        String collectionName = getCollectionName(type);
        String column = KnowledgeUtil.getKnowledgeTypeName(type);

        if(StringUtils.isEmpty(column)) {
            System.out.println(String.format("column=%s,type=%d", column,type));
            return null;
        }

       /*List<Long> list = new ArrayList<Long>(kid.length);
        for(int i = 0; i < kid.length; i++) {
            list.add(kid[i]);
        }
        Criteria ctri = new Criteria("cpathid");
        ctri.regex("^" + column + ".*$").and("status").is("4");*/
        if(kid == null || kid.length == 0) return null;
        List<Long> list = new ArrayList<Long>(kid.length);
        for(int i = 0; i < kid.length; i++) {
            list.add(kid[i]);
        }
        Criteria ctri = Criteria.where("_id").in(list).and("cpathid").regex("^" + column + ".*$").and("status").is(4);
        Query query_n = new Query(ctri);
        return mongoTemplate.find(query_n.limit(limit),Knowledge.class, collectionName);

    }


    //{"_id":{ "$in":[]},"$and":[{"cpathid":{ "$regex":"^资讯.*$"}},{"status":4}]}
    @Override
    public long fetchFriendKwCount(long[] kid, short type) {
        String collectionName = getCollectionName(type);

        String column = KnowledgeUtil.getKnowledgeTypeName(type);
        if(StringUtils.isEmpty(column)) {
            logger.error("column : {}, type: {}", column,type);
            return 0L;
        }

        String knowledge = (kid == null || kid.length == 0) ? "[]" : JSONArray.fromObject(kid).toString();
        String result = String.format("{\"_id\":{ \"$in\":%s},\"$and\":[{\"cpathid\":{ \"$regex\":\"^%s.*$\"}},{\"status\":4}]}", knowledge,column);
        BasicQuery query = new BasicQuery(result);
        long count = mongoTemplate.count(query, collectionName);
        return count;
    }

    @Override
    public List<Knowledge> selectPlatform(short type, int columnId, String columnPath,long userId, int start, int size)
    {
        return getAllByParam(type, columnId, columnPath, -1, start, size);
    }

    @Override
    public List<Knowledge> getAllByParam(short columnType, int columnId, String columnPath, long userId, int start, int size)
    {
        logger.info("columnType:{} columnId:{} userId:{} columnPath： {}", columnType, columnId, userId, columnPath);
        final String collectionName = KnowledgeUtil.getKnowledgeCollectionName(columnType);
        return getMongoIds(columnType, columnId, columnPath, userId, collectionName, start, size);
    }

    private List<Knowledge> getMongoIds(final short columnType, final int columnId, final String columnPath, final long userId, final String tableName, final int start, int size) {
        if (start < 0 || size < 0) {
            logger.error("param is invalidated. start: {}, size: {}", start, size);
            return null;
        }
        final String key = getKey(columnType, columnId, userId, tableName);
        List<Long> knowledgeIds = (List<Long>) cache.get(key);
        size = size > maxSize ? maxSize : size;
        List<Knowledge> result = null;
        boolean bLoading = loadingMap.get(key) == null ? false : loadingMap.get(key);
        if (!bLoading) {
            try {
                loadingMap.put(key, Boolean.TRUE);
                logger.info("First query begin... key: {}", key);
                // 查询栏目类型
                Criteria criteria = Criteria.where("status").is(4);
                // 金桐脑知识条件
                if (userId >= 0) {
                    criteria.and("uid").is(userId);
                }
                // 查询栏目目录为当前分类下的所有数据
                final String reful = columnPath;
                // 该栏目路径下的所有文章条件
                criteria.and("cpathid").regex("^" + reful + ".*$");
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
                    cache.set(key, cacheTTL, ids);
                    final String knowledgeKey = getKey(columnType, columnId, userId, tableName, start, size);
                    saveKnowledgeToCache(result, knowledgeKey);
                }
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        } else if (knowledgeIds !=null && knowledgeIds.size() > 0) {
            final String knowledgeKey = getKey(columnType, columnId, userId, tableName, start, size);
            result = (List<Knowledge>)cache.get(knowledgeKey);
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
            if (ids != null && ids.size() > 0) {
                long begin = System.currentTimeMillis();
                Criteria criteria = new Criteria();
                criteria.and("_id").in(ids);
                Query query = new Query(criteria);
                query.with(new Sort(Sort.Direction.DESC, Constant._ID));
                result = mongoTemplate.find(query, Knowledge.class, tableName);
                if (result != null && result.size() > 0) {
                    logger.info("get Knowledge size: {}", result.size());
                    for(Knowledge detail : result) {
                        filterKnowledge(detail);
                    }
                    saveKnowledgeToCache(result, knowledgeKey);
                }
                else {
                    logger.info("can't get Knowledge by Ids: {}", ids.toString());
                }
//	            for (Long id : ids) {
//	                Knowledge vo = mongoTemplate.findById(id, KnowledgeNews.class, tableName);
//	                if (vo != null) {
//	                    result.add(vo);
//	                }
//	            }
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
        logger.info("columnType:{} columnId:{} userId:{} columnPath： {}", columnType, columnId, userId, columnPath);
        final String collectionName = KnowledgeUtil.getKnowledgeCollectionName(columnType);
        return getKnowledgeBase(columnType, columnId, columnPath, userId, collectionName, start, size);
    }

    private List<KnowledgeBase> getKnowledgeBase(final short columnType, final int columnId, final String columnPath, final long userId, final String tableName, final int start, int size) {
        if (start < 0 || size < 0) {
            logger.error("param is invalidated. start: {}, size: {}", start, size);
            return null;
        }
        final String key = getBaseKey(columnType, columnId, userId, tableName);
        List<Long> knowledgeIds = (List<Long>) cache.get(key);
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
                if (userId >= 0) {
                    criteria.and("uid").is(userId);
                }
                // 查询栏目目录为当前分类下的所有数据
                final String reful = columnPath;
                // 该栏目路径下的所有文章条件
                criteria.and("cpathid").regex("^" + reful + ".*$");
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
                    cache.set(key, cacheTTL, ids);
                    final String knowledgeKey = getBaseKey(columnType, columnId, userId, tableName, start, size);
                    result = saveKnowledgeBaseToCache(detailList, knowledgeKey);
                }
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        } else if (knowledgeIds !=null && knowledgeIds.size() > 0) {
            final String knowledgeKey = getBaseKey(columnType, columnId, userId, tableName, start, size);
            result = (List<KnowledgeBase>)cache.get(knowledgeKey);
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
            if (ids != null && ids.size() > 0) {
                long begin = System.currentTimeMillis();
                Criteria criteria = new Criteria();
                criteria.and("_id").in(ids);
                Query query = new Query(criteria);
                query.with(new Sort(Sort.Direction.DESC, Constant._ID));
                final List<Knowledge> detailList = mongoTemplate.find(query, Knowledge.class, tableName);
                if (detailList != null && detailList.size() > 0) {
                    logger.info("get Knowledge size: {}", result.size());
                    for(Knowledge detail : detailList) {
                        filterKnowledge(detail);
                    }
                    result = saveKnowledgeBaseToCache(detailList, knowledgeKey);
                }
                else {
                    logger.info("can't get Knowledge by Ids: {}", ids.toString());
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

    @Override
    public List<Knowledge> getKnowledgeByUserIdAndColumnIds(int[] columnIds,long userId, short type,int start,int size)
    {
        final String collectionName = this.getCollectionName(type);
        final List<String> list = new ArrayList<String>(columnIds.length);
        for(int i = 0; i < columnIds.length; i++) {
            list.add(String.valueOf(columnIds[i]));
        }
        Query query = new Query(Criteria.where("uid").is(userId).and("columnid").in(list).and("status").is(4)).skip(start).limit(size);
        query.with(new Sort(Sort.Direction.DESC, Constant._ID));
        return mongoTemplate.find(query, Knowledge.class, collectionName);
    }

    @Override
    public long getKnowledgeCountByUserIdAndColumnID(String[] columnID,long userId, short type)
    {
        final String collectionName = KnowledgeUtil.getKnowledgeCollectionName(type);
        final List<String> list = new ArrayList<String>(columnID.length);
        for(int i = 0; i < columnID.length; i++) {
            list.add(columnID[i]);
        }
        return mongoTemplate.count(new Query(Criteria.where("uid").is(userId).and("columnid").in(list).and("status").is(4)), collectionName);
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
        if (result == null || result.size() <= 0) {
            logger.error("The knowledge is null, so skip..");
        }
        if (StringUtils.isEmpty(key)) {
            logger.error("The knowledge key is null, so skip..");
        }
        logger.error("Save the query result to catch, key: {}", key);
        cache.set(key, cacheTTL, result);
    }

    private List<KnowledgeBase> saveKnowledgeBaseToCache(final List<Knowledge> detailList, final String key)
    {
        if (CollectionUtils.isEmpty(detailList)) {
            logger.error("The knowledge is null, so skip..");
        }
        if (StringUtils.isEmpty(key)) {
            logger.error("The knowledge key is null, so skip..");
        }
        logger.error("Save the query result to catch, key: {}", key);
        List<KnowledgeBase> baseList = DataCollect.convertDetailToBaseList(detailList, true);
        cache.set(key, cacheTTL, baseList);
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
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < params.length; i++) {
            sb.append(i > 0 ? "_" : "").append(params[i]);
        }
        return sb.toString();
    }

    private static String getBaseKey(Object... params) {
        if (ArrayUtils.isEmpty(params)) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < params.length; i++) {
            sb.append(i > 0 ? "-" : "").append(params[i]);
        }
        return sb.toString();
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
                    cache.set(key, ids);
                } finally {
                    loadingMap.remove(key);
                }
            }

        }
    }
}
