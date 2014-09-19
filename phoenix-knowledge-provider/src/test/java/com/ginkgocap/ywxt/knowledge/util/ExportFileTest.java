package com.ginkgocap.ywxt.knowledge.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.Article;
import com.ginkgocap.ywxt.knowledge.service.knowledge.ArticleService;
import com.lowagie.text.DocumentException;

public class ExportFileTest  extends TestBase {

    @Autowired
    private ArticleService articleService;
    @Before
    public void setUp() throws Exception {
    }
    @After
    public void tearDown() throws Exception {
    }
    
    
    
    @Test
    public void testExport() {
    	Article article = articleService.selectByPrimaryKey(1);
    	System.out.println(article == null);
    	if (article != null){
	    	String outpath = "D:" + File.separator + "workspace-sts-3.2.0.RELEASE" + File.separator + "test" + File.separator + article.getArticleTitle() + ".doc";
	    	try {
				ExportFile.exportWordFile(article, outpath, new HashMap());
				System.out.println(ImportFile.getTextFromRtf(outpath));
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
}
