package com.ginkgocap.ywxt.knowledge.dao.impl;

import java.util.List;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeColumnSubscribeDao;
import com.ginkgocap.ywxt.knowledge.form.KnowledgeSimpleMerge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeColumn;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeColumnSubscribe;

public class KnowledgeColumnSubscribeDaoImpl implements KnowledgeColumnSubscribeDao {

    @Override
    public KnowledgeColumnSubscribe insert(KnowledgeColumnSubscribe kcs) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public KnowledgeColumnSubscribe update(KnowledgeColumnSubscribe kcs) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public KnowledgeColumnSubscribe merge(KnowledgeColumnSubscribe kcs) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int deleteByPK(long id) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<KnowledgeColumnSubscribe> selectByUserId(long userId) {
        // TODO Auto-generated method stub
        return null;
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
