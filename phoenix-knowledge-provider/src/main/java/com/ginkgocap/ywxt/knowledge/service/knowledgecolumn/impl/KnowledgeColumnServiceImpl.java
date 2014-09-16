package com.ginkgocap.ywxt.knowledge.service.knowledgecolumn.impl;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.knowledgecolumn.KnowledgeColumnDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeColumn;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeColumnService;
import com.ginkgocap.ywxt.knowledge.util.tree.ConvertUtil;
import com.ginkgocap.ywxt.knowledge.util.tree.Tree;

@Service("knowledgeColumnService")
public class KnowledgeColumnServiceImpl implements KnowledgeColumnService {

    @Autowired
    private KnowledgeColumnDao knowledgeColumnDao;

    @Override
    public KnowledgeColumn saveOrUpdate(KnowledgeColumn kc) {

        if (null == kc) {
            return null;
        }

        Long id = kc.getId();
        Date date = new Date();

        if (null == id || id.intValue() <= 0) {

            kc.setCreateTime(date);
            kc.setUpdateTime(date);
            kc.setDelStatus(0);
            kc.setSubscribeCount(0);
            return knowledgeColumnDao.insert(kc);
        }

        if (id > 0) {
            //          KnowledgeColumn okc= knowledgeColumnDao.queryById(kc.getId());
            //          
            //          if(null != okc){
            //              okc.setColumnLevelPath(kc.getColumnLevelPath());
            //              okc.setSubscribeCount(kc.getSubscribeCount());
            //              okc.setUpdateTime(date);
            //              knowledgeColumnDao.update(okc);
            //          }

            kc.setUpdateTime(date);
            return knowledgeColumnDao.update(kc);
        }

        return null;
    }

    @Override
    public KnowledgeColumn queryById(long id) {
        return knowledgeColumnDao.queryById(id);
    }

    @Override
    public boolean isExist(int parentColumnId, String columnName) {
        if (knowledgeColumnDao.countByPidAndName(parentColumnId, columnName) > 0) {
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
    public void recoverOneKC(Long id) {
        if (null == id || id.longValue() < 0) {
            return;
        }

        knowledgeColumnDao.recoverOneKC(id);
    }

    @Override
    public void clearById(long id) {
        knowledgeColumnDao.clearById(id);
    }

    @Override
    public List<KnowledgeColumn> querySubByUserId(long createUserId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<KnowledgeColumn> querySubBySystem(long systemId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String selectColumnTreeBySortId(long userId, String sortId, String status) {
        List<KnowledgeColumn> cl = knowledgeColumnDao.selectCategoryTreeBySortId(userId, sortId);
        if (cl != null && cl.size() > 0) {
            return JSONObject.fromObject(Tree.build(ConvertUtil.convert2Node(cl, "id", "columName", "parentColumnId", "columnLevelPath")))
                    .toString();
        }
        return "";
    }

}
