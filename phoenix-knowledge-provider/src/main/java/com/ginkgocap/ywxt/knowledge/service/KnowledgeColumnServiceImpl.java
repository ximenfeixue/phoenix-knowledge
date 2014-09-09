package com.ginkgocap.ywxt.knowledge.service;

import java.util.Date;
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
        
        if (null == kc) {
            return null;
        }
        
        long id = kc.getId();
        Date date=new Date();
        
        if (id > 0) {
            
//            KnowledgeColumn okc= knowledgeColumnDao.queryById(kc.getId());
//            
//            if(null != okc){
//                okc.setColumnLevelPath(kc.getColumnLevelPath());
//                okc.setSubscribeCount(kc.getSubscribeCount());
//                okc.setUpdateTime(date);
//                knowledgeColumnDao.update(okc);
//            }
            
            kc.setUpdateTime(date);
            knowledgeColumnDao.update(kc);
            
        } else {
            kc.setCreateTime(date);
            kc.setUpdateTime(date);
            kc.setDelStatus(0);
            kc.setSubscribeCount(0);
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

    @Override
    public List<KnowledgeColumn> queryByUserId(long createUserId) {
        return knowledgeColumnDao.queryByUserId(createUserId);
    }

    @Override
    public List<KnowledgeColumn> queryByUserIdAndSystem(long createUserId, long systemId) {
        return knowledgeColumnDao.queryByUserIdAndSystem(createUserId, systemId);
    }

    @Override
    public List<KnowledgeColumn> queryAll() {
        return knowledgeColumnDao.queryAll();
    }

    @Override
    public List<KnowledgeColumn> queryAllDel() {
        return knowledgeColumnDao.queryAllDel();
    }

    @Override
    public void recoverOneKC(long id) {
        knowledgeColumnDao.recoverOneKC(id);
    }

}
