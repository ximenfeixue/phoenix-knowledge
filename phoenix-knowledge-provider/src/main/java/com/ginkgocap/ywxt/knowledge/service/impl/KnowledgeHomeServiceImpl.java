package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
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

    @Override
    public List<KnowledgeStatics> getRankList(Long columnid) {
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
        return columnValueMapper.selectByParam(column, Constants.gtnid, userId);
    }

    @SuppressWarnings({ "unchecked", "static-access" })
    @Override
    public <T> Map<String,Object> selectAllByParam(T t, int state, String columnid, Long userid, int page, int size) {
        Map<String, Object> model = new HashMap<String, Object>();
        String[] names = t.getClass().getName().split("\\.");
        int length = names.length;
        //栏目
        Long cid = Long.parseLong(columnid);
        //查询栏目类型
        Column column = columnMapper.selectByPrimaryKey(cid);
        int leng = column.getColumnLevelPath().length();
        String ty = column.getColumnLevelPath().substring(0, 9);
        long type = Long.parseLong(ty);
        Criteria criteria =Criteria.where("status").is(4);
        Criteria criteriaPj = new Criteria();
        Criteria criteriaUp = new Criteria();
        Criteria criteriaGt = new Criteria();
        List<Long> ids = new ArrayList<Long>();

        //查询栏目大类下的数据：自己，好友，全平台3种
        ids = userPermissionValueMapper.selectByParamsSingle(userid, type);

        //查询金桐脑
//        List<Long> getIds = columnKnowledgeValueMapper.selectKnowledgeIds(Constants.gtnid, columnid);
//        ids.addAll(getIds);
        //查询资讯
        if (ids != null) {
            criteriaUp.and("_id").in(ids);
        }
        if (leng >= 10 ) {//子栏目查询
            List<Column>rcl=columnService.selectFullPath(cid);
            StringBuffer refull = new StringBuffer();
            int count=0;
            for(Column v:rcl){
                count++;
                refull.append(v.getColumnname());
                if(rcl.size()!=count){
                    refull.append("/");
                }
            }
            criteriaUp.and("cpathid").regex(refull.toString());
        }
        criteriaGt.and("cid").is(Constants.gtnid);
        criteriaPj.orOperator(criteriaUp,criteriaGt);
        criteria.andOperator(criteriaPj);
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

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> selectIndexByParam(Constants.Type ty, int page, int size) {
        String[] names = ty.obj().split("\\.");
        int length = names.length;
        Criteria criteria =Criteria.where("status").is(4);
        Criteria criteriaPj = new Criteria();
        Criteria criteriaUp = new Criteria();
        Criteria criteriaGt = new Criteria();
        List<Long> ids = new ArrayList<Long>();

        //查询栏目大类下的数据：全平台
        ids = userPermissionValueMapper.selectByParamsSingle(null, (long) ty.v());
//        ColumnKnowledgeExample ckme = new ColumnKnowledgeExample();

        //查询金桐脑
//        ckme.createCriteria().andUserIdEqualTo(Constants.gtnid).andTypeEqualTo((short) ty.v());
//        List<ColumnKnowledge> ckl = columnKnowledgeMapper.selectByExample(ckme);
//        for (ColumnKnowledge c : ckl) {
//            ids.add(c.getColumnId());
//        }

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
        return knowledgeStaticsMapper.selectByPrimaryKey(id);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Map<String, Object> selectAllKnowledgeCategoryByParam(String tid, String lid, int state, String sortid,
            Long userid, int page, int size) {
        int start = (page - 1) * size;
        int count = knowledgeCategoryValueMapper.countKnowledgeIds(userid, state, sortid, Constants.gtnid, tid, lid);
        List kcl = knowledgeCategoryValueMapper.selectKnowledgeIds(userid, state, sortid,
                Constants.gtnid, tid, lid,start,size);
        PageUtil p = new PageUtil(count, page, size);
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("page", p);
        m.put("list", kcl);
        return m;
    }

    @Override
    public List<Node> queryColumns(long cid, long userId) {
        Column c = columnMapper.selectByPrimaryKey(cid);
        List<Column> cl = columnValueMapper.selectColumnTreeBySortId(userId, c.getColumnLevelPath());
        if (cl != null && cl.size() > 0) {
            return ConvertUtil.convert2Node(cl, "userId", "id", "columnname", "parentId", "columnLevelPath");
        }
        return null;
    }

    @Override
    public int beRelation(long cid, long userId) {
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
