package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.HashMap; 
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeHomeDao;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeColumn;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeHomeService;
import com.ginkgocap.ywxt.util.PageUtil;

@Service("knowledgeHomeService")
public class KnowledgeHomeServiceImpl implements KnowledgeHomeService {

    @Autowired
    KnowledgeHomeDao knowledgeHomeDao;

    @Override
    public List<Knowledge> getRankList(int type, int column) {
        return knowledgeHomeDao.getRankList(type, column);
    }

    @Override
    public Map<String, Object> getHomeList(int column, long userId, int pageNo, int pageSize, int sortBy) {
        Map<String,Object> m =new HashMap<String ,Object>();
        int c = knowledgeHomeDao.countHomeList(column, userId);
        PageUtil p=new PageUtil(c, pageNo, pageSize);
        List<Knowledge> l = knowledgeHomeDao.getHomeList(column, userId, p.getPageStartRow()-1, pageSize, sortBy);
        m.put("list", l);
        m.put("page", p);
        return null;
    }

    @Override
    public List<KnowledgeColumn> getTypeList(long userId, int column) {
        return knowledgeHomeDao.getTypeList(userId, column);
    }

}
