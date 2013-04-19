package com.ginkgocap.ywxt.knowledge.service.article.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.URL;
import com.ginkgocap.ywxt.knowledge.dao.article.ArticleDao;
import com.ginkgocap.ywxt.knowledge.dao.category.CategoryDao;
import com.ginkgocap.ywxt.knowledge.form.DataGridModel;
import com.ginkgocap.ywxt.knowledge.model.Article;
import com.ginkgocap.ywxt.knowledge.model.Category;
import com.ginkgocap.ywxt.knowledge.service.article.ArticleService;
import com.ginkgocap.ywxt.knowledge.util.OpenOfficeConvert;
import com.ginkgocap.ywxt.knowledge.util.gen.GenFile;
import com.ginkgocap.ywxt.knowledge.util.gen.GenHTML;
import com.ginkgocap.ywxt.knowledge.util.zip.ZipUtil;
import com.ginkgocap.ywxt.util.PageUtil;

@Service("articleService")
public class ArticleServiceImpl implements ArticleService{
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private CategoryDao categoryDao;
	@Override
	public Article selectByPrimaryKey(long id) {
		return articleDao.selectByPrimaryKey(id);
	}

	@Override
	public Article insert(Article article) {
		return articleDao.insert(article);
	}

	@Override
	public void delete(long id) {
		articleDao.delete(id);
	}

	@Override
	public void update(Article article) {
		articleDao.update(article);
	}

	@Override
	public void updateEssence(String essence, String[] ids) {
		articleDao.updateEssence(essence, ids);
	}

	@Override
	public void updateRecycleBin(String recycleBin, String[] ids) {
		articleDao.updateRecycleBin(recycleBin, ids);
	}

	
	private String genDocFile(Article article,String path,OpenOfficeConvert oc){
		String flag = "";
		//******************************************************************生成HTML的文件名称
		String htmlFileName = article.getId() + "_" + article.getArticleTitle() + ".html";
		//拼接HTML模板
		StringBuffer HTML = new StringBuffer("");
		HTML.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">");
		HTML.append("<HTML><HEAD>");
		HTML.append("<META HTTP-EQUIV=\"CONTENT-TYPE\"");
		HTML.append(" CONTENT=\"text/html; charset=utf-8\"><BODY>");
		HTML.append("文章标题:" + article.getArticleTitle());
		HTML.append("<HR><BR/>");
		HTML.append(article.getArticleContent());
		HTML.append("</BODY></HTML>");
		//html生成对象
		GenFile gf = new GenHTML();
		//生成HTML并返回HTML文件对象
		File html = gf.genFile(HTML.toString(), path + "/", htmlFileName);
		String wordFileName = article.getId() + "_" + article.getArticleTitle() + ".doc";
		oc.htmlToWord(html, path + "/" + wordFileName);
		html.delete();
		return flag;
	}
	
	
	@Override
	public String exportArticleById(long id) {
		String sharePath = "D:/workspace-sts-3.2.0.RELEASE/phoenix-knowledge-web/src/main/webapp/GENFILE/TEMP/";
		//**************************先从数据库获取文章信息*************************
		Article article = articleDao.selectByPrimaryKey(id);
		//*************************先通过取到的文章信息生成HTML文件*************************
		//生成HTML的路径
		String htmlPath = sharePath + "UID_62/" + article.getId() + "/" + article.getSortId() + "/" + "HTML/";
		//生成HTML的文件名称
		String htmlFileName = id + "_" + article.getArticleTitle() + ".html";
		//拼接HTML模板
		StringBuffer HTML = new StringBuffer("");
		HTML.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">");
		HTML.append("<HTML><HEAD>");
		HTML.append("<META HTTP-EQUIV=\"CONTENT-TYPE\"");
		HTML.append(" CONTENT=\"text/html; charset=utf-8\"><BODY>");
		HTML.append("文章标题:" + article.getArticleTitle());
		HTML.append("<HR><BR/>");
		HTML.append(article.getArticleContent());
		HTML.append("</BODY></HTML>");
		//html生成对象
		GenFile gf = new GenHTML();
		//生成HTML并返回HTML文件对象
		File html = gf.genFile(HTML.toString(), htmlPath, htmlFileName);
		//*************************将生成的HTML文件转换成word文档*************************
		OpenOfficeConvert oc = new OpenOfficeConvert();
		oc.getOfficeManager().start();
		String wordPath = sharePath + "UID_62/" + article.getId() + "/" + article.getSortId() + "/" + "WORD/";
		String wordFileName = id + "_" + article.getArticleTitle() + ".doc";
		oc.htmlToWord(html, wordPath + wordFileName);
		oc.getOfficeManager().stop();
		//*************************若成功生成word，将返回word路径*************************
		return "/GENFILE/TEMP/" + "UID_62/" + article.getId() + "/" + article.getSortId() + "/" + "WORD/" + URL.encode(wordFileName);
	}


	@Override
	public List<Article> articleAllListBySortId(long uid, String sortId,
			String recycleBin, String essence) {
		List<Article> articles = articleDao.articleAllListBySortId(uid, sortId, recycleBin, essence);
		return articles;
	}

	private Map<String ,Object> proMap;
	
	@Override
	public Map<String,Object> exportFileBySortId(long uid, String sortId,String recycleBin,String essence) {
		Map<String, Object> map = new HashMap<String, Object>();
		String nowDate = new Date().getTime() + "";
		String sharePath = "D:/workspace-sts-3.2.0.RELEASE/phoenix-knowledge-web/src/main/webapp/GENFILE/TEMP/" + uid + "/genfile/" + nowDate;
		String zipPath = "D:/workspace-sts-3.2.0.RELEASE/phoenix-knowledge-web/src/main/webapp/GENFILE/TEMP/" + uid + "/" + nowDate;
		String download = "/GENFILE/TEMP/" + uid + "/" + nowDate;
		List<Article> list = articleDao.articleAllListBySortId(uid, sortId, recycleBin,essence);
		List exportArticleList = new ArrayList();
		List errorArticleList = new ArrayList();
		proMap = new HashMap();
		if (list != null && list.size() > 0){
			proMap.put("totle", list.size());
			for(Article article:list){
				try{
					String path = article.getSortId();
					int len = path.length() / 9;
					Category[] categories = new Category[len];
					for(int i = 0 ; i < categories.length; i ++){
						String pathSortId = path.substring(0 * 9,9 + (i * 9));
						categories[i] = categoryDao.selectBySortId(uid, pathSortId);
					}
					String allPath = "";
					String genPath = "";
					for (Category cat:categories){
						genPath += "/" + cat.getCategoryName();
					}
					allPath = sharePath + genPath;
					File file = new File(allPath);
					if (!file.exists())file.mkdirs();
					OpenOfficeConvert oc = new OpenOfficeConvert();
					oc.getOfficeManager().start();
					genDocFile(article,allPath,oc);
					oc.getOfficeManager().stop();
					exportArticleList.add(article);
				}catch(Exception e){
					errorArticleList.add(article);
				}
				proMap.put("export", exportArticleList);
				proMap.put("error", errorArticleList);
			}
			//压缩文件
			try {
				File zipFile = new File(zipPath);
				if (!zipFile.exists())zipFile.mkdirs();
				ZipUtil util = new ZipUtil(zipPath + "/exportfile.zip");
				util.put(new String[]{sharePath});
				util.close();
				map.put("downloadpath", download + "/exportfile.zip");
			} catch (IOException e) {
				map.put("ziperr", "ziperr");
				e.printStackTrace();
			}
		}else{
			map.put("noexport", "noexport");
		}
		//导出的文章
		map.put("export", exportArticleList);
		//错误未导出的文章
		map.put("errexport", errorArticleList);
		return map;
	}
	


	@Override
	public Map<String, Object> processView() {
		return proMap;
	}

	@Override
	public PageUtil count(long uid, String sortId, String essence,String recycleBin, String keywords, int pageIndex,int pageSize) {
		long count = articleDao.selectByParamsCount(uid, sortId, essence,recycleBin, keywords);
		return new PageUtil((int) count, pageIndex, pageSize);
	}

	@Override
	public List<Article> list(long uid, String sortId, String essence,String recycleBin, String keywords,String sort, int pageIndex, int pageSize) {
		List<Article> articles = articleDao.selectByParams(uid, sortId, essence,recycleBin, keywords, sort,pageIndex, pageSize);
		return articles;
	}

}
