package com.ginkgocap.ywxt.knowledge.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.dao.article.ArticleDao;

	/**
	 * 类型的测试
	 * @author lk
	 * 创建时间：2013-3-29 10:37:50
	 */
	public class ArticleDaoTest  extends TestBase{
	    @Autowired
	    private ArticleDao articleDao;

	    /**
	     * @throws java.lang.Exception
	     */
	    @Before
	    public void setUp() throws Exception {
	        
	    }

	    /**
	     * @throws java.lang.Exception
	     */
	    @After
	    public void tearDown() throws Exception {
	    }
	    
	    @Test
	    public void select(){
	        articleDao.articleAllListBySortId(10522551, "1599", "", "");  
	    }
//	    @Test
//	    public void testSelectCountByCategoryId(){
//	    	long count = articleDao.selectCountByCategoryId(3);
//	    	System.out.println(count);
//	    }
//	    @Test 
//	    public void testUpdateEssence(){
//	    	articleDao.updateEssence("1", new String[]{"1"});
//	    	Article article = articleDao.selectByPrimaryKey(1);
//	    	System.out.println("ID为1的文章精华状态:" + article.getEssence());
//	    }
//	    @Test 
//	    public void testUpdateRecycleBin(){
//	    	articleDao.updateRecycleBin("1", new String[]{"1","2"});
//	    	Article article = articleDao.selectByPrimaryKey(1);
//	    	System.out.println("ID为1的文章回收状态:" + article.getRecycleBin());
//	    }
////	    @Test
////	    public void testArticlelist(){
////	    	List<Article> list = articleDao.selectArticleList(62, "", "", "", 1, 10);
////	    	System.out.println(list.size());
////	    }
//	    @Test
//	    public void testlist(){
//	    	List<Article> list = articleDao.selectByParams(12423041, "", "", "", "", "id", 1, 5);
//	    	for (Article a:list){
//	    		System.out.println(a.getId() + "   " + a.getArticleTitle() + " " + a.getSortId() + "  clickNum:" + a.getClickNum());
//	    	}
//	    	
//	    }
////	    @Test
////	    public void testcount(){
////	    	List<Article> list = articleDao.selectArticleList(62, "", "", "", 1, 10);
////	    	System.out.println(list.size());
////	    }
	}
