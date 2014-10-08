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
import com.ginkgocap.ywxt.knowledge.service.ColumnService;
import com.ginkgocap.ywxt.knowledge.service.ColumnVisibleService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.knowledge.util.KCHelper;
import com.ginkgocap.ywxt.knowledge.util.tree.ConvertUtil;
import com.ginkgocap.ywxt.knowledge.util.tree.Tree;

@Service("columnVisibleService")
public class ColumnVisibleServiceImpl implements ColumnVisibleService {

    @Autowired
    ColumnVisibleMapper columnVisibleMapper;

    @Override
    public List<ColumnVisible> queryListByPidAndUserId(long userid, long cid) {
        ColumnVisibleExample cm = new ColumnVisibleExample();
        if (userid == 0) {
            cm.createCriteria().andColumnIdEqualTo(cid);
        } else {
            cm.createCriteria().andUserIdEqualTo(userid).andColumnIdEqualTo(cid);
        }
        List<ColumnVisible> cs = columnVisibleMapper.selectByExample(cm);
        if (cs != null && cs.size() == 1) {
            return cs;
        }
        return null;
    }
    @Override
    public ColumnVisible queryListByCidAndUserId(long userid, long cid) {
        ColumnVisibleExample cm = new ColumnVisibleExample();
        if (userid == 0) {
            cm.createCriteria().andColumnIdEqualTo(cid);
        } else {
            cm.createCriteria().andUserIdEqualTo(userid).andColumnIdEqualTo(cid);
        }
        List<ColumnVisible> cs = columnVisibleMapper.selectByExample(cm);
        if (cs != null && cs.size() == 1) {
            return cs.get(0);
        }
        return null;
    }

    @Override
    public void saveCids(long userid, String cids, long pcid) {
        String[] ids = cids.split(",");
        delByPcid(pcid, userid);
        for (String id : ids) {
            try {
                long c = Long.parseLong(id);
                ColumnVisible cv = new ColumnVisible();
                cv.setColumnId(c);
                cv.setPcid(pcid);
                cv.setUserId(userid);
                cv.setCtime(new Date());
                cv.setUtime(new Date());
                columnVisibleMapper.insert(cv);
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void del(long id) {
        columnVisibleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void delByPcid(long id, long userid) {
        ColumnVisibleExample cm = new ColumnVisibleExample();
        cm.createCriteria().andUserIdEqualTo(userid).andPcidEqualTo(id);
        columnVisibleMapper.deleteByExample(cm);
    }

}
