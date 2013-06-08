package com.ginkgocap.ywxt.knowledge.service;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.Article;
import com.ginkgocap.ywxt.knowledge.model.Category;
import com.ginkgocap.ywxt.knowledge.service.article.ArticleService;
import com.ginkgocap.ywxt.knowledge.service.category.CategoryService;
import com.ginkgocap.ywxt.util.DateFunc;

/**
 * 文章Service的测试用例
 * @author lk
 * @创建时间：2013-03-29 10:24:22
 */

public class ArticleServiceTest extends TestBase{
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CategoryService categoryService;
    
    private long uid = 10000;
    
    private Article article;
    
    private Article articlechild;
    
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
        categoryService.insert(category);
        child = new Category();
        child.setParentId(category.getId());
        child.setState("0");
        child.setSubtime(DateFunc.getDate());
        child.setModtime(DateFunc.getDate());
        child.setName("测试子类");
        child.setUid(uid);
        categoryService.insert(child);
        
        
        article = new Article();
        article.setArticleContent("content");
        article.setArticleTitle("title");
        article.setAuthor("lk");
        article.setCategoryid(category.getId());
        article.setClickNum(0);
        article.setEssence("0");
        article.setModifyTime(DateFunc.getDate());
        article.setPubdate(DateFunc.getDate());
        article.setRecycleBin("0");
        article.setSortId(category.getSortId());
        article.setSource("source");
        article.setState("0");
        article.setUid(uid);
        articleService.insert(article);
        
        
        articlechild = new Article();
        articlechild.setArticleContent("content内容");
        articlechild.setArticleTitle("title标题");
        articlechild.setAuthor("lk");
        articlechild.setCategoryid(child.getId());
        articlechild.setClickNum(0);
        articlechild.setEssence("0");
        articlechild.setModifyTime(DateFunc.getDate());
        articlechild.setPubdate(DateFunc.getDate());
        articlechild.setRecycleBin("0");
        articlechild.setSortId(child.getSortId());
        articlechild.setSource("source");
        articlechild.setState("0");
        articlechild.setUid(uid);
        articleService.insert(articlechild);
        
    }
    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    	Article a1 = articleService.selectByPrimaryKey(article.getId());
    	Article a2 = articleService.selectByPrimaryKey(articlechild.getId());
    	long a1id = a1.getId();
    	long a2id = a2.getId();
    	String[] ids = new String[]{a1id + "",a2id + ""};
    	articleService.deleteArticles(ids);
    	categoryService.delete(child.getId());
    	categoryService.delete(category.getId());
    }

    @Test
    public void testExportArticleById(){
    	String downPath = articleService.exportArticleById(article.getId());
    	assertTrue(!"".equals(downPath));
    	System.out.println(downPath);
    }
    @Test
    public void testArticleAllListBySortId(){
    	List<Article> list = articleService.articleAllListBySortId(uid, category.getSortId(), "0", "0");
    	System.out.println("articleList " + list.size());
    	assertTrue(list.size() > 0);
    	for(Article article:list){
    		System.out.println(article.getIsNew() + " " + article.getId());
    		
    	}
    }
    @Test
    public void testExportFileBySortId(){
    	for (int i = 1; i <=3; i ++){
    		Map<String,Object>map = articleService.exportFileBySortId(uid,category.getSortId(),"taskId", "", "",i + "");
    		String f = (String)map.get("result");
    		assertTrue("success".equals(f));
    	}
    }
    @Test
    public void testSelectByPrimaryKey(){
    	Article article = articleService.selectByPrimaryKey(articlechild.getId());
    	System.out.println(article.getPubdate());
    	System.out.println(article.getModifyTime());
    	System.out.println(article.getArticleTitle());
    	assertTrue(article != null);
    }
    @Test
    public void testReNum(){
    	long before = articleService.selectRecyleNum(uid);
    	Article r = articleService.selectByPrimaryKey(article.getId());
    	r.setRecycleBin("1");
    	articleService.update(r);
    	long after = articleService.selectRecyleNum(uid);
    	long c = after - before;
    	assertTrue(c == 1);
    	System.out.println(c);
    }
    @Test
    public void testList(){
    	List<Article> list = articleService.list(uid,category.getSortId(), "0", "0", "title", "id", 1, 10);
    	assertTrue(list.size() > 0);
    	for (Article a: list){
    		System.out.println(a.getId() + "    " + a.getClickNum() + "    "  + a.getArticleTitle());
    	}
    }
 }
