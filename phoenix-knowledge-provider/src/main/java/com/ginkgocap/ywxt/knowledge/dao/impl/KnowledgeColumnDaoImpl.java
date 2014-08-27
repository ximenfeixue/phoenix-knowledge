package com.ginkgocap.ywxt.knowledge.dao.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeColumnDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeColumn;
import com.ibatis.sqlmap.client.SqlMapClient;

@Component("knowledgeColumnDao")
public class KnowledgeColumnDaoImpl extends SqlMapClientDaoSupport implements KnowledgeColumnDao {
    @Autowired
    SqlMapClient sqlMapClient;

    @PostConstruct
    public void initSqlMapClient() {
        super.setSqlMapClient(sqlMapClient);
    }
    
    @Override
    public KnowledgeColumn insert(KnowledgeColumn kc) {
        Long id = (Long) getSqlMapClientTemplate().insert("tb_knowledge_column.insert", kc);
        if (id != null) {
            kc.setId(id);
            return kc;
        } else {
            return null;
        }
    }

    @Override
    public KnowledgeColumn update(KnowledgeColumn kc) {
        getSqlMapClientTemplate().update("tb_knowledge_column.update", kc);
        return kc;
    }

    @Override
    public KnowledgeColumn queryById(long id) {
        KnowledgeColumn kl = (KnowledgeColumn) getSqlMapClientTemplate().queryForObject("tb_knowledge_column.selectById", id);
        return kl;
    }

    @Override
    public long countByPidAndName(final int parentColumnId,final String columnName) {
        Map<String ,Object> params=new HashMap<String,Object>(){
            {
                put("parentColumnId",parentColumnId);
                put("column",columnName);
            }
        };
        long count =  (Long) getSqlMapClientTemplate().queryForObject("tb_knowledge_column.countByPidAndName", params);
        return count;
    }

    @Override
    public void delById(long id) {
        getSqlMapClientTemplate().delete("tb_knowledge_column.delById", id);
    }

    @Override
    public List<KnowledgeColumn> queryByParentId(final int parentColumnId,final int createUserId) {
        @SuppressWarnings("serial")
        Map<String ,Object> params=new HashMap<String,Object>(){
            {
                put("parentColumnId",parentColumnId);
                put("createUserId",createUserId);
            }
        };
        
        @SuppressWarnings("unchecked")
        List<KnowledgeColumn> list=getSqlMapClientTemplate().queryForList("tb_knowledge_column.selectByParentId", params);
        return list;
    }

}
