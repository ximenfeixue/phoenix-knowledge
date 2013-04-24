package com.ginkgocap.ywxt.knowledge.service.article.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.artofsolving.jodconverter.office.OfficeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.URL;
import com.ginkgocap.ywxt.file.model.FileIndex;
import com.ginkgocap.ywxt.knowledge.dao.article.ArticleDao;
import com.ginkgocap.ywxt.knowledge.dao.category.CategoryDao;
import com.ginkgocap.ywxt.knowledge.model.Article;
import com.ginkgocap.ywxt.knowledge.model.Category;
import com.ginkgocap.ywxt.knowledge.service.article.ArticleService;
import com.ginkgocap.ywxt.knowledge.util.Content;
import com.ginkgocap.ywxt.knowledge.util.HTMLTemplate;
import com.ginkgocap.ywxt.knowledge.util.OpenOfficeConvert;
import com.ginkgocap.ywxt.knowledge.util.OpenOfficeServer;
import com.ginkgocap.ywxt.knowledge.util.gen.GenFile;
import com.ginkgocap.ywxt.knowledge.util.gen.GenHTML;
import com.ginkgocap.ywxt.knowledge.util.process.ExportWatched;
import com.ginkgocap.ywxt.knowledge.util.process.ExportWatcher;
import com.ginkgocap.ywxt.knowledge.util.process.ImportWatched;
import com.ginkgocap.ywxt.knowledge.util.zip.ZipUtil;
import com.ginkgocap.ywxt.util.DateFunc;
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
		HTML.append(HTMLTemplate.getTemplate().replaceAll(HTMLTemplate.ARTICLE_TITLE, article.getArticleTitle())
				   .replaceAll(HTMLTemplate.ARTICLE_CONTENT, article.getArticleContent()));
		//html生成对象
		GenFile gf = new GenHTML();
		//生成HTML并返回HTML文件对象
		File html = gf.genFile(HTML.toString(), path + "/", htmlFileName);
		String wordFileName = article.getId() + "_" + article.getArticleTitle() + ".doc";
		oc.htmlToWord(html, path + "/" + wordFileName);
		html.delete();
		return flag;
	}
	
	private String genHtmlFile(File doc,String path,OpenOfficeConvert oc){
		String flag = "";
		oc.wordToHtml(doc, path);
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
		HTML.append(HTMLTemplate.getTemplate().replaceAll(HTMLTemplate.ARTICLE_TITLE, article.getArticleTitle())
											   .replaceAll(HTMLTemplate.ARTICLE_CONTENT, article.getArticleContent()));
		//html生成对象
		GenFile gf = new GenHTML();
		//生成HTML并返回HTML文件对象
		File html = gf.genFile(HTML.toString(), htmlPath, htmlFileName);
		//*************************将生成的HTML文件转换成word文档*************************
		OfficeManager os = OpenOfficeServer.getInstance().getOfficeManager();
		os.start();
		OpenOfficeConvert oc = new OpenOfficeConvert(os);
		String wordPath = sharePath + "UID_62/" + article.getId() + "/" + article.getSortId() + "/" + "WORD/";
		String wordFileName = id + "_" + article.getArticleTitle() + ".doc";
		oc.htmlToWord(html, wordPath + wordFileName);
		os.stop();
		//*************************若成功生成word，将返回word路径*************************
		return "/GENFILE/TEMP/" + "UID_62/" + article.getId() + "/" + article.getSortId() + "/" + "WORD/" + URL.encode(wordFileName);
	}


	@Override
	public List<Article> articleAllListBySortId(long uid, String sortId,
			String recycleBin, String essence) {
		List<Article> articles = articleDao.articleAllListBySortId(uid, sortId, recycleBin, essence);
		return articles;
	}

	
	@Override
	public Map<String,Object> exportFileBySortId(long uid, String sortId,String taskId,String recycleBin,String essence) {
		Map<String, Object> map = new HashMap<String, Object>();
		//取到系统当前时间
		String nowDate = new Date().getTime() + "";
		//生成的word文件路径
		String sharePath = Content.EXPORTDOCPATH + uid + "/genfile/" + nowDate;
		//生成word后压缩的的文件路径
		String zipPath = Content.EXPORTDOCPATH + uid + "/" + nowDate;
		//下载压缩文件地址
		String download = "/GENFILE/TEMP/" + uid + "/" + nowDate;
		//已转黄的文章列表
		List exportArticleList = new ArrayList();
		//转换时出错的文章列表
		List errorArticleList = new ArrayList();
		//取到需要导出的文章列表
		List<Article> list = articleDao.articleAllListBySortId(uid, sortId, recycleBin,essence);
		//当有文章列表不为空时执行转换操作
		if (list != null && list.size() > 0){
				//得到OpenOffice服务端的实例
				OpenOfficeServer of = OpenOfficeServer.getInstance();
				OfficeManager om = of.getOfficeManager();
				//启动OpenOffice服务
				om.start();
				//被监听对象
				ExportWatched watched = new ExportWatched();
				watched.setTaskId(taskId);
				watched.setTotal(list.size() + 1);
				Content.MAP.put(taskId, watched);
				int k=0;
				for(Article article:list){
					boolean flag = false;
					k++;
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
						//通过html生成word
						genDocFile(article,allPath,new OpenOfficeConvert(om));
						exportArticleList.add(article);
						flag = true;
					}catch(Exception e){
						e.printStackTrace();
						errorArticleList.add(article);
					}
					watched.changeData(k,article.getArticleTitle(),flag);
				}
				//压缩文件
				try {
					File zipFile = new File(zipPath);
					if (!zipFile.exists())zipFile.mkdirs();
					ZipUtil util = new ZipUtil(zipPath + "/exportfile.zip");
					util.put(new String[]{sharePath});
					util.close();
					map.put("downloadpath", download + "/exportfile.zip");
					watched.changeData(k + 1,"文件压缩",true);
					watched.setDownloadPath(download + "/exportfile.zip");
				} catch (IOException e) {
					map.put("ziperr", "ziperr");
					watched.changeData(k + 1,"文件压缩",false);
					e.printStackTrace();
				}
				om.stop();
				watched.setDone(true);
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
	public Map<String,Object> processView(String taskId) {
		Map<String,Object> map = new HashMap<String,Object>();
		//从hash表中得到被监听的对象
		ExportWatched watched = (ExportWatched)Content.MAP.get(taskId);
		//通过被监听对象初始化监听对象
		ExportWatcher w = new ExportWatcher(watched);
		//得到被监听对象状态
		w.update(watched, "");
		//假如事务完成则从Hash表中清楚被监听对象
		if (watched.isDone()){
			Content.MAP.remove(taskId);
			System.out.println("监听列表移除:" + (Content.MAP.get(taskId) == null));
		}
		map.put("mes", w.getMes());
		map.put("downloadPath", w.getProgen());
		return map;
	}


	@Override
	public Map<String, Object> importFileBySortId(long uid, long categoryId,List<FileIndex> fileList,String taskId) {
		Map<String, Object> map = new HashMap<String, Object>();
		//得到分类的sortId
		String sortId = categoryDao.selectByPrimaryKey(categoryId).getSortId();
		if (fileList != null && fileList.size() > 0){
			String HtmlPath = "";
			//已转入的文件列表
			List importFileList = new ArrayList();
			//转环时出错的列表
			List errorFileList = new ArrayList();
			//得到OpenOffice服务端的实例
			OpenOfficeServer of = OpenOfficeServer.getInstance();
			OfficeManager om = of.getOfficeManager();
			//启动OpenOffice服务
			om.start();
			//被监听对象
			ImportWatched watched = new ImportWatched();
			watched.setTaskId(taskId);
			watched.setTotal(fileList.size());
			Content.MAP.put(taskId, watched);
			int k=0;
			for (FileIndex file:fileList){
				boolean flag = false;
				k ++;
				try{
					String date = DateFunc.getDate();
					OpenOfficeConvert oc = new OpenOfficeConvert(om);
					//将文件名称存入为文章标题
					String Articleitle = file.getFileTitle();
					//将文件作者存入为文章作者
					String author = file.getAuthorName();
					//将taskId设置为文章的taskId
					genHtmlFile(new File(file.getFilePath()),HtmlPath,oc);
					//得到内容
					String content = oc.getHTML(new File(HtmlPath));
					//设置存入数据库的对象
					Article article = new Article();
					article.setArticleContent(content);
					article.setArticleTitle(Articleitle);
					article.setAuthor(author);
					article.setCategoryid(categoryId);
					article.setSortId(sortId);
					article.setClickNum(0);
					article.setEssence("0");
					article.setModifyTime(date);
					article.setPubdate(date);
					article.setRecycleBin("0");
					article.setState("0");
					article.setTaskId(taskId);
					article.setUid(uid);
					articleDao.insert(article);
					importFileList.add(file);
					flag = true;
				}catch(Exception e){
					errorFileList.add(file);
					e.printStackTrace();
				}
				watched.changeData(k,file.getId(),flag);
			}
			om.stop();
		}else{
			map.put("noimport", "noimport");
		}
		return map;
	}

	@Override
	public String importProcess(String taskId) {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public Map<String, Object> importFileBySortId(long uid, long category,
			String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteArticles(String[] ids) {
		articleDao.deleteArticles(ids);
	}


}
