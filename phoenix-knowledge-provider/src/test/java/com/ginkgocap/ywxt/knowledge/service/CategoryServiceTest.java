package com.ginkgocap.ywxt.knowledge.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.Category;
import com.ginkgocap.ywxt.knowledge.service.category.CategoryService;
import com.ginkgocap.ywxt.util.DateFunc;


/**
 * 地域Service的测试用例
 * @author lk
 * @创建时间：2013-03-29 10:24:22
 */

public class CategoryServiceTest extends TestBase{
    @Autowired
    private CategoryService categoryService;
    
    private long uid = 10000;
    
    private Category category;
    
    private Category child;
    @Before
    public void setUp() throws Exception {
      category = new Category();
      category.setParentId(0);
      category.setState("0");
      category.setSubtime(DateFunc.getDate());
      category.setModtime(DateFunc.getDate());
      category.setName("测试分类");
      category.setUid(uid);
      category = categoryService.insert(category);
      child = new Category();
      child.setParentId(category.getId());
      child.setState("0");
      child.setSubtime(DateFunc.getDate());
      child.setModtime(DateFunc.getDate());
      child.setName("测试子类");
      child.setUid(uid);
      categoryService.insert(child);
      Map<String,Object> map = new HashMap<String, Object>();
      map.put("name", "试分类");
      map.put("uid", uid);
      map.put("sortId", category.getSortId());
      map.put("state", "0");
     List<Category> list = categoryService.findByParam(map);
     System.out.println(list.size());
    }
    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    	categoryService.delete(child.getId());
    	categoryService.delete(category.getId());
    }

    @Test
    public void testSelectByPrimaryKey() {
    	Category cat = categoryService.selectByPrimaryKey(category.getId());
    	System.out.println(cat.getId());
        assertEquals(cat.getSortId(),category.getSortId());
    }
    
    @Test
    public void testSelectTreeOfSortByUserid(){
    	List<Category> list = categoryService.selectTreeOfSortByUserid(category.getUid(),category.getState());
    	assertTrue(list.size() > 0);
    	for(Category cat:list){
    		System.out.println("name: " + cat.getName() + " sortId:" + cat.getSortId());
    	}
    }
    @Test
    public void testSelectBySortId(){
    	Category cat = categoryService.selectBySortId(uid,category.getSortId());
    	assertTrue(cat != null);
    	System.out.println(cat.getName() + "  " + cat.getUid());
    }


    
    @Test
    public void testSelectCategoryPathBySortId(){
    	Category[] cats = categoryService.selectCategoryPathBySortId(child.getUid(), child.getSortId());
    	assertTrue(cats.length > 0);
    	for (Category cat: cats){
    		System.out.println(cat.getName());
    	}
    }
    
    @Test
    public void testSelectChildBySortId(){
    	List<Category>list = categoryService.selectChildBySortId(category.getUid(), "");
    	assertTrue(list.size() > 0);
    	for (Category c: list){
    		System.out.println(c.getName() + "   " + c.getSortId());
    	}
    }
    @Test
    public void testUpdate(){
    	category.setName("修改测试分类");
    	categoryService.update(category);
    	assertTrue("修改测试分类".equals(category.getName()));
    	System.out.println(category.getName());
    }
}
