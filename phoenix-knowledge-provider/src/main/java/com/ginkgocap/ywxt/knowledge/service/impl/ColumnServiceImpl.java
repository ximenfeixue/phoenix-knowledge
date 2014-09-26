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
import com.ginkgocap.ywxt.knowledge.mapper.ColumnMapper;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnValueMapper;
import com.ginkgocap.ywxt.knowledge.service.ColumnService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.knowledge.util.KCHelper;
import com.ginkgocap.ywxt.knowledge.util.tree.ConvertUtil;
import com.ginkgocap.ywxt.knowledge.util.tree.Tree;

@Service("columnService")
public class ColumnServiceImpl implements ColumnService {

    public static int NO_DEL_VALUE=0;
    public static int ROOT_PARENT_ID=0;
    public static int MAX_ALLOWED_LEVEL=7;
    
    @Autowired
    ColumnMapper columnMapper;
    @Autowired
    private ColumnValueMapper columnValueMapper;
    
    /**
     * 在查询条件中增加delstatus条件，过滤掉已删除对象
     * @param c
     * @return
     */
    public Criteria filterDel(Criteria c){
        c.andDelStatusEqualTo(Byte.valueOf(NO_DEL_VALUE+""));
        return c;
    }
    
    @Override
    public List<Column> selectAncestors(Column c){
        
        List<Column> list=new ArrayList<Column>(MAX_ALLOWED_LEVEL);
        list.add(c);
        
        if (c.getParentId().intValue()==ROOT_PARENT_ID) {
            return list;
        }
        
        Column cs=c;
        while(cs.getParentId().intValue()>ROOT_PARENT_ID) {
            cs=columnMapper.selectByPrimaryKey(cs.getParentId());
            list.add(cs);
        }
        return list;
    }

    @Override
    public Column saveOrUpdate(Column kc) {

        if (null == kc) {
            return null;
        }

        Long id = kc.getId();
        Date date = new Date();

        if (null == id || id.intValue() <= 0) {
            
            if (kc.getParentId() == null||kc.getParentId()<0) {
                return null;
            }
           
            kc.setCreatetime(date);
            kc.setUpdateTime(date);
            kc.setDelStatus((byte)0);
            kc.setSubscribeCount(0l);
            
            //参考UserCategoryServiceImpl.insert 生成columnlevelpath
            //系统栏目的USERID应为金桐脑的id
            
            //插入数据之前必须有columnpath 
            
            List<Column> ancestors=selectAncestors(kc);
            
            List<Long> pids=new ArrayList<Long>();
            for (int i = 0; i < ancestors.size(); i++) {
                pids.add(ancestors.get(i).getParentId());
            }
            
            Long pathId=columnValueMapper.selectMaxID()+1;
            
            String sort=KCHelper.getSortPath(pids, pathId);
            
            kc.setColumnLevelPath(sort);
            
            columnMapper.insert(kc);
            
            ColumnExample ce=new ColumnExample();
            filterDel(ce.createCriteria().andUpdateTimeEqualTo(date).andCreatetimeEqualTo(date));
            kc=columnMapper.selectByExample(ce).get(0);
            
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
    public boolean isExist(long parentColumnId, String columnName) {
        ColumnExample ce=new ColumnExample();
        
        ce.createCriteria().andParentIdEqualTo(parentColumnId).andColumnnameEqualTo(columnName);
        
        return columnMapper.countByExample(ce)>0?true:false;
    }
    
    @Override
    public List<Column> querySubByUserId(long createUserId) {
        List<Column> list=columnValueMapper.selectSubByUserId(createUserId);
        return list;
    }
    
    @Override
    public List<Column> queryByUserIdAndSystem(long createUserId, long systemId) {
        List<Long> values=new ArrayList<Long>();
        values.add(createUserId);
        values.add(systemId);
        
        ColumnExample ce=new ColumnExample();
        Criteria c=ce.createCriteria().andUserIdIn(values);
        filterDel(c);
        
        ce.setOrderByClause("id ASC");
        //TODO 排序
        
        List<Column> list= columnMapper.selectByExample(ce);
        return list;
    }

    @Override
    public void delById(long id) {
        // TODO Auto-generated method stub
    }

    @Override
    public List<Column> queryByParentId(long parentId,long userId) {
        ColumnExample ce=new ColumnExample(); 
        Criteria c=ce.createCriteria().andParentIdEqualTo(parentId).andUserIdEqualTo(userId);
        filterDel(c);
        List<Column> list= columnMapper.selectByExample(ce);
        return list;
    }
    
    @Override
    public List<Map<String, Object>> querySubAndStatus(long userId){
//        
        List<Column> list=this.querySubBySystem(Constants.gtnid);
        
        List<Column> subList=this.querySubByUserId(userId);
        List<Long> subListId=new ArrayList<Long>();
        for (int i = 0; i < subList.size(); i++) {
            subListId.add(subList.get(i).getId());
        }
        
        List<Map<String, Object>> ss=new ArrayList<Map<String, Object>>();
        for (int i = 0; i < list.size(); i++) {
            Long id=list.get(i).getId();
            
            Map<String, Object> map=new HashMap<String, Object>();
            
            map.put("column", list.get(i));
           
            if (subListId.contains(id)) {
                map.put("status", true);
            }else {
                map.put("status", false);
            }
            
            ss.add(map);
        }
        return ss;
    }

    @Override
    public List<Column> queryByUserId(long createUserId) {
        // TODO Auto-generated method stub
        return null;
    }

  

    @Override
    public List<Column> querySubBySystem(long systemId) {
        
        List<Column> list= columnValueMapper.selectSubC(systemId);
        
        return list;
    }

   

    @Override
    public List<Column> queryAll() {
        ColumnExample ce=new ColumnExample();
        Criteria c=ce.createCriteria();
        filterDel(c);
        List<Column> list=columnMapper.selectByExample(ce);
        return list;
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
//  @Override
//  public boolean isExist(int parentColumnId, String columnName) {
//      if (ColumnDao.countByPidAndName(parentColumnId, columnName) > 0) {
//          return true;
//      }
//      return false;
//  }
//
//  @Override
//  public void delById(long id) {
//      ColumnDao.delById(id);
//  }
//
//  @Override
//  public List<Column> queryByParentId(int parentColumnId, int createUserId) {
//      return ColumnDao.queryByParentId(parentColumnId, createUserId);
//  }
//
//  @Override
//  public List<Column> queryByUserId(long createUserId) {
//      return ColumnDao.queryByUserId(createUserId);
//  }
//
//  @Override
//  public List<Column> queryByUserIdAndSystem(long createUserId, long systemId) {
//      return ColumnDao.queryByUserIdAndSystem(createUserId, systemId);
//  }
//
//  @Override
//  public List<Column> queryAll() {
//      return ColumnDao.queryAll();
//  }
//
//  @Override
//  public List<Column> queryAllDel() {
//      return ColumnDao.queryAllDel();
//  }
//
//  @Override
//  public void recoverOneKC(Long id) {
//      if (null == id || id.longValue() < 0) {
//          return;
//      }
//
//      ColumnDao.recoverOneKC(id);
//  }
//
//  @Override
//  public void clearById(long id) {
//      ColumnDao.clearById(id);
//  }
//
//  @Override
//  public List<Column> querySubByUserId(long createUserId) {
//      return ColumnDao.querySubByUserId(createUserId);
//  }

}
