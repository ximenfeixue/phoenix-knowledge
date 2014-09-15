package com.ginkgocap.ywxt.knowledge.dao.impl;


import java.util.HashMap; 
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeHomeDao;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeColumn;
import com.ibatis.sqlmap.client.SqlMapClient;

@Component("knowledgeHomeDao")
public class KnowledgeHomeDaoImpl extends SqlMapClientDaoSupport implements KnowledgeHomeDao {
    @Autowired
    SqlMapClient sqlMapClient;

    @PostConstruct
    public void initSqlMapClient() {
        super.setSqlMapClient(sqlMapClient);
    }

    @Override
    public List<Knowledge> getRankList(int type, int column) {
        System.out.println("enter getRanklist");// TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Knowledge> getHomeList(int column, long userId, int pageStart, int pageSize, int sortBy) {
        System.out.println("enter getHomelist");// TODO Auto-generated method stub
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int countHomeList(int column, long userId) {
        System.out.println("enter countHomeList");// TODO Auto-generated method stub
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<KnowledgeColumn> getTypeList(long userId, int column) {
        System.out.println("enter getTypeList");// TODO Auto-generated method stub
        // TODO Auto-generated method stub
        return null;
    }
    
    

}
