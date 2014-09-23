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
import com.ginkgocap.ywxt.knowledge.mapper.ColumnValueMapper;
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
    public List<KnowledgeStatics> getRankList(Short type) {
        KnowledgeStaticsExample ce = new KnowledgeStaticsExample();
        ce.createCriteria().andSourceEqualTo((short) 0).andTypeEqualTo(type);
        ce.setLimitStart(0);
        ce.setLimitEnd(10);
        ce.setOrderByClause("commentCount desc");
        return knowledgeStaticsMapper.selectByExample(ce);
    }

    @Override
    public List<Column> getTypeList(long userId, long column, long pid) {
        return columnValueMapper.selectByParam(pid, column, userId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> selectAllByParam(T t, long type, boolean isBigColumn, Long columnid, Long userid, int page,
            int size) {

        Criteria criteria = new Criteria();
        List<Long> ids = new ArrayList<Long>();

        //查询栏目大类下的数据：自己，好友，全平台3种
        ids = userPermissionValueMapper.selectByParamsSingle(userid, type);
        ColumnKnowledgeExample ckme = new ColumnKnowledgeExample();
        
        //查询金桐脑(需要判断是否为栏目大类)
        if (isBigColumn) {
            ckme.createCriteria().andUserIdEqualTo(Constants.gtnid).andTypeEqualTo((short) type);
        } else {
            ckme.createCriteria().andUserIdEqualTo(Constants.gtnid).andColumnIdEqualTo(columnid);
            //过滤出栏目小类
            ColumnKnowledgeExample ckm = new ColumnKnowledgeExample();
            ckm.createCriteria().andKnowledgeIdIn(ids).andColumnIdEqualTo(columnid);
            List<ColumnKnowledge> ckl = columnKnowledgeMapper.selectByExample(ckm);
            ids = new ArrayList<Long>();
            for (ColumnKnowledge c : ckl) {
                ids.add(c.getColumnId());
            }
        }
        
        List<ColumnKnowledge> ckl = columnKnowledgeMapper.selectByExample(ckme);
        for (ColumnKnowledge c : ckl) {
            ids.add(c.getColumnId());
        }

        //查询资讯
        if (ids != null) {
            criteria.and("_id").in(ids);
        }
        Query query = new Query(criteria);
        query.sort().on("createtime", Order.DESCENDING);
        long count = mongoTemplate.count(query, t.getClass());
        PageUtil p = new PageUtil((int) count, page, size);
        query.limit(p.getPageStartRow() - 1);
        query.skip(size);
        return (List<T>) mongoTemplate.find(query, t.getClass());
    }
}
