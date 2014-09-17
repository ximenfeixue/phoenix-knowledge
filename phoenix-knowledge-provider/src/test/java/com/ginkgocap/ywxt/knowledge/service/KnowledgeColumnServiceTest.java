package com.ginkgocap.ywxt.knowledge.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.Category;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeColumn;
import com.ginkgocap.ywxt.knowledge.service.category.CategoryService;
import com.ginkgocap.ywxt.util.DateFunc;


/** 
 * <p>知识栏目操作测试接口</p>  
 * <p>于2014-8-19 由 bianzhiwei 创建 </p>
 * @author  <p>当前负责人 guangyuan</p>     
 * @since <p>1.2.1-SNAPSHOT</p> 
 */

public class KnowledgeColumnServiceTest extends TestBase{
    @Autowired
    private KnowledgeColumnService knowledgeColumnService;
 
    @Test
    public void queryById() throws Exception {
        
       KnowledgeColumn kc= knowledgeColumnService.queryById(1);
       System.out.println(kc.getColumnName());
    }
    
    @Test
    public void selectColumnTreeBySortId(){
        knowledgeColumnService.selectColumnTreeBySortId(1, null, "0");
    }
    
    @Test
    public void  queryByParentId() {
        
        try {
            List<KnowledgeColumn> list= knowledgeColumnService.queryByParentId(1, 71);
            
            for (KnowledgeColumn knowledgeColumn : list) {
                System.out.print(knowledgeColumn.getColumnName()+' ');
                
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
        List<KnowledgeColumn> list=null;
        try {
            list= knowledgeColumnService.queryByUserIdAndSystem(71, 0);
            
            for (int i=0;i<list.size();i++) {
                KnowledgeColumn knowledgeColumn=list.get(i);
                System.out.print(knowledgeColumn.getColumnName()+' ');
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
        List<KnowledgeColumn>l =knowledgeColumnService.selectFullPath(42);
        for(KnowledgeColumn k :l){
            System.out.println(k.getId());
        }
    }
        
    @Test
    public void saveOrUpdate(){
        
        Long clearId = new Long(-1);
        
        try {
            
            //==========add==========//
            
            KnowledgeColumn kc =new KnowledgeColumn();
            Date date=new Date();
            SimpleDateFormat f=new SimpleDateFormat("HH:mm:ss");
            kc.setColumnName("_自定义"+f.format(date));
            kc.setParentColumnId(1L);
            kc.setCreateUserId(71L);
            kc.setLevel(2);
            kc.setColumnLevelPath("0-1");
          //  kc.setPathName("资讯->自定义");
            
            KnowledgeColumn ks = knowledgeColumnService.saveOrUpdate(kc);
            
            System.out.println(clearId=ks.getId());
            System.out.println(ks.getColumnName());
            
            assertEquals(true, ks.getId()>0);
            
            //==========update==========//
            
            ks=knowledgeColumnService.queryById(ks.getId());
            ks.setColumnName("_test_aaa");
            
            Thread.sleep(1000);
            
            knowledgeColumnService.saveOrUpdate(ks);
            
            KnowledgeColumn ku=knowledgeColumnService.queryById(ks.getId());
            
            assertEquals(ks.getColumnName(), ku.getColumnName());
            System.out.println(ku.getColumnName());
            
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }finally{
            try {
                if (clearId>0) {
                    knowledgeColumnService.delById(clearId);
                    knowledgeColumnService.clearById(clearId);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                Assert.fail();
            }
           
        }
    }
    
    @Test
    public void querySubByUserId(){
        List<KnowledgeColumn> list=knowledgeColumnService.querySubByUserId(72l);
        for (KnowledgeColumn kc : list) {
            System.out.println(kc.getColumnName());
        }
    }
    
}
