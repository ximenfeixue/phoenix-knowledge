package com.ginkgocap.ywxt.knowledge.dao.knowledgecolumn.impl;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.knowledgecolumn.KnowledgeColumnDao;
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
        } 
        
        return null;

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
    public long countByPidAndName(final long parentColumnId,final String columnName) {
        @SuppressWarnings("serial")
        Map<String ,Object> params=new HashMap<String,Object>(){
            {
                put("parentColumnId",parentColumnId);
                put("columnName",columnName);
            }
        };
        long count =  (Long) getSqlMapClientTemplate().queryForObject("tb_knowledge_column.countByPidAndName", params);
        return count;
    }

    @Override
    public void delById(long id) {
        
        Date updateTime=new Date();
        Map<String ,Object> map=new HashMap<String,Object>();
        map.put("id",id);
        map.put("updateTime",updateTime);
        map.put("delStatus",1);
        
        getSqlMapClientTemplate().update("tb_knowledge_column.delById", map,1);
    }
    
    public void clearById(long id){
        getSqlMapClientTemplate().delete("tb_knowledge_column.clearById", id,1);
    }

    @Override
    public List<KnowledgeColumn> queryByParentId(final long parentColumnId,final long createUserId) {
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

    @SuppressWarnings("unchecked")
    @Override
    public List<KnowledgeColumn> queryByUserId(long createUserId) {
        List<KnowledgeColumn> list=getSqlMapClientTemplate().queryForList("tb_knowledge_column.selectByUserId", createUserId);
        return list;
    }

    @Override
    public List<KnowledgeColumn> queryByUserIdAndSystem(long createUserId, long systemId) {
        
        Map<String ,Object> map=new HashMap<String,Object>();
        map.put("createUserId", createUserId);
        map.put("systemId", systemId);
        
        @SuppressWarnings("unchecked")
        List<KnowledgeColumn> list=getSqlMapClientTemplate().queryForList("tb_knowledge_column.selectByUserIdAndSystem", map);
        return list;
    }

    @Override
    public List<KnowledgeColumn> queryAll() {
        @SuppressWarnings("unchecked")
        List<KnowledgeColumn> list=getSqlMapClientTemplate().queryForList("tb_knowledge_column.selectAll");
        return list;
    }

    @Override
    public List<KnowledgeColumn> queryAllDel() {
        @SuppressWarnings("unchecked")
        List<KnowledgeColumn> list=getSqlMapClientTemplate().queryForList("tb_knowledge_column.selectAllDel");
        return list;
    }

    @Override
    public void recoverOneKC(long id) {
        Date update_time=new Date();
        Map<String ,Object> map=new HashMap<String,Object>();
        map.put("id",id);
        map.put("updateTime",update_time);
        map.put("delStatus",0);
        
        getSqlMapClientTemplate().update("tb_knowledge_column.recoverOneKC", map);
    }

    @Override
    public List<KnowledgeColumn> querySubByUserId(long createUserId) {
        @SuppressWarnings("unchecked")
        List<KnowledgeColumn> list=getSqlMapClientTemplate().queryForList("tb_knowledge_column.selectSubByUserId",createUserId);
        return list;
    }

    @Override
    public List<KnowledgeColumn> querySubBySystem(long systemId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<KnowledgeColumn> selectCategoryTreeBySortId(long userId, String sortId) {
        Map<String ,Object> map=new HashMap<String,Object>();
        map.put("createUserId",userId);
        map.put("columnLevelPath",sortId);
        List<KnowledgeColumn> list=getSqlMapClientTemplate().queryForList("tb_knowledge_column.selectCategoryTreeBySortId",map);
        return list;
    }

    @Override
    public List<KnowledgeColumn> selectFullPath(long id) {
        // TODO Auto-generated method stub
        return null;
    }

}
