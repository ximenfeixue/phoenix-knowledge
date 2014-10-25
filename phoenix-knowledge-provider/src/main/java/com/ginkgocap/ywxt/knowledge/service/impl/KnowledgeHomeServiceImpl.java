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
        String columnname = column.getColumnname();
        int leng = column.getColumnLevelPath().length();
        String ty = column.getColumnLevelPath().substring(0, 9);
        long type = Long.parseLong(ty);
        Criteria criteria =Criteria.where("status").is(4);
        Criteria criteriaPj = new Criteria();
        Criteria criteriaUp = new Criteria();
        Criteria criteriaMy = new Criteria();
        Criteria criteriaGt = new Criteria();
        Criteria criteriaGtz = new Criteria();
        Criteria criteriaGto = new Criteria();
        List<Long> ids = new ArrayList<Long>();
        StringBuffer refull = new StringBuffer();
        //栏目类型过滤
        ids = userPermissionValueMapper.selectByParamsSingle(userid, type);
        //查询资讯
        if (ids != null) {
            criteriaUp.and("_id").in(ids);
        }
        //子栏目查询,否则为栏目类型查询
        if (leng >= 10 ) {
            List<Column>rcl=columnService.selectFullPath(cid);
            int count=0;
            for(Column v:rcl){
                count++;
                refull.append(v.getColumnname());
                if(rcl.size()!=count){
                    refull.append("/");
                }
            }
            criteriaUp.and("cpathid").regex(refull.toString()+".*$").and("cid").is(userid);
        }else{
            //一级栏目为自定义的情形
            if (cid > 11) {
                refull=new StringBuffer();
                refull.append(columnname);
                criteriaUp = new Criteria().and("cid").is(userid).and("cpathid").is(refull.toString());
            }
        }
        criteriaMy.and("cid").is(userid);
        criteriaGtz=criteriaGtz.and("cid").is(Constants.gtnid).and("cpathid").is(refull.toString());
        criteriaGto=criteriaGto.and("cid").is(Constants.gtnid).and("cpathid").regex(refull.toString()+"/.*$");
        criteriaGt.orOperator(criteriaGtz,criteriaGto);
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
    public int beRelation(long cid, long userId) {
        logger.info("com.ginkgocap.ywxt.knowledge.service.impl.KnowledgeHomeService.beRelation:{}",cid);
        UserPermissionExample example = new UserPermissionExample();
        example.createCriteria().andKnowledgeIdEqualTo(cid).andReceiveUserIdEqualTo(userId);
        int count = userPermissionMapper.countByExample(example);
        if (count > 0) {
            return 1;//自己
        }
        example.createCriteria().andKnowledgeIdEqualTo(cid).andReceiveUserIdEqualTo(-1l);
        count = userPermissionMapper.countByExample(example);
        if (count > 0) {
            return 4;//全平台
        }
        example.createCriteria().andKnowledgeIdEqualTo(cid).andSendUserIdEqualTo(0l);
        count = userPermissionMapper.countByExample(example);
        if (count > 0) {
            return 3;//gt
        }
        return 2;//好友可见
    }
}
