package com.ginkgocap.ywxt.knowledge.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.entity.Column;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnValueMapper;
import com.ginkgocap.ywxt.knowledge.model.Category;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeColumn;
import com.ginkgocap.ywxt.knowledge.service.CategoryService;
import com.ginkgocap.ywxt.knowledge.service.ColumnService;
import com.ginkgocap.ywxt.knowledge.util.KCHelper;
import com.ginkgocap.ywxt.util.DateFunc;


/** 
 * <p>知识栏目操作测试接口</p>  
 * <p>于2014-8-19 由 bianzhiwei 创建 </p>
 * @author  <p>当前负责人 guangyuan</p>     
 * @since <p>1.2.1-SNAPSHOT</p> 
 */

public class ColumnServiceTest extends TestBase{
    @Autowired
    private ColumnService kcs;
    
    @Resource ColumnValueMapper cvm;
    
//    @Resource(name="columnService") KnowledgeColumnService cs;
 
    @Test
    public void queryById() throws Exception {
        
       Column kc= kcs.queryById(1);
       System.out.println(kc.getColumnname());
    }
    
    @Test
    public void selectColumnTreeBySortId(){
        kcs.selectColumnTreeBySortId(1, null, "0");
    }
    
    @Test
    public void  queryByParentId() {
        
        try {
            List<Column> list= kcs.queryByParentId(1, 71);
            
            for (Column knowledgeColumn : list) {
                System.out.print(knowledgeColumn.getColumnname()+' ');
                
            }
            System.out.println();
            assertTrue(!list.isEmpty());
        
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }finally{
            System.gc();
        }
    }
    
    @Test
    public void queryByUserIdAndSystem(){
        List<Column> list=null;
        try {
            list= kcs.queryByUserIdAndSystem(71, 0);
            
            for (int i=0;i<list.size();i++) {
                Column knowledgeColumn=list.get(i);
                System.out.print(knowledgeColumn.getColumnname()+' ');
                System.out.print(knowledgeColumn.getColumnLevelPath()+' ');
                if ((i+1)%13==0) {
                    System.out.println();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
        
        System.out.println();
        assertTrue(!list.isEmpty());
    }
    
    @Test
    public void selectFullPath(){
        List<Column>l =kcs.selectFullPath(42);
        for(Column k :l){
            System.out.println(k.getId());
        }
    }
        
    @Test
    public void saveOrUpdate(){
        
        Long clearId = new Long(-1);
        
        try {
            
            //==========add==========//
            
            Column kc =new Column();
            Date date=new Date();
            SimpleDateFormat f=new SimpleDateFormat("HH:mm:ss");
            kc.setColumnname("_自定义"+f.format(date));
            kc.setParentId(1L);
            kc.setUserId(71L);
//            kc.setLevel(2);
//            kc.setColumnLevelPath("0-1");
          //  kc.setPathName("资讯->自定义");
            
            Column ks = kcs.saveOrUpdate(kc);
            
            System.out.println(clearId=ks.getId());
            System.out.println(ks.getColumnname());
            
            assertEquals(true, ks.getId()>0);
            
            //==========update==========//
            
//            ks=kcs.queryById(ks.getId());
//            ks.setColumnname("_test_aaa");
//            
//            Thread.sleep(1000);
//            
//            kcs.saveOrUpdate(ks);
//            
//            Column ku=kcs.queryById(ks.getId());
//            
//            assertEquals(ks.getColumnname(), ku.getColumnname());
//            System.out.println(ku.getColumnname());
            
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }finally{
            try {
                if (clearId>0) {
//                    kcs.delById(clearId);
//                    kcs.clearById(clearId);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                Assert.fail();
            }
           
        }
    }
    
    @Test
    public void save(){
        Long clearId = new Long(-1);
        
        try {
            
            Column kc =new Column();
            Date date=new Date();
            SimpleDateFormat f=new SimpleDateFormat("HH:mm:ss");
            
            kc.setColumnname("_自定义"+f.format(date));
            kc.setParentId(0L);
//            kc.setParentId(12L);
//            kc.setParentId(595L);
            kc.setUserId(71L);
            kc.setPathName("资讯->自定义");
            
            Column ks = kcs.saveOrUpdate(kc);
            
            System.out.println(clearId=ks.getId());
            System.out.println(ks.getColumnname());
            
            assertEquals(true, ks.getId()>0);
            
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }finally{
            try {
                if (clearId>0) {
//                    kcs.delById(clearId);
//                    kcs.clearById(clearId);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                Assert.fail();
            }
           
        }
    }
    
    @Test
    public void querySubByUserId(){
        try {
            List<Column> list=kcs.querySubByUserId(72l);
            for (Column kc : list) {
                System.out.println(kc.getColumnname());
            }
        
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }finally{
            System.gc();
        }
       
    }
    
    @Test
    public void isExist(){
        try {
            boolean b=kcs.isExist(1, "股票");
            assertTrue(b);
        
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }finally{
            System.gc();
        }
       
    }
    
    @Test
    public void queryAll(){
        
        try {
            List<Column> cList=kcs.queryAll();
            System.out.println(cList.size());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
        
    }
    
    @Test
    public void querySubAndStatus(){
        try {
            List<Map<String, Object>> llist=kcs.querySubAndStatus(71l);
            
            for (int i = 0; i < llist.size(); i++) {
                Column c=(Column) llist.get(i).get("column");
                System.out.print(c.getId()+"  ");
                System.out.println(llist.get(i).get("status"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
        
    }
    
    @Test
    public void updateColumnlevelpath(){
        boolean b=true;
        try {
            if (b) {
                return;
            }
           
//            Long pathId=cvm.selectMaxID()+1;
            Long pathId=1l;
            List<Column> cList=kcs.queryAll();
            
            for (int j = 0; j < cList.size(); j++) {
                Long id=cList.get(j).getId();
                Column c=kcs.queryById(id);
                List<Column> ancestors=kcs.selectAncestors(c);
                
                List<Long> pids=new ArrayList<Long>();
                for (int i = 0; i < ancestors.size(); i++) {
                    pids.add(ancestors.get(i).getParentId());
                }
                
                ++pathId;
                
                String sort=KCHelper.getSortPath(pids, pathId);
                
                System.out.println(sort);
                
                Map<String, Object> map=new HashMap<String, Object>();
                map.put("id", id);
                map.put("columnLevelPath", sort);
                
                cvm.updateCLP(map);
            }
            
            
        
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }finally{
            System.gc();
        }
       
    }
    
}
