package com.ginkgocap.ywxt.knowledge.service;

import static org.junit.Assert.assertEquals;


import java.util.List;

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
    
    private String state = "0";
    
    private Category category;
    @Before
    public void setUp() throws Exception {
      category = new Category();
      category.setId(1);
      category.setParentId(0);
      category.setState("0");
      category.setSubtime(DateFunc.getDate());
      category.setSortId("000000001");
      category.setName("测试分类");
    }
    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSelectByPrimaryKey() {
        assertEquals(categoryService.selectByPrimaryKey(1).getName(),category.getName());
    }
    
    @Test
    public void testSelectTreeOfSortByUserid(){
    	List<Category> list = categoryService.selectTreeOfSortByUserid(uid,state);
    	for(Category cat:list){
    		System.out.println("name:" + cat.getName() + " sortId:" + cat.getSortId());
    	}
    }
    @Test
    public void testSelectBySortId(){
    	Category cat = categoryService.selectBySortId(uid,"000000001000000001");
    	System.out.println(cat.getName() + "  " + cat.getUid());
    }
    @Test
    public void testInsert(){
    	Category cat = new Category();
    	cat.setName("junit测试新增");
    	cat.setParentId(1);
    	cat.setState("0");
    	cat.setSubtime(DateFunc.getDate());
    	cat.setUid(uid);
    	Category newCat = categoryService.insert(cat);
    	System.out.println(newCat.getSortId());
    	System.out.println(newCat.getId());
    }

    
    @Test
    public void testSelectCategoryPathBySortId(){
    	Category[] cats = categoryService.selectCategoryPathBySortId(62, "000000001000000001");
    	for (Category cat: cats){
    		System.out.println(cat.getName());
    	}
    }
    
    @Test
    public void testSelectChildBySortId(){
    	List<Category>list = categoryService.selectChildBySortId(62, "");
    	for (Category c: list){
    		System.out.println(c.getName() + "   " + c.getSortId());
    	}
    }
}
