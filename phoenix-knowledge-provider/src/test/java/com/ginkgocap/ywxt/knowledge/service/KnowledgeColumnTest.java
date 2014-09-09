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
 * @author  <p>当前负责人 bianzhiwei</p>     
 * @since <p>1.2.1-SNAPSHO</p> 
 */

public class KnowledgeColumnTest extends TestBase{
    @Autowired
    private KnowledgeColumnService knowledgeColumnService;
 
    @Test
    public void queryById() throws Exception {
        
       KnowledgeColumn kc= knowledgeColumnService.queryById(1);
       System.out.println(kc.getColumnName());
    }
    
    @Test
    public void  queryByParentId() throws Exception {
        List<KnowledgeColumn> list= knowledgeColumnService.queryByParentId(0, 0);
        
        for (KnowledgeColumn knowledgeColumn : list) {
            System.out.print(knowledgeColumn.getColumnName()+' ');
            
        }
        System.out.println();
        assertTrue(!list.isEmpty());
    }
    
    @Test
    public void saveOrUpdate(){
        
        try {
            
            KnowledgeColumn kc =new KnowledgeColumn();
            Date date=new Date();
            SimpleDateFormat f=new SimpleDateFormat("HH:mm:ss");
            kc.setColumnName("自定义"+f.format(date));
            kc.setParentColumnId(1L);
            kc.setCreateUserId(71L);
            kc.setLevel(2);
            kc.setColumnLevelPath("0-1");
            
            KnowledgeColumn ks = knowledgeColumnService.saveOrUpdate(kc);
            
            System.out.println(ks.getId());
            
            assertEquals(true, ks.getId()>0);
            
            ks=knowledgeColumnService.queryById(ks.getId());
            ks.setColumnName("_test_aaa");
            
            Thread.sleep(1000);
            
            knowledgeColumnService.saveOrUpdate(ks);
            
            assertEquals(true, ks.getId()>0);
            
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
        
       
        
    }
 
}
