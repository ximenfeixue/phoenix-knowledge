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
import com.ginkgocap.ywxt.util.PageUtil;


/**
 * 文章Service的测试用例
 * @author lk
 * @创建时间：2013-03-29 10:24:22
 */

public class ArticleServiceTest extends TestBase{
    @Autowired
    private ArticleService articleService;
    
    private long uid = 62;
    
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
    public void testList(){
    	List<Article> list = (List<Article>)articleService.list(62, "", 1, 10);
    	System.out.println("testList " + list.size());
    }
    @Test
    public void testCount(){
    	PageUtil p = articleService.count(62, "",1, 10);
    	System.out.println("testCount " + p.getCount());
    }
//    @Test
//    public void testExportArticleById(){
//    	System.out.println(articleService.exportArticleById(5));
//    }
    @Test
    public void testArticleCount(){
    	PageUtil p = articleService.articleCount(62, "", "", "", 1, 10);
    	System.out.println("testArticleCount " + p.getCount());
    }
    @Test
    public void testArticlelist(){
    	List<Article> list = articleService.articlelist(62, "", "", "", 1, 10);
    	System.out.println("articleList " + list.size());
    }
    @Test
    public void testArticleAllListBySortId(){
    	List<Article> list = articleService.articleAllListBySortId(62, "000000002", "", "");
    	System.out.println("articleList " + list.size());
    }
    @Test
    public void testExportFileBySortId(){
    	Map<String,Object>map = articleService.exportFileBySortId(uid, "000000002", "", "");
    }
    @Test
    public void testTask_ID(){
    	Article article = articleService.selectByPrimaryKey(1);
    	System.out.println(article.getTaskId());
    }
 }