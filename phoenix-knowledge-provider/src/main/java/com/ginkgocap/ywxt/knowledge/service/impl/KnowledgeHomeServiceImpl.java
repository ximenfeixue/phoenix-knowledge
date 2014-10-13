package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.ArrayList;
import java.util.List;
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
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStatics;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStaticsExample;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnKnowledgeMapper;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnKnowledgeValueMapper;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnMapper;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnValueMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeCategoryValueMapper;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeStaticsMapper;
import com.ginkgocap.ywxt.knowledge.mapper.UserPermissionValueMapper;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeHomeService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.util.PageUtil;

@Service("knowledgeHomeService")
public class KnowledgeHomeServiceImpl implements KnowledgeHomeService {

    @Autowired
    KnowledgeStaticsMapper knowledgeStaticsMapper;
    @Autowired
    ColumnValueMapper columnValueMapper;
    @Autowired
    KnowledgeCategoryValueMapper knowledgeCategoryValueMapper;
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
            byte type = c.getType();
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

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> selectAllByParam(T t, int state ,String columnid, Long userid, int page, int size) {
        String[] names = t.getClass().getName().split("\\.");
        int length=names.length;
        if(state==1||state==2){//目录
            List<Long> ids=knowledgeCategoryValueMapper.selectKnowledgeIds(userid, 0,columnid,Constants.gtnid);
            Criteria criteria = new Criteria();
            criteria.and("uid").is(userid).and("status").is(4);
            if(state==1){
                criteria.and("id").in(ids);
            }else{
                ids=knowledgeCategoryValueMapper.selectKnowledgeIds(userid, 1,columnid,Constants.gtnid);
                criteria.and("id").in(ids);
                
            }
            Query query = new Query(criteria);
            query.sort().on("createtime", Order.DESCENDING);
            long count = mongoTemplate.count(query, names[length-1]);
            PageUtil p = new PageUtil((int) count, page, size);
            query.limit(size);
            query.skip(p.getPageStartRow() - 1);
            return (List<T>) mongoTemplate.find(query, t.getClass(),names[length-1]);
        }
        //栏目
        Long cid=Long.parseLong(columnid);
        //查询栏目类型
        Column column = columnMapper.selectByPrimaryKey(cid);
        long type = column.getType();

        Criteria criteria = new Criteria();
        List<Long> ids = new ArrayList<Long>();

        //查询栏目大类下的数据：自己，好友，全平台3种
        ids = userPermissionValueMapper.selectByParamsSingle(userid, type);

        //查询金桐脑
        List<Long> getIds = columnKnowledgeValueMapper.selectKnowledgeIds(Constants.gtnid, columnid);
        ids.addAll(getIds);
        
        //查询资讯
        if (ids != null) {
            criteria.and("_id").in(ids);
        }
        Query query = new Query(criteria);
        query.sort().on("createtime", Order.DESCENDING);
        long count = mongoTemplate.count(query, names[length-1]);
        PageUtil p = new PageUtil((int) count, page, size);
        query.limit(size);
        query.skip(p.getPageStartRow() - 1);
        return (List<T>) mongoTemplate.find(query, t.getClass(),names[length-1]);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> selectIndexByParam(Constants.Type ty, int page, int size) {
        String[] names = ty.obj().split("\\.");
        int length=names.length;
        Criteria criteria = new Criteria();
        List<Long> ids = new ArrayList<Long>();
        
        //查询栏目大类下的数据：全平台
        ids = userPermissionValueMapper.selectByParamsSingle(null, (long) ty.v());
        ColumnKnowledgeExample ckme = new ColumnKnowledgeExample();
        
        //查询金桐脑
        ckme.createCriteria().andUserIdEqualTo(Constants.gtnid).andTypeEqualTo((short) ty.v());
        List<ColumnKnowledge> ckl = columnKnowledgeMapper.selectByExample(ckme);
        for (ColumnKnowledge c : ckl) {
            ids.add(c.getColumnId());
        }
        
        //查询资讯
        if (ids != null) {
            criteria.and("id").in(ids);
        }
        Query query = new Query(criteria);
        query.sort().on("createtime", Order.DESCENDING);
        long count;
        try {
            count = mongoTemplate.count(query, names[length-1]);
            PageUtil p = new PageUtil((int) count, page, size);
            query.limit(size);
            query.skip(p.getPageStartRow() - 1);
            return (List<T>) mongoTemplate.find(query, Class.forName(ty.obj()), names[length-1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public KnowledgeStatics getPl(long id) {
        return knowledgeStaticsMapper.selectByPrimaryKey(id);
    }
}
