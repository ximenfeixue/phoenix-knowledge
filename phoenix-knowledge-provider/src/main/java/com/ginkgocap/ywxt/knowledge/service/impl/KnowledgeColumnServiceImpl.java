package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.ArrayList; 
import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.entity.Column;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnMapper;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnValueMapper;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeColumnService;
import com.ginkgocap.ywxt.knowledge.util.tree.ConvertUtil;
import com.ginkgocap.ywxt.knowledge.util.tree.Tree;

@Service("ColumnService")
public class KnowledgeColumnServiceImpl implements KnowledgeColumnService {

    @Autowired
    private ColumnMapper columnMapper;
    @Autowired
    private ColumnValueMapper columnValueMapper;

    @Override
    public Column saveOrUpdate(Column kc) {

        if (null == kc) {
            return null;
        }

        Long id = kc.getId();
        Date date = new Date();

        if (null == id || id.intValue() <= 0) {

            kc.setCreatetime(date);
            kc.setUpdateTime(date);
            kc.setDelStatus((byte)0);
            kc.setSubscribeCount(0l);
            columnMapper.insert(kc);
            return kc;
        }

        if (id > 0) {
            //          Column okc= ColumnDao.queryById(kc.getId());
            //          
            //          if(null != okc){
            //              okc.setColumnLevelPath(kc.getColumnLevelPath());
            //              okc.setSubscribeCount(kc.getSubscribeCount());
            //              okc.setUpdateTime(date);
            //              ColumnDao.update(okc);
            //          }

            kc.setUpdateTime(date);
            columnMapper.updateByPrimaryKey(kc);
            return kc;
        }

        return null;
    }

    @Override
    public Column queryById(long id) {
        return columnMapper.selectByPrimaryKey(id);
    }

//    @Override
//    public boolean isExist(int parentColumnId, String columnName) {
//        if (ColumnDao.countByPidAndName(parentColumnId, columnName) > 0) {
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public void delById(long id) {
//        ColumnDao.delById(id);
//    }
//
//    @Override
//    public List<Column> queryByParentId(int parentColumnId, int createUserId) {
//        return ColumnDao.queryByParentId(parentColumnId, createUserId);
//    }
//
//    @Override
//    public List<Column> queryByUserId(long createUserId) {
//        return ColumnDao.queryByUserId(createUserId);
//    }
//
//    @Override
//    public List<Column> queryByUserIdAndSystem(long createUserId, long systemId) {
//        return ColumnDao.queryByUserIdAndSystem(createUserId, systemId);
//    }
//
//    @Override
//    public List<Column> queryAll() {
//        return ColumnDao.queryAll();
//    }
//
//    @Override
//    public List<Column> queryAllDel() {
//        return ColumnDao.queryAllDel();
//    }
//
//    @Override
//    public void recoverOneKC(Long id) {
//        if (null == id || id.longValue() < 0) {
//            return;
//        }
//
//        ColumnDao.recoverOneKC(id);
//    }
//
//    @Override
//    public void clearById(long id) {
//        ColumnDao.clearById(id);
//    }
//
//    @Override
//    public List<Column> querySubByUserId(long createUserId) {
//        return ColumnDao.querySubByUserId(createUserId);
//    }


    @Override
    public String selectColumnTreeBySortId(long userId, String sortId, String status) {
        List<Column> cl = columnValueMapper.selectCategoryTreeBySortId(userId, sortId);
        if (cl != null && cl.size() > 0) {
            return JSONObject.fromObject(Tree.build(ConvertUtil.convert2Node(cl, "id", "columname", "parentId", "columnLevelPath")))
                    .toString();
        }
        return "";
    }

    
    @Override
    public List<Column> selectFullPath(long id) {
        List<Column> l = new ArrayList<Column>();
        Column k = this.queryById(id);
        if (k != null) {
            l.add(k);
            getFullPath(l, k.getParentId());
            Collections.reverse(l);
        }
        return l;
    }

    
    public void getFullPath(List<Column> l, long pid) {
        if (pid == 0) {
            return;
        }
        Column kn = this.queryById(pid);
        l.add(kn);
    }

    @Override
    public boolean isExist(int parentColumnId, String columnName) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void delById(long id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<Column> queryByParentId(int parentColumnId, int createUserId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Column> queryByUserId(long createUserId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Column> querySubByUserId(long createUserId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Column> querySubBySystem(long systemId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Column> queryByUserIdAndSystem(long createUserId, long systemId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Column> queryAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Column> queryAllDel() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void recoverOneKC(Long id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void clearById(long id) {
        // TODO Auto-generated method stub
        
    }
    
}
