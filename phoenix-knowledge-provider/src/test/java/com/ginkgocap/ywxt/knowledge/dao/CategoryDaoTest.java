package com.ginkgocap.ywxt.knowledge.dao;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.dao.category.CategoryDao;
import com.ginkgocap.ywxt.knowledge.model.Category;
/**
 * 类型的测试
 * @author lk
 * 创建时间：2013-3-29 10:37:50
 */
public class CategoryDaoTest  extends TestBase{
    @Autowired
    private CategoryDao categoryDao;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
//        codeOldName = "测试代码1";
//        codeNewName = "测试代码2";
//        code = new Code();
//        code.setName(codeOldName);
//        code.setType("TZXQ");
//        code.setLevel(1);
//        code.setCtime("2012-3-5 10:58:04");
        
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    @Rollback(false)
    public void testFormat() {

    }
    @Test
    public void testSelectChildCountById(){
    	long count = categoryDao.selectChildCountById(1);
    	System.out.println(count);
    }
    @Test
    public void testSelectMaxSortId(){
    	String maxsort = categoryDao.selectMaxSortId(62, "000000001");
    	System.out.println(maxsort);
    }
    @Test
    public void testSelectTreeOfSortByUserid(){
    	List<Category> categories = categoryDao.selectTreeOfSortByUserid(62, "");
    	for(Category category:categories){
    		System.out.println(category.getSortId() + "    " + category.getCategoryName());
    	}
    }
}
