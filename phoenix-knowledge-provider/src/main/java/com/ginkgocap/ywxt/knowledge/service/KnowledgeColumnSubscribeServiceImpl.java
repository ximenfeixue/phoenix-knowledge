package com.ginkgocap.ywxt.knowledge.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeColumnDao;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeColumnSubscribeDao;
import com.ginkgocap.ywxt.knowledge.form.KnowledgeSimpleMerge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeColumn;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeColumnSubscribe;

@Service("knowledgeColumnSubscribeService")
public class KnowledgeColumnSubscribeServiceImpl implements KnowledgeColumnSubscribeService {

    @Autowired
    private KnowledgeColumnDao knowledgeColumnDao;
    
    @Autowired
    private KnowledgeColumnSubscribeDao kcsDao;
    
    @Override
    public KnowledgeColumnSubscribe add(KnowledgeColumnSubscribe kcs) {
        KnowledgeColumn kc=knowledgeColumnDao.queryById(kcs.getColumnId());
//        kc.setKcType(kcType)
        Date date=new Date();
        kcs.setSubDate(date);
        return kcsDao.insert(kcs);
    }

    @Override
    public int update(KnowledgeColumnSubscribe kcs) {
        return kcsDao.update(kcs);
    }

    @Override
    public KnowledgeColumnSubscribe merge(KnowledgeColumnSubscribe kcs) {

        if (null == kcs) {
            return null;
        }
        
        Long id =-1l;
        
        //在不使用包装类型的情况下
        try {
            id = kcs.getId();
        } catch (NullPointerException e) {
            e.printStackTrace();
            
            KnowledgeColumnSubscribe kcsi=kcsDao.insert(kcs);
            return kcsi;
        }
        
        if (id>0) {
            this.update(kcs);
            return kcs;
        }
        
        return null;
    }

    @Override
    public void deleteByUIdAndKCId(long userId, long columnId) {
       kcsDao.deleteByUIdAndKCId(userId, columnId);

    }

    @Override
    public void deleteByPK(long id) {
        kcsDao.deleteByPK(id);
    }

    @Override
    public List<KnowledgeColumnSubscribe> selectByUserId(long userId) {
        return kcsDao.selectByUserId(userId);
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
