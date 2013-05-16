package com.ginkgocap.ywxt.knowledge.service;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.form.DataGridModel;
import com.ginkgocap.ywxt.knowledge.model.Article;
import com.ginkgocap.ywxt.knowledge.service.article.ArticleService;


/**
 * 文章Service的测试用例
 * @author lk
 * @创建时间：2013-03-29 10:24:22
 */

public class ArticleServiceTest extends TestBase{
    @Autowired
    private ArticleService articleService;
    
    private long uid = 12423041;
    
    private Article article;
    
    private DataGridModel dgm =null;

    @Before
    public void setUp() throws Exception {
    	dgm = new DataGridModel();
    	dgm.setPage(1);
    	dgm.setRows(10);
    }
    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testExportArticleById(){
//    	System.out.println(articleService.exportArticleById(5));
    }
    @Test
    public void testArticleAllListBySortId(){
    	List<Article> list = articleService.articleAllListBySortId(12423041, "", "", "");
    	System.out.println("articleList " + list.size());
    	for(Article article:list){
    		System.out.println(article.getIsNew() + " " + article.getId());
    		
    	}
    }
    @Test
    public void testExportFileBySortId(){
    	Map<String,Object>map = articleService.exportFileBySortId(12423041, "","taskId", "", "","3");
    }
    @Test
    public void testTask_ID(){
    	Article article = articleService.selectByPrimaryKey(1);
    	System.out.println(article.getTaskId());
    	System.out.println(article.getPubdate());
    	System.out.println(article.getModifyTime());
    }
    @Test
    public void testDeleteArticles(){
//    	articleService.deleteArticles(new String[]{"1","2"});
    	Article a = articleService.selectByPrimaryKey(1);
    	System.out.println(a == null);
    }
    @Test
    public void testReNum(){
    	long a = articleService.selectRecyleNum(10000);
    	System.out.println(a);
    }
    @Test
    public void testList(){
    	List<Article> list = articleService.list(uid, "", "", "", "", "id", 1, 10);
    	for (Article a: list){
    		System.out.println(a.getId() + "    " + a.getClickNum() + "    "  + a.getArticleTitle());
    	}
    }
 }
