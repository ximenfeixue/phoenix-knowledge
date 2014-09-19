package com.ginkgocap.ywxt.knowledge.service;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.Article;
import com.ginkgocap.ywxt.knowledge.model.Category;
import com.ginkgocap.ywxt.knowledge.service.knowledge.ArticleService;
import com.ginkgocap.ywxt.knowledge.service.knowledge.CategoryService;
import com.ginkgocap.ywxt.knowledge.util.Content;
import com.ginkgocap.ywxt.knowledge.util.process.ExportWatched;
import com.ginkgocap.ywxt.knowledge.util.process.ImportWatched;
import com.ginkgocap.ywxt.util.DateFunc;
import com.ginkgocap.ywxt.util.PageUtil;

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
        article.setArticleContent("http://www.baidu.com");
        article.setArticleTitle("title");
        article.setArticleType("1");
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
        articlechild.setArticleType("1");
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
//    	String downPath = articleService.exportArticleById(article.getId());
//    	assertTrue(!"".equals(downPath));
//    	System.out.println(downPath);
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
    	String osName = System.getProperty("os.name");
    	String home = "";
		if (Pattern.matches("Windows.*", osName)) {
			home= "C:/Program Files (x86)/OpenOffice.org 3";
		}
			home = "/opt/openoffice.org3";
		File file = new File(home);
		if (file.exists() && file.isDirectory())
	    	for (int i = 1; i <=3; i ++){
	    		Map<String,Object>map = articleService.exportFileBySortId(uid,category.getSortId(),"taskId", "", "",i + "");
	    		String f = (String)map.get("result");
	    		assertTrue("success".equals(f));
	    	}
		else
			assertTrue(!file.exists());
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
    
    
    @Test
    public void testProcessView(){
//    	String taskId="aJJLIEowiejioj2930kj5k2092-i=230349";
//    	ExportWatched watched = new ExportWatched();
//		watched.setTaskId(taskId);
//		watched.setTotal(10);
//		Content.MAP.put(taskId, watched);
//		Map<String, Object> map = articleService.processView(taskId);
//		watched.setDone(true);
//		map = articleService.processView(taskId);
//		ExportWatched watched1 = (ExportWatched) Content.MAP.get(taskId);
//		assertTrue(watched1 == null);
    }
    
    @Test
    public void testImportProcess(){
//    	String taskId="aJJLIEowiejioj2930kj5k2092-i=230340";
//    	ImportWatched watched = new ImportWatched();
//    	watched.setTaskId(taskId);
//		watched.setTotal(10);
//		Content.MAP.put(taskId, watched);
//		String a = articleService.importProcess(taskId);
//		System.out.println(a);
//		watched.setDone(true);
//		a = articleService.importProcess(taskId);
//		System.out.println(a);
//		assertTrue("done".equals(a));
    }
    
    @Test
    public void testCount(){
    	PageUtil p = articleService.count(uid, article.getSortId(), article.getEssence(), article.getRecycleBin(), "title", 1, 10);
    	System.out.println(p.getCount());
    	assertTrue(p.getCount() >0);
    }
    
    @Test
    public void testUpdateEssence(){
    	articleService.updateEssence("0", new String[]{article.getId()+""});
    	System.out.println(article.getEssence());
    	assertTrue("0".equals(article.getEssence()));
    }
    @Test
    public void testUpdateRecycleBin(){
    	articleService.updateRecycleBin("0", new String[]{article.getId()+""});
    	System.out.println(article.getRecycleBin());
    	assertTrue("0".equals(article.getRecycleBin()));
    } 
    
    @Test
    public void testCleanRecyle(){
    	articleService.cleanRecyle(uid);
    	long i = articleService.selectRecyleNum(uid);
    	assertTrue(i==0);
    }
    @Test
    public void testRelationList(){
    	List<Article> list = articleService.relationList(uid, article.getId(), article.getSortId(), 1, 10);
    	System.out.println(list.size());
    	assertTrue(list.size() > 0);
    }
    @Test
    public void testNewCount(){
    	PageUtil p = articleService.count(uid, article.getArticleType(),article.getSortId(), article.getEssence(), article.getRecycleBin(), "title", 1, 10);
    	System.out.println(p.getCount());
    	assertTrue(p.getCount() >0);
    }
    @Test
    public void testNewList(){
    	List<Article> list = articleService.list(uid,article.getArticleType(),category.getSortId(), "0", "0", "title", "id", 1, 10);
    	assertTrue(list.size() > 0);
    	for (Article a: list){
    		System.out.println(a.getId() + "    " + a.getClickNum() + "    "  + a.getArticleTitle());
    	}
    }
 }
