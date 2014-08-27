package com.ginkgocap.ywxt.knowledge.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeColumnDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeColumn;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeColumnService;

@Service("knowledgeColumnService")
public class KnowledgeColumnServiceImpl implements KnowledgeColumnService {

    @Autowired
    private KnowledgeColumnDao knowledgeColumnDao;
    
    @Override
    public KnowledgeColumn saveOrUpdate(KnowledgeColumn kc) {
        long id = kc.getId();
        if (id > 0) {
            knowledgeColumnDao.update(kc);
        } else {
            kc = knowledgeColumnDao.insert(kc);
        }
        return kc;
    }

    @Override
    public KnowledgeColumn queryById( long id) {
        return knowledgeColumnDao.queryById(id);
    }

    @Override
    public boolean isExist(int parentColumnId, String columnName) {
        if(knowledgeColumnDao.countByPidAndName(parentColumnId, columnName)>0){
            return true;
        }
        return false;
    }

    @Override
    public void delById(long id) {
         knowledgeColumnDao.delById(id);
    }

    @Override
    public List<KnowledgeColumn> queryByParentId(int parentColumnId, int createUserId) {
        return knowledgeColumnDao.queryByParentId(parentColumnId, createUserId);
    }

}
