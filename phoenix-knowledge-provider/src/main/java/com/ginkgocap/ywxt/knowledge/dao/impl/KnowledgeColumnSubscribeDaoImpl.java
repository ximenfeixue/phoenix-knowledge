package com.ginkgocap.ywxt.knowledge.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.knowledgecolumn.KnowledgeColumnSubscribeDao;
import com.ginkgocap.ywxt.knowledge.form.KnowledgeSimpleMerge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeColumn;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeColumnSubscribe;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 
 * @author guangyyuan
 *
 */
@Component("knowledgeColumnSubscribeDao")
public class KnowledgeColumnSubscribeDaoImpl extends SqlMapClientDaoSupport implements KnowledgeColumnSubscribeDao {

    @Autowired
    SqlMapClient sqlMapClient;

    @PostConstruct
    public void initSqlMapClient() {
        super.setSqlMapClient(sqlMapClient);
    }
    
    
    @Override
    public KnowledgeColumnSubscribe insert(KnowledgeColumnSubscribe kcs) {
        Long id=(Long) getSqlMapClientTemplate().insert("tb_knowledge_column_subscribe.insert", kcs);
        
        if (null==id) {
            return null;
        }
        
        kcs.setId(id);
        return kcs;
    }

    @Override
    public int update(KnowledgeColumnSubscribe kcs) {
        int r=getSqlMapClientTemplate().update("tb_knowledge_column_subscribe.update", kcs);
        return r;
    }

    @Override
    public void deleteByPK(long id) {
        getSqlMapClientTemplate().delete("tb_knowledge_column_subscribe.deleteByPK", id, 1);
    }
    

    @Override
    public void deleteByUIdAndKCId(long userId, long columnId) {
        
        Map<String, Long> map=new HashMap<String, Long>();
        map.put("userId", userId);
        map.put("columnId", columnId);
        
        getSqlMapClientTemplate().delete("tb_knowledge_column_subscribe.deleteByUIdAndKCId", map, 1);
    }

    @Override
    public List<KnowledgeColumnSubscribe> selectByUserId(long userId) {
        @SuppressWarnings("unchecked")
        List<KnowledgeColumnSubscribe> list=getSqlMapClientTemplate().queryForList("tb_knowledge_column_subscribe.selectByUserId", userId);
        return list;
    }

    @Override
    public List<KnowledgeColumn> selectKCListByUserId(long userId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Long> selectUserIdListByKc(long columnId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<KnowledgeColumnSubscribe> selectByKCId(long columnId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int countByKC(long columnId) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<Integer> countAllByKC(long userId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int countByUserId(long userId) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<KnowledgeSimpleMerge> selectSubKnowByUserId(long userId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<KnowledgeSimpleMerge> selectSubKnowByUserId(long userId, int type) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<KnowledgeSimpleMerge> selectSubKnowByKCList(List<KnowledgeColumn> list) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<KnowledgeSimpleMerge> selectSubKnowByKCList(List<KnowledgeColumn> list, int type) {
        // TODO Auto-generated method stub
        return null;
    }



}
