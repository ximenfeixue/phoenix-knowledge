package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.entity.Column;
import com.ginkgocap.ywxt.knowledge.entity.ColumnExample;
import com.ginkgocap.ywxt.knowledge.entity.ColumnExample.Criteria;
import com.ginkgocap.ywxt.knowledge.entity.ColumnVisible;
import com.ginkgocap.ywxt.knowledge.entity.ColumnVisibleExample;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnMapper;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnValueMapper;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnVisibleMapper;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnVisibleValueMapper;
import com.ginkgocap.ywxt.knowledge.service.ColumnService;
import com.ginkgocap.ywxt.knowledge.service.ColumnVisibleService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.knowledge.util.KCHelper;
import com.ginkgocap.ywxt.knowledge.util.tree.ConvertUtil;
import com.ginkgocap.ywxt.knowledge.util.tree.Tree;

@Service("columnVisibleService")
public class ColumnVisibleServiceImpl implements ColumnVisibleService {

    @Autowired
    ColumnService columnService;
    @Autowired
    ColumnVisibleMapper columnVisibleMapper;
    @Autowired
    ColumnVisibleValueMapper columnVisibleValueMapper;
    @Autowired
    ColumnValueMapper columnValueMapper;

    @Override
    public List<ColumnVisible> queryListByPidAndUserId(long userid, long cid) {
        ColumnVisibleExample cm = new ColumnVisibleExample();
        cm.createCriteria().andUserIdEqualTo(userid).andPcidEqualTo(cid);
        List<ColumnVisible> cs = columnVisibleMapper.selectByExample(cm);
        if (cs != null) {
            return cs;
        }
        return null;
    }

    @Override
    public ColumnVisible queryListByCidAndUserId(long userid, long cid) {
        ColumnVisibleExample cm = new ColumnVisibleExample();
        cm.createCriteria().andUserIdEqualTo(userid).andColumnIdEqualTo(cid);
        List<ColumnVisible> cs = columnVisibleMapper.selectByExample(cm);
        if (cs != null && cs.size() == 1) {
            return cs.get(0);
        }
        return null;
    }

    @Override
    public void updateCids(long userid, String cids,long pcid) {
        String[] ids = cids.split(",");
        List<Long> idl = new ArrayList<Long>();
        for (String id : ids) {
            try {
                long c = Long.parseLong(id);
                idl.add(c);
            } catch (Exception e) {
            }
        }
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("instate", 0);
        m.put("pcid", pcid);
        m.put("utime", new Date());
        m.put("userId", userid);
        m.put("state", 0);
        m.put("listl", idl);
        columnVisibleValueMapper.update(m);
        for(Long id:idl){
            ColumnVisibleExample cm = new ColumnVisibleExample();
            cm.createCriteria().andUserIdEqualTo(userid).andColumnIdEqualTo(id);
            List<ColumnVisible> cs = columnVisibleMapper.selectByExample(cm);
            if(cs.size()>0){
                ColumnVisible cv = cs.get(0);
                String sortId =cv.getSortId();
                m.put("sortId",sortId);
                columnVisibleValueMapper.updateChild(m);
            }
        }
        m.put("instate", 1);
        m.put("state", 1);
        columnVisibleValueMapper.update(m);
        List<Long>idnl =columnVisibleValueMapper.selectNotinIds(m);
        for(Long id:idnl){
            ColumnVisibleExample cm = new ColumnVisibleExample();
            cm.createCriteria().andUserIdEqualTo(userid).andColumnIdEqualTo(id);
            List<ColumnVisible> cs = columnVisibleMapper.selectByExample(cm);
            if(cs.size()>0){
                ColumnVisible cv = cs.get(0);
                String sortId =cv.getSortId();
                m.put("sortId",sortId);
                columnVisibleValueMapper.updateChild(m);
            }
        }
    }

    @Override
    public void del(long id) {
        columnVisibleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public long countListByPidAndUserId(long userid, Long pid) {
        ColumnVisibleExample cm = new ColumnVisibleExample();
        com.ginkgocap.ywxt.knowledge.entity.ColumnVisibleExample.Criteria cri = cm.createCriteria();
        cri.andUserIdEqualTo(userid);
        if (pid != null) {
            cri.andPcidEqualTo(pid);
        }
        long cs = columnVisibleMapper.countByExample(cm);
        return cs;
    }

    @Override
    public void init(long userid, long gtnid) {
        List<Column> l = columnValueMapper.selectByParam(null, Constants.gtnid, userid);
        for (Column c : l) {
            if(!c.getColumnname().equals("未分组")){
                long id = c.getId();
                long pcid = c.getParentId();
                String cname = c.getColumnname();
                ColumnVisible cv = new ColumnVisible();
                cv.setColumnId(id);
                cv.setPcid(pcid);
                cv.setUserId(userid);
                cv.setCtime(new Date());
                cv.setUtime(new Date());
                cv.setSortId(c.getColumnLevelPath());
                cv.setColumnName(cname);
                cv.setState((short) 0);
                columnVisibleMapper.insert(cv);
            }
        }
    }

    @Override
    public void saveCid(long userid, long cid) {
        Column c = columnService.queryById(cid);
        long id = c.getId();
        long pcid = c.getParentId();
        String cname = c.getColumnname();
        ColumnVisible cv = new ColumnVisible();
        cv.setColumnId(id);
        cv.setPcid(pcid);
        cv.setUserId(userid);
        cv.setCtime(new Date());
        cv.setUtime(new Date());
        cv.setColumnName(cname);
        cv.setSortId(c.getColumnLevelPath());
        cv.setState((short) 0);
        columnVisibleMapper.insert(cv);
    }

    @Override
    public void delByUserIdAndColumnId(long userid, long cid) {
        ColumnVisibleExample cm = new ColumnVisibleExample();
        com.ginkgocap.ywxt.knowledge.entity.ColumnVisibleExample.Criteria cri = cm.createCriteria();
        cri.andUserIdEqualTo(userid);
        cri.andColumnIdEqualTo(cid);
        columnVisibleMapper.deleteByExample(cm);
    }

    @Override
    public void saveOrUpdate(Column c) {
        Long id = c.getId();
        long pcid = c.getParentId();
        long uid = c.getUserId();
        String cname = c.getColumnname();
        if(id==null){//新增栏目
            ColumnVisible cv = new ColumnVisible();
            cv.setColumnId(id);
            cv.setPcid(pcid);
            cv.setUserId(uid);
            cv.setCtime(new Date());
            cv.setUtime(new Date());
            cv.setColumnName(cname);
            cv.setSortId(c.getColumnLevelPath());
            cv.setState((short) 0);
            columnVisibleMapper.insert(cv);
        }else{//修改栏目
            ColumnVisible cv = new ColumnVisible();
            cv.setColumnName(cname);
            cv.setUtime(new Date());
            ColumnVisibleExample e = new ColumnVisibleExample();
            e.createCriteria().andUserIdEqualTo(c.getUserId()).andColumnIdEqualTo(c.getId());
            columnVisibleMapper.updateByExampleSelective(cv, e);
        }
        
    }

}
