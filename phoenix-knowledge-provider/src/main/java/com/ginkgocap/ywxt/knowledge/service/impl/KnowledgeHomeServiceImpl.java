package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeHomeDao;
import com.ginkgocap.ywxt.knowledge.dao.news.KnowledgeNewsDAO;
import com.ginkgocap.ywxt.knowledge.entity.Column;
import com.ginkgocap.ywxt.knowledge.entity.ColumnKnowledge;
import com.ginkgocap.ywxt.knowledge.entity.ColumnKnowledgeExample;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategory;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStatics;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStaticsExample;
import com.ginkgocap.ywxt.knowledge.entity.UserPermissionExample;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnKnowledgeMapper;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnKnowledgeValueMapper;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnMapper;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnValueMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeCategoryValueMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeStaticsMapper;
import com.ginkgocap.ywxt.knowledge.mapper.UserPermissionMapper;
import com.ginkgocap.ywxt.knowledge.mapper.UserPermissionValueMapper;
import com.ginkgocap.ywxt.knowledge.service.ColumnService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeHomeService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.knowledge.util.tree.ConvertUtil;
import com.ginkgocap.ywxt.knowledge.util.tree.Node;
import com.ginkgocap.ywxt.util.PageUtil;

@Service("knowledgeHomeService")
public class KnowledgeHomeServiceImpl implements KnowledgeHomeService {

    @Autowired
    KnowledgeStaticsMapper knowledgeStaticsMapper;
    @Autowired
    ColumnValueMapper columnValueMapper;
    @Autowired
    ColumnService columnService;
    @Autowired
    KnowledgeCategoryValueMapper knowledgeCategoryValueMapper;
    @Autowired
    UserPermissionMapper userPermissionMapper;
    @Autowired
    ColumnMapper columnMapper;
    @Autowired
    ColumnKnowledgeValueMapper columnKnowledgeValueMapper;
    @Autowired
    KnowledgeHomeDao knowledgeHomeDao;
    @Autowired
    KnowledgeNewsDAO knowledgeNewsDao;

    @Resource
    private MongoTemplate mongoTemplate;

    @Autowired
    private ColumnKnowledgeMapper columnKnowledgeMapper;
    @Autowired
    private UserPermissionValueMapper userPermissionValueMapper;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public List<KnowledgeStatics> getRankList(Long columnid) {
        logger.info("com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.getRankList:{}",columnid);
        Column c = columnMapper.selectByPrimaryKey(columnid);
        if (c != null) {
            String clp = c.getColumnLevelPath().substring(0, 9);
            byte type = (byte) Integer.parseInt(clp);
            KnowledgeStaticsExample ce = new KnowledgeStaticsExample();
            ce.createCriteria().andSourceEqualTo((short) 0).andTypeEqualTo((short) type);
            ce.setLimitStart(0);
            ce.setLimitEnd(10);
            ce.setOrderByClause("commentCount desc");
            return knowledgeStaticsMapper.selectByExample(ce);
        }
        return null;
    }

    @Override
    public List<Column> getTypeList(Long userId, Long column) {
        logger.info("com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.getTypeList:{}",column);
        return columnValueMapper.selectByParam(column, Constants.gtnid, userId);
    }


    //首页栏目
    @SuppressWarnings("unchecked")
    @Override
    public <T> Map<String,Object> selectAllByParam(T t, int state, String columnid, Long userid, int page, int size) {
        logger.info("com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.selectAllByParam:{},",state);
        logger.info("com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.selectAllByParam:{},",columnid);
        logger.info("com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.selectAllByParam:{},",userid);
        Map<String, Object> model = new HashMap<String, Object>();
        String[] names = t.getClass().getName().split("\\.");
        int length = names.length;
        //栏目id
        Long cid = Long.parseLong(columnid);
        //查询栏目类型
        Column column = columnMapper.selectByPrimaryKey(cid);
        int leng = column.getColumnLevelPath().length();
        String ty = column.getColumnLevelPath().substring(0, 9);
        long type = Long.parseLong(ty);
        Criteria criteria =Criteria.where("status").is(4);
        Criteria criteriaPj = new Criteria();
        Criteria criteriaUp = new Criteria();
        Criteria criteriaMy = new Criteria();
        Criteria criteriaMyo = new Criteria();
        Criteria criteriaMyz = new Criteria();
        Criteria criteriaGt = new Criteria();
        Criteria criteriaGtz = new Criteria();
        Criteria criteriaGto = new Criteria();
        List<Long> ids = new ArrayList<Long>();
        String reful=column.getPathName();
        //栏目类型过滤
        ids = userPermissionValueMapper.selectByParamsSingle(userid, type);
       
        if (ids != null) {
            criteriaUp.and("_id").in(ids);
        }
        //子栏目查询,否则为栏目类型查询
        if (leng >= 10 ) {
            criteriaMyo.and("cid").is(userid).and("cpathid").regex(reful+"/.*$");
            criteriaMyz.and("cid").is(userid).and("cpathid").regex(reful+".*$");
            criteriaUp.and("cpathid").regex(reful+".*$").and("cid").is(userid);
        }else{
            criteriaMy.and("cid").is(userid);
            //一级栏目为自定义的情形
            if (cid > 11) {
                criteriaUp = new Criteria().and("cid").is(userid).and("cpathid").is(reful);
            }
        }
        criteriaGtz=criteriaGtz.and("cid").is(Constants.gtnid).and("cpathid").is(reful);
        criteriaGto=criteriaGto.and("cid").is(Constants.gtnid).and("cpathid").regex(reful+"/.*$");
        criteriaGt.orOperator(criteriaGtz,criteriaGto);
        criteriaMy.orOperator(criteriaMyz,criteriaMyo);
        criteriaPj.orOperator(criteriaUp,criteriaGt,criteriaMy);
        criteria.andOperator(criteriaPj);
        //查询知识
        Query query = new Query(criteria);
        query.sort().on("createtime", Order.DESCENDING);
        long count = mongoTemplate.count(query, names[length - 1]);
        PageUtil p = new PageUtil((int) count, page, size);
        query.limit(size);
        query.skip(p.getPageStartRow() - 1);
        model.put("page", p);
        model.put("list", (List<T>) mongoTemplate.find(query, t.getClass(), names[length - 1]));
        return model;
    }

    //首页主页
    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> selectIndexByParam(Constants.Type ty, int page, int size) {
        logger.info("com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.selectIndexByParam:{},",ty);
        String[] names = ty.obj().split("\\.");
        int length = names.length;
        Criteria criteria =Criteria.where("status").is(4);
        Criteria criteriaPj = new Criteria();
        Criteria criteriaUp = new Criteria();
        Criteria criteriaGt = new Criteria();
        List<Long> ids = new ArrayList<Long>();

        //查询栏目大类下的数据：全平台
        ids = userPermissionValueMapper.selectByParamsSingle(null, (long) ty.v());

        //查询资讯
        if (ids != null) {
            criteriaUp.and("_id").in(ids);
        }
        criteriaGt.and("cid").is(Constants.gtnid);
        criteriaPj.orOperator(criteriaUp,criteriaGt);
        criteria.andOperator(criteriaPj);
        Query query = new Query(criteria);
        query.sort().on("createtime", Order.DESCENDING);
        long count;
        try {
            count = mongoTemplate.count(query, names[length - 1]);
            PageUtil p = new PageUtil((int) count, page, size);
            query.limit(size);
            query.skip(p.getPageStartRow() - 1);
            return (List<T>) mongoTemplate.find(query, Class.forName(ty.obj()), names[length - 1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public KnowledgeStatics getPl(long id) {
        logger.info("com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.getPl:{}",id);
        return knowledgeStaticsMapper.selectByPrimaryKey(id);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Map<String, Object> selectAllKnowledgeCategoryByParam(String tid, String lid, int state, String sortid,
            Long userid,String keyword, int page, int size) {
        logger.info("com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.selectAllKnowledgeCategoryByParam:{},",tid);
        logger.info("com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.selectAllKnowledgeCategoryByParam:{},",lid);
        logger.info("com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.selectAllKnowledgeCategoryByParam:{},",state);
        logger.info("com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.selectAllKnowledgeCategoryByParam:{},",sortid);
        logger.info("com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.selectAllKnowledgeCategoryByParam:{},",userid);
        logger.info("com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.selectAllKnowledgeCategoryByParam:{},",keyword);
        int start = (page - 1) * size;
        int count = knowledgeCategoryValueMapper.countKnowledgeIds(userid, state, sortid, Constants.gtnid, tid, lid,keyword);
        List kcl = knowledgeCategoryValueMapper.selectKnowledgeIds(userid, state, sortid,
                Constants.gtnid, tid, lid,keyword,start,size);
        PageUtil p = new PageUtil(count, page, size);
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("page", p);
        m.put("list", kcl);
        return m;
    }
    @SuppressWarnings("rawtypes")
    @Override
    public Map<String, Object> selectKnowledgeCategoryForImport(Long userid,List<Long> groupid, int page, int size) {
        logger.info("com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.selectKnowledgeCategoryForImport:{},",userid);
        logger.info("com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.selectKnowledgeCategoryForImport:{},",groupid);
        int start = (page - 1) * size;
        int count = knowledgeCategoryValueMapper.countKnowledgeId(userid, groupid);
        List kcl = knowledgeCategoryValueMapper.selectKnowledgeId(userid, groupid,start,size);
        PageUtil p = new PageUtil(count, page, size);
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("page", p);
        m.put("list", kcl);
        return m;
    }

    @Override
    public List<Node> queryColumns(long cid, long userId) {
        logger.info("com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.queryColumns:{}",cid);
        Column c = columnMapper.selectByPrimaryKey(cid);
        List<Column> cl = columnValueMapper.selectColumnTreeBySortId(userId, c.getColumnLevelPath());
        if (cl != null && cl.size() > 0) {
            return ConvertUtil.convert2Node(cl, "userId", "id", "columnname", "parentId", "columnLevelPath");
        }
        return null;
    }

    @Override
    public int beRelation(long id,int t,long cid, Long userId) {
        logger.info("com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.beRelation:{}",cid);
        UserPermissionExample example = new UserPermissionExample();
        example.createCriteria().andKnowledgeIdEqualTo(cid).andSendUserIdEqualTo(userId);
        int count = countKnowledgeByMongo(id,t,cid,userId);
        if (count > 0) {
            return Constants.Relation.self.v();//自己
        }
        example.createCriteria().andKnowledgeIdEqualTo(cid).andReceiveUserIdEqualTo(-1l);
        count = userPermissionMapper.countByExample(example);
        if (count > 0) {
            return Constants.Relation.platform.v();//全平台
        }
        example.createCriteria().andKnowledgeIdEqualTo(cid).andReceiveUserIdEqualTo(0l);
        count = userPermissionMapper.countByExample(example);
        if (count > 0) {
            return Constants.Relation.jinTN.v();//gt
        }
        return Constants.Relation.friends.v();//好友可见
    }

    private int countKnowledgeByMongo(long id, int t, long cid, long userId) {
        String[] names = Constants.Type.values()[t-1].obj().split("\\.");
        int length = names.length;
        Criteria criteria = Criteria.where("status").is(4);
        criteria.and("cid").is(userId).and("_id").is(id);
        Query query = new Query(criteria);
        long count = mongoTemplate.count(query, names[length - 1]);
        return (int) count;
    }

    @Override
    public List<KnowledgeStatics> getRankHotList(Long column) {
        logger.info("com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.getRankHotList:{}",column);
        Column c = columnMapper.selectByPrimaryKey(column);
        if (c != null) {
            String clp = c.getColumnLevelPath().substring(0, 9);
            byte type = (byte) Integer.parseInt(clp);
            KnowledgeStaticsExample ce = new KnowledgeStaticsExample();
            ce.createCriteria().andSourceEqualTo((short) 0).andTypeEqualTo((short) type);
            ce.setLimitStart(0);
            ce.setLimitEnd(10);
            ce.setOrderByClause("(shareCount * 0.1 + commentcount * 0.2 + collectioncount * 0.3 + clickcount * 0.4)/4 DESC");
            return knowledgeStaticsMapper.selectByExample(ce);
        }
        return null;
    }
}
