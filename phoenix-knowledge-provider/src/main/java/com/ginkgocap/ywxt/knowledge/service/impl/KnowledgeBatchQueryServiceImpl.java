package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeBatchQueryDao;
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
    public Map<String, Object> selectKnowledgeByTagsAndkeywords(Long userid, String keywords, String scope, String tag, int page, int size) {
        logger.info("userid:{},", userid);
        logger.info("keywords:{},", keywords);
        logger.info("scope:{},", scope);
        logger.info("tag:{},", tag);
        logger.info("page:{},", page);
        logger.info("size:{},", size);
        int start = (page - 1) * size;
        // 判断是否传的是默认值
        start = start < 0 ? 0 : start;
        int count = 0; //mobileKnowledgeMapper.selectCountKnowledgeByTagsAndKeyWords(userid, tag, keywords);
        List<?> kcl = null; //mobileKnowledgeMapper.selectKnowledgeByTagsAndKeyWords(userid, tag, keywords, start, size);
        Map<String, Object> m = new HashMap<String, Object>();

        if (size != 0) {
            PageUtil p = new PageUtil(count, page, size);
            m.put("page", p);
        }

        m.put("list", kcl);
        return m;
    }

    // 已作废
    @Override
    public Map<String, Object> selectKnowledgeByMyCollectionAndkeywords(Long userid, String keywords, String scope, int page, int size) {
        logger.info(":{},", userid);
        logger.info(":{},", keywords);
        logger.info(":{},", scope);
        logger.info(":{},", page);
        logger.info(":{},", size);
        int start = (page - 1) * size;
        /** 判断是否传的是默认值 */
        start = start < 0 ? 0 : start;
        int count = 0; //mobileKnowledgeMapper.selectCountKnowledgeByMyCollectionAndKeyWords(userid, keywords);
        List<?> kcl = null; //mobileKnowledgeMapper.selectKnowledgeByMyCollectionAndKeyWords(userid, keywords, start, size);
        PageUtil p = new PageUtil(count, page, size);
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("page", p);
        m.put("list", kcl);
        return m;
    }

    @Override
    public Map<String, Object> selectMyFriendknowledgeByColumnId(long columnId, long userId, String scope, int page, int size) {
        logger.info(":{},", columnId);
        logger.info(":{},", userId);
        logger.info(":{},", scope);
        logger.info(":{},", page);
        logger.info(":{},", size);
        int start = (page - 1) * size;
        /** 判断是否传的是默认值 */
        int count = 0; //mobileKnowledgeMapper.selectCountForMyFriendKnowledgeByColumnId(columnId, userId);
        List<?> kcl = null; //mobileKnowledgeMapper.selectMyFriendKnowledgeByColumnId(columnId, userId, start, size);
        PageUtil p = new PageUtil(count, page, size);
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("page", p);
        m.put("list", kcl);
        return m;
    }

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
    public int selectKnowledgeCountByPermission(long userId, long columnId) {
        return 0;//mobileKnowledgeMapper.selectKnowledgeCountByPermission(userId, columnId);
    }

    @Override
    public List<Knowledge> getMixKnowledge(String columnID, long user_id, short type, int offset, int limit) {
        return null; //mobileKnowledgeDAO.getMixKnowledge(columnID, user_id, type, offset, limit);
    }

    @Override
    public long getMixKnowledgeCount(String columnID, long user_id, short type) {
        return knowledgeBatchQueryDao.getMixKnowledgeCount(columnID, user_id, type);
    }

    @Override
    public List<Knowledge> fileKnowledge(Map<Long, Integer> map) {
        return knowledgeBatchQueryDao.fileKnowledge(map);
    }

    @Override
    public long getKnowledgeCountByUserIdAndColumnID(String[] columnID, long userId, short type) {
        return knowledgeBatchQueryDao.getKnowledgeByUserIdAndColumnID(columnID, userId, type);
    }

    @Override
    public List<Knowledge> getKnowledge(String[] columnID, long userIdd, short type, int start, int size) {
        return knowledgeBatchQueryDao.getKnowledge(columnID, userIdd, type, start, size);
    }

    @Override
    public List<Knowledge> getAllByParam(short type, int columnId, String columnPath, long userId, int start, int size)
    {
        return knowledgeBatchQueryDao.getAllByParam(type, columnId, columnPath, userId, start, size);
    }

    @Override
    public <T> Map<String, Object> selectAllByParam(int classType, int state, String columnid, Long userid, int page, int size) {
        logger.info("com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.selectAllByParam:{},", state);
        logger.info("com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.selectAllByParam:{},", columnid);
        logger.info("com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.selectAllByParam:{},", userid);
        Map<String, Object> model = new HashMap<String, Object>();

        /*
        EnumSet<Constants.Type> enumSet = EnumSet.allOf(Constants.Type.class);
        Iterator<Constants.Type> it = enumSet.iterator();

        String class_name = null;
        while (it.hasNext()) {
            Constants.Type t = (Constants.Type) it.next();
            if (t.v() == classType) {
                class_name = t.obj();
                break;
            }
        }

        if (class_name == null) {
            return null;
        }
        Class<?> testTypeForName;
        try {
            testTypeForName = Class.forName(class_name);
        } catch (ClassNotFoundException e) {
            return null;
        }

        String[] names = class_name.split("\\.");
        int length = names.length;
        // 栏目id
        Long cid = Long.parseLong(columnid);
        // 查询栏目类型
        Column column = columnMapper.selectByPrimaryKey(cid);
        String ty = column.getColumnLevelPath().substring(0, 9);
        int leng = column.getColumnLevelPath().length();
        long type = Long.parseLong(ty);
        Criteria criteria = Criteria.where("status").is(state);
        Criteria criteriaUp = new Criteria();
        Criteria criteriaMy = new Criteria();
        Criteria criteriaGt = new Criteria();
        List<Long> ids = new ArrayList<Long>();
        String reful = column.getPathName();
        // 栏目类型过滤
        ids = userPermissionValueMapper.selectByParamsSingle(userid, type);

        if (ids != null) {
            criteriaUp.and("_id").in(ids);
        }
        Criteria child = new Criteria().and("cpathid").regex(reful + "/.*$");
        Criteria parent = new Criteria().and("cpathid").is(reful);
        criteriaGt.and("cid").is(Constants.gtnid);
        if (cid > 11) { // 一级栏目为自定义的情形
            criteria.and("cid").is(userid).and("cpathid").is(reful);
        } else { // 一级栏目为预定义的
            criteriaMy.and("cid").is(userid);
            if (leng >= 10) {
                criteriaMy.orOperator(parent, child);
                criteriaUp.orOperator(parent, child);
                criteriaGt.orOperator(parent, child);
            }
            Criteria criteriaPG = new Criteria().orOperator(criteriaMy, criteriaUp, criteriaGt);
            criteria.andOperator(criteriaPG);
        }
        // 查询知识
        String str = "" + JSONObject.fromObject(criteria);
        logger.info("MongoObject:" + class_name + ",Query:" + str);

        // 好友知识
        List<Long> list = new ArrayList<Long>();
        list.add(-1L);
        list.add(0L);
        list.add(userid);
        criteria.and("uid").nin(list);

        Query query = new Query(criteria);
        query.sort().on("createtime", Order.DESCENDING);
        long count = mongoTemplate.count(query, names[length - 1]);
        PageUtil p = new PageUtil((int) count, page, size);
        query.limit(size);
        query.skip(p.getPageStartRow() - 1);
        model.put("page", p);
        model.put("list", (List) mongoTemplate.find(query, testTypeForName, names[length - 1]));*/
        return model;
    }

    private List<String> fillList(List<Long> cls) {
        List<String> clstr = new ArrayList<String>();
        for (Long l : cls) {
            clstr.add(l + "");
        }
        return clstr;
    }

    @Override
    public int getCountForMyKnowledgeTag(long userId, String keyword) {
        return 0;// mobileKnowledgeMapper.selectMyKnowledgeCountByTag(userId, keyword);
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
