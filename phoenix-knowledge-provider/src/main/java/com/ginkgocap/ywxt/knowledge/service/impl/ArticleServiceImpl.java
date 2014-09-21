package com.ginkgocap.ywxt.knowledge.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.file.model.FileIndex;
import com.ginkgocap.ywxt.file.service.FileIndexService;
import com.ginkgocap.ywxt.knowledge.dao.article.ArticleDao;
import com.ginkgocap.ywxt.knowledge.dao.category.CategoryDao;
import com.ginkgocap.ywxt.knowledge.dao.share.KnowledgeShareDao;
import com.ginkgocap.ywxt.knowledge.model.Article;
import com.ginkgocap.ywxt.knowledge.model.Category;
import com.ginkgocap.ywxt.knowledge.service.ArticleService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeShareService;
import com.ginkgocap.ywxt.knowledge.util.Content;
import com.ginkgocap.ywxt.knowledge.util.CopyFile;
import com.ginkgocap.ywxt.knowledge.util.HTMLTemplate;
import com.ginkgocap.ywxt.knowledge.util.OpenOfficeConvert;
import com.ginkgocap.ywxt.knowledge.util.Snippet;
import com.ginkgocap.ywxt.knowledge.util.gen.GenFile;
import com.ginkgocap.ywxt.knowledge.util.gen.GenHTML;
import com.ginkgocap.ywxt.knowledge.util.process.ExportWatched;
import com.ginkgocap.ywxt.knowledge.util.process.ExportWatcher;
import com.ginkgocap.ywxt.knowledge.util.process.ImportWatched;
import com.ginkgocap.ywxt.knowledge.util.process.ImportWatcher;
import com.ginkgocap.ywxt.knowledge.util.zip.ZipUtil;
import com.ginkgocap.ywxt.util.DateFunc;
import com.ginkgocap.ywxt.util.MakeTaskId;
import com.ginkgocap.ywxt.util.PageUtil;

@Service("articleService")
public class ArticleServiceImpl implements ArticleService {
	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private KnowledgeShareDao knowledgeShareDao;
	@Autowired
	private KnowledgeShareService knowledgeShareService;

	public ArticleDao getArticleDao() {
		return articleDao;
	}

	public void setArticleDao(ArticleDao articleDao) {
		this.articleDao = articleDao;
	}

	public CategoryDao getCategoryDao() {
		return categoryDao;
	}

	public void setCategoryDao(CategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}

	public FileIndexService getFileIndexService() {
		return fileIndexService;
	}

	public void setFileIndexService(FileIndexService fileIndexService) {
		this.fileIndexService = fileIndexService;
	}

	@Autowired
	private FileIndexService fileIndexService;

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
		knowledgeShareDao.deleteShareInfoByKnowledgeId(id);
		
	}

	@Override
	public void update(Article article) {
		articleDao.update(article);
		knowledgeShareService.updateTitle(article.getUid(), article.getId(), article.getArticleTitle());
	}

	@Override
	public void updateEssence(String essence, String[] ids) {
		articleDao.updateEssence(essence, ids);
	}

	@Override
	public void updateRecycleBin(String recycleBin, String[] ids) {
		articleDao.updateRecycleBin(recycleBin, ids);
	}

	private String genDocFile(Article article, String path, OpenOfficeConvert oc) {
		String flag = "";
		String title = article.getArticleTitle();
//		//文件名处理
//		if (title != null) {
//			//去除文件名后去除扩展名
//			title = Snippet.getFileNameNoEx(title);
//		}
		// ******************************************************************生成HTML的文件名称
		String htmlFileName = article.getId() + "_" + title + ".html";
		// 拼接HTML模板
		HTMLTemplate ht = new HTMLTemplate();
		// html生成对象
		GenFile gf = new GenHTML();
		// 生成HTML并返回HTML文件对象
		File html = gf.genFile(ht.getTemplate(article), path + "/",htmlFileName);
//		System.out.println("htmlcontent ****************************" + ht.getTemplate(article));
		//生成的word文件名称
		String wordFileName = article.getId() + "_" + title + ".doc";
//		System.out.println("wordFileName+++++++++++++++++++++++++++++++++" + wordFileName);
		//通过html文件转换成word
		oc.htmlToWord(html, path + "/" + wordFileName);
		//删除临时的html文件
		html.delete();
		return flag;
	}

	private String genHtmlFile(File doc, String path, OpenOfficeConvert oc) {
		String flag = "";
		oc.wordToHtml(doc, path);
		return flag;
	}

	private String getOfficeHome() {
		String osName = System.getProperty("os.name");
		if (Pattern.matches("Windows.*", osName)) {
			return "C:/Program Files (x86)/OpenOffice.org 3";
		}
//		if (Pattern.matches("Linux.*", osName)) {
			return "/opt/openoffice.org3";
//		} else if (Pattern.matches("Windows.*", osName)) {
//			return "C:/Program Files (x86)/OpenOffice.org 3";
//		} else if (Pattern.matches("Mac.*", osName)) {
//			return "/Application/OpenOffice.org.app/Contents";
//		}
//		return null;
	}

	private OfficeManager getOfficeManager() {
		DefaultOfficeManagerConfiguration config = new DefaultOfficeManagerConfiguration();
		// 获取OpenOffice.org 3的安装目录
		String officeHome = getOfficeHome();
		// 设置openoffice安装目录
		config.setOfficeHome(officeHome);
		// 设置转换端口，默认为8100
		config.setPortNumbers(8100);
		// 设置任务执行超时为5分钟
		config.setTaskExecutionTimeout(1000 * 60 * 5L);
		// 设置任务队列超时为24小时
		config.setTaskQueueTimeout(1000 * 60 * 60 * 24L);
		// 启动OpenOffice的服务
		OfficeManager officeManager = config.buildOfficeManager();
		// 启动任务
		officeManager.start();
		return officeManager;
	}

	@Override
	public String exportArticleById(long id) {
		// *************************将生成的HTML文件转换成word文档*************************
		DefaultOfficeManagerConfiguration config = new DefaultOfficeManagerConfiguration();
		Random roll = new Random();
		config.setPortNumber(30000+roll.nextInt(1000));
		// 获取OpenOffice.org 3的安装目录
		String officeHome = getOfficeHome();
		System.out.println("officeHome:    " + officeHome);
		config.setOfficeHome(officeHome);
		// 启动OpenOffice的服务
		OfficeManager om = config.buildOfficeManager();
		// 启动OpenOffice服务
		om.start();
		try{
			// **************************先从数据库获取文章信息*************************
			Article article = articleDao.selectByPrimaryKey(id);
			// *************************先通过取到的文章信息生成HTML文件*************************
			// 生成HTML的路径
			String htmlPath = Content.WEBSERVERPATH + File.separator + "TEMP" + File.separator + "ID_" + id + File.separator + article.getSortId() + File.separator + "HTML" + File.separator;
//			System.out.println("htmlPath---------------------------------------" + htmlPath);
			// 生成HTML的文件名称
			String htmlFileName = id + "_" + article.getArticleTitle() + ".html";
//			System.out.println("htmlFileName---------------------------------------" + htmlFileName);
			// 拼接HTML模板
			// StringBuffer HTML = new StringBuffer("");
			// HTML.append(HTMLTemplate.getTemplate().replaceAll(HTMLTemplate.ARTICLE_TITLE,
			// article.getArticleTitle())
			// .replaceAll(HTMLTemplate.ARTICLE_CONTENT,
			// article.getArticleContent()));
			HTMLTemplate ht = new HTMLTemplate();
			// html生成对象
			GenFile gf = new GenHTML();
			// 生成HTML并返回HTML文件对象
			File html = gf.genFile(ht.getTemplate(article), htmlPath, htmlFileName);
			
			OpenOfficeConvert oc = new OpenOfficeConvert(om);
			// word生成路径
//			String wordPath = Content.WEBSERVERPATH +  File.separator + "TEMP" + File.separator + "ID_" + id + File.separator + article.getSortId() + File.separator + "WORD" + File.separator;
			String wordPath = Content.EXPORTMOUNTPATH + "/" ;
			//			System.out.println("wordPath---------------------------------------" + wordPath);
			// 返回下载路径
//			String wordFileName =  article.getArticleTitle() + ".doc";
			String wordFileName =   id + "_article.doc";
			wordFileName = Snippet.stringFilter(wordFileName);
//			String download = File.separator + "TEMP" + File.separator + "ID_" + id + File.separator + article.getSortId() + File.separator + "WORD" + File.separator + URL.encode(wordFileName);
			String download = wordFileName;
			createDir(wordPath);
//			System.out.println("wordFileName---------------------------------------" + wordFileName);
			oc.htmlToWord(html, wordPath +  wordFileName);
			// *************************若成功生成word，将返回word路径*************************
			return Content.NGINXROOT + "/attachment/downloadgenfile?filepath="+ download + "&articleid=" + article.getId();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			om.stop();
		}
		return "";
	}

	@Override
	public List<Article> articleAllListBySortId(long uid, String sortId,
			String recycleBin, String essence) {
		List<Article> articles = articleDao.articleAllListBySortId(uid, sortId,
				recycleBin, essence);
		return articles;
	}

	private File createDir(String path) {
		File f = new File(path);
		if (!f.exists())
			f.mkdirs();
		return f;
	}

	@Override
	public Map<String, Object> exportFileBySortId(long uid, String sortId,
			String taskId, String recycleBin, String essence, String option) {
		// 返回的map信息
		Map<String, Object> map = new HashMap<String, Object>();
		// 取到系统当前时间
		String now = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
				.format(new Date());
		// 需要压缩的路径
		String zipPath = Content.WEBSERVERPATH + Content.EXPORTDOCPATH
				+ File.separator + "GENPATH_" + uid + File.separator + now;
		// 压缩输出路径
//		String zipOutPath = Content.WEBSERVERPATH + Content.EXPORTDOCPATH
//				+ File.separator + "DOWNLOAD_" + uid + File.separator + now;
		String zipOutPath = Content.EXPORTMOUNTPATH;
		// 下载地址
//		String downloadPath = Content.EXPORTDOCPATH + File.separator + "DOWNLOAD_" + uid + File.separator + now;
		String downloadPath = uid + "_exportfile_multiple.zip";
		// 已导出正确的文章列表
		List exportArticleList = new ArrayList();
		// 导出时出错的文章列表
		List errorArticleList = new ArrayList();
		// 通过sortId及Uid取到文章列表
		List<Article> list = articleDao.articleAllListBySortId(uid, sortId,
				recycleBin, essence);
		// 生成word的文件夹
		File articleDir = null;
		// 生成附件的文件夹名称
		File fileDir = null;
		// 若文件列表为空不执行操作，直接返回map信息
		if (list != null && list.size() > 0) {
			//执行任务总数 列表数  + openoffice启动前初始化任务 + oppenoffice启动任务  + 压缩任务
			int taskNum = list.size() + 1 + 1;
			// 执行到当前的序号
			int currentNum = 1;
			// 被监听对象
			ExportWatched watched = new ExportWatched();
			// 设置监听对象的任务标识
			watched.setTaskId(taskId);
			// 设置监听任务的工作总数 加1为压缩的任务
			watched.setTotal(taskNum);
			// 将监听任务存放的Hash表中
			Content.MAP.put(taskId, watched);
			// //得到OpenOffice服务端的实例
			// OpenOfficeServer of = OpenOfficeServer.getInstance();
			// 得到Office的管理对象以便启动服务
			DefaultOfficeManagerConfiguration config = new DefaultOfficeManagerConfiguration();
			Random roll = new Random();
			config.setPortNumber(33000+roll.nextInt(1000));
			// 获取OpenOffice.org 3的安装目录
			String officeHome = getOfficeHome();
			config.setOfficeHome(officeHome);
			// 启动OpenOffice的服务
			OfficeManager om = config.buildOfficeManager();
			watched.changeData(1, "导出准备", false);
			// 启动OpenOffice服务
			om.start();
//			List <Category> cat = categoryDao.selectChildBySortId(uid, sortId);
			if ("1".equals(option)) {
				// 创建二级的文章目录
				articleDir = createDir(zipPath + File.separator + "article");
//				for(Category c:cat){
//					genPath(c.getSortId(),uid,articleDir.getPath());
//				}
			} else if ("2".equals(option)) {
				// 创建二级的附件目录
				fileDir = createDir(zipPath + File.separator + "file");
//				for(Category c:cat){
//					genPath(c.getSortId(),uid,fileDir.getPath());
//				}
			} else if ("3".equals(option)) {
				// 创建二级文章及附件目录
				articleDir = createDir(zipPath + File.separator + "article");
				fileDir = createDir(zipPath + File.separator + "file");
//				for(Category c:cat){
//					genPath(c.getSortId(),uid,articleDir.getPath());
//				}
//				for(Category c:cat){
//					genPath(c.getSortId(),uid,fileDir.getPath());
//				}
			}


			for (Article article : list) {
				// 当前任务序号通过文章数量的循环递增
				currentNum ++;
				// 此篇文章处理的状态true为成功
				boolean flag = false;
				try {
					// 当导出文章时生成word的名称为文章标题的名称，当导出附件时，多个附件时一个文章有多个附件时用文件夹存放，文件夹命名为文章
					String title = article.getArticleTitle();
					// 生成文章包及内容
					if (articleDir != null) {
						// 根据每个文章的分类生成目录
						File articlepath = genPath(article, articleDir.getPath());
						// 将文档转换到各个分类的目录中
						genDocFile(article, articlepath.getPath(), new OpenOfficeConvert(om));
					}
					// 生成附件包及内容
					if (fileDir != null) {
						// 根据分类生成附件的目录
						File filepath = genPath(article, fileDir.getPath());
						// 通过文章得到附件列表
						List<FileIndex> filelist = fileIndexService.selectByTaskId(article.getTaskId(), "1");

						if (filelist != null) {
							// 以文章标题创建文件夹存放附件
							File articleFilePath = this.createDir(filepath.getPath() + File.separator + "article_ID" + article.getId());
							// 将挂载点的文件拷贝到包中
							for (FileIndex f : filelist) {
								File source = new File(f.getFilePath());
								File target = new File(articleFilePath.getPath() + File.separator + f.getFileTitle());
								// 将文件拷贝到目录中
								CopyFile.copyFile(source,target);
							}
						}
					}
					// 将转换完毕的文章对象存放到列表中
					exportArticleList.add(article);
					// 标志当前文章处理成功
					flag = true;
					watched.changeData(currentNum, article.getArticleTitle(), flag);
				} catch (Exception e) {
					watched.changeData(currentNum, article.getArticleTitle(), flag);
					// 将出错的文章增加到列表中
					errorArticleList.add(article);
					e.printStackTrace();
				}
				
			}
			// 压缩文件
			try {
				// 创建压缩输出的路径
				createDir(zipOutPath);
				// 初始化压缩工具
				ZipUtil util = new ZipUtil(zipOutPath + File.separator + uid + "_exportfile_multiple.zip");
				// 压缩文件夹
				util.put(new String[] { zipPath });
				// 压缩文件说明
				String rem = "\nfiles:" + util.getFileCount() + " ! sources:www.gintong.com ";
				util.comment.append(rem);
				// 设置压缩文件说明
				util.setComment(util.comment.toString());
				// 关闭压缩工具流
				util.close();
				// 设置监听为压缩成功
				watched.changeData(currentNum + 1, "文件压缩", true);
				// 设置压缩路径到监听对象以便下载使用
				watched.setDownloadPath(downloadPath);
				// 设置整个任务成功
				map.put("result", "success");
			} catch (IOException e) {
				// 设置监听为压缩失败
				watched.changeData(currentNum + 1, "文件压缩", false);
				// 设置整个任务失败
				map.put("result", "error");
				e.printStackTrace();
			}
			// 停止OpenOffice服务
			om.stop();
			// 设置整个任务完成
			watched.setDone(true);
		} else {
			map.put("noexport", "noexport");
		}
		// 导出的文章
		map.put("export", exportArticleList);
		// 错误未导出的文章
		map.put("errexport", errorArticleList);
		return map;
	}

	private File genPath(Article article, String sharePath) {
		File f = null;
		try {
			// 根据文章分类生成相应路径
			String path = article.getSortId();
			int len = path.length() / 9;
			Category[] categories = new Category[len];
			for (int i = 0; i < categories.length; i++) {
				String pathSortId = path.substring(0 * 9, 9 + (i * 9));
				categories[i] = categoryDao.selectBySortId(article.getUid(),
						pathSortId);
			}
			String genPath = "";
			for (Category cat : categories) {
				genPath += File.separator + "目录_" + cat.getId() + "_" + cat.getName();
			}
			f = this.createDir(sharePath + genPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}

	
	private File genPath(String sortId, long uid,String sharePath) {
		File f = null;
		try {
			// 根据文章分类生成相应路径
			String path = sortId;
			int len = path.length() / 9;
			Category[] categories = new Category[len];
			for (int i = 0; i < categories.length; i++) {
				String pathSortId = path.substring(0 * 9, 9 + (i * 9));
				categories[i] = categoryDao.selectBySortId(uid,
						pathSortId);
			}
			String genPath = "";
			for (Category cat : categories) {
				genPath += File.separator + "目录_" + cat.getId() + "_" + cat.getName();
			}
			f = this.createDir(sharePath + genPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}
	
	
	@Override
	public Map<String, Object> processView(String taskId) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 从hash表中得到被监听的对象
		ExportWatched watched = (ExportWatched) Content.MAP.get(taskId);
		// 通过被监听对象初始化监听对象
		ExportWatcher w = new ExportWatcher(watched);
		// 得到被监听对象状态
		w.update(watched, "");
		// 假如事务完成则从Hash表中清楚被监听对象
		if (watched.isDone()) {
			Content.MAP.remove(taskId);
			System.out.println("监听列表移除:" + (Content.MAP.get(taskId) == null));
		}
		map.put("mes", w.getMes());
		map.put("downloadPath", w.getProgen());
		return map;
	}

	@Override
	public Map<String, Object> importFileBySortId(long uid, long categoryId,
			List<FileIndex> fileList, String taskId) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 得到分类的sortId
		String sortId = categoryDao.selectByPrimaryKey(categoryId).getSortId();
		if (fileList != null && fileList.size() > 0) {
			//任务执行总数，列表数 + openoffice启动前任务 + openoffice启动后任务 + openoffice关闭任务
			int total = fileList.size() + 1 + 1 + 1; 
			// 被监听对象
			ImportWatched watched = new ImportWatched();
			watched.setTaskId(taskId);
			watched.setTotal(total);
			Content.MAP.put(taskId, watched);
			// html临时生成路径
			String HtmlPath = Content.WEBSERVERPATH + Content.EXPORTDOCPATH
					+ File.separator + "GENPATH_" + uid + File.separator + "HTMLTEMP";
			// 已转入的文件列表
			watched.changeData(1, "任务启动准备", false);
			List importFileList = new ArrayList();
			// 转环时出错的列表
			List errorFileList = new ArrayList();
			// 得到Office的管理对象以便启动服务
			DefaultOfficeManagerConfiguration config = new DefaultOfficeManagerConfiguration();
			Random roll = new Random();
			config.setPortNumber(35000+roll.nextInt(1000));
			// 获取OpenOffice.org 3的安装目录
			String officeHome = getOfficeHome();
			config.setOfficeHome(officeHome);
			// 启动OpenOffice的服务
			OfficeManager om = config.buildOfficeManager();
			// 启动OpenOffice服务
			watched.changeData(2, "任务启动", true);
			om.start();
			int k = 2;
			for (FileIndex file : fileList) {
				boolean flag = false;
				k++;
				try {
					// 将文件作者存入为文章作者
					String author = file.getAuthorName();
					String filetaskId = MakeTaskId.getTaskId(author);
					String title = file.getFileTitle();
					//文件名处理
					if (title != null) {
						//去除文件名后去除扩展名
						title = Snippet.getFileNameNoEx(title);
					}
					// 生成临时的html文件名称
					String htmlName = HtmlPath + File.separator + file.getId() + "_" + title + ".html";
//					System.out.println("htmlName:--------------------------------------" + htmlName);
					String date = DateFunc.getDate();
					OpenOfficeConvert oc = new OpenOfficeConvert(om);

//					System.out.println("path------------------" + file.getFilePath());
					// 生成需要转换的html
					genHtmlFile(new File(file.getFilePath()), htmlName, oc);
					// System.out.println("word文件路径:" + file.getFilePath());
					// System.out.println("生成的html文件:" + htmlName);
					// 得到内容
					String content = oc.getHTML(new File(htmlName));
					if (content != null)
						content = Snippet.imgFilter(content);
//					System.out.println("------------------------------------------------" + content + "");
					// 设置存入数据库的对象
					Article article = new Article();
					article.setArticleContent(content);
					article.setArticleTitle(title);
					article.setAuthor(author);
					article.setCategoryid(categoryId);
					article.setSortId(sortId);
					article.setClickNum(0);
					article.setEssence("0");
					article.setModifyTime(date);
					article.setPubdate(date);
					article.setRecycleBin("0");
					article.setState("0");
					article.setTaskId(filetaskId);
					article.setUid(uid);
					Article narticle = articleDao.insert(article);
					if (narticle != null){
						importFileList.add(file);
					}else{
						errorFileList.add(file);
					}
					
					flag = true;
				} catch (Exception e) {
					errorFileList.add(file);
					e.printStackTrace();
				}
				watched.changeData(k, file.getId(), flag);
			}
			om.stop();
			map.put("errlist", errorFileList);
			map.put("importlist", importFileList);
			watched.changeData(k + 1, "任务关闭", true);
			watched.setDone(true);
		} else {
			map.put("noimport", "noimport");
		}
		return map;
	}

	@Override
	public String importProcess(String taskId) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 从hash表中得到被监听的对象
		ImportWatched watched = (ImportWatched) Content.MAP.get(taskId);
		// 通过被监听对象初始化监听对象
		ImportWatcher w = new ImportWatcher(watched);
		// 得到被监听对象状态
		w.update(watched, "");
		// 假如事务完成则从Hash表中清楚被监听对象
		if (watched.isDone()) {
			Content.MAP.remove(taskId);
			System.out.println("监听列表移除:" + (Content.MAP.get(taskId) == null));
		}
		return w.getMes();
	}

	@Override
	public PageUtil count(long uid, String sortId, String essence,
			String recycleBin, String keywords, int pageIndex, int pageSize) {
		long count = articleDao.selectByParamsCount(uid, sortId, essence,
				recycleBin, keywords);
		return new PageUtil((int) count, pageIndex, pageSize);
	}

	@Override
	public List<Article> list(long uid, String sortId, String essence,
			String recycleBin, String keywords, String sort, int pageIndex,
			int pageSize) {
		List<Article> articles = articleDao.selectByParams(uid, sortId,
				essence, recycleBin, keywords, sort, pageIndex, pageSize);
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
		for(String id : ids){
			if(StringUtils.isNotBlank(id)){
				knowledgeShareDao.deleteShareInfoByKnowledgeId(Long.valueOf(id));
			}
		}
	}

	@Override
	public void updateEssence(String[] ids, String essence) {
		articleDao.updateEssence(essence, ids);
	}

	@Override
	public void cleanRecyle(long uid) {
		articleDao.cleanRecyle(uid);
	}

	@Override
	public long selectRecyleNum(long uid) {
		// TODO Auto-generated method stub
		return articleDao.selectRecyleNum(uid);
	}

	@Override
	public List<Article> relationList(long uid, long ralatoinid, String sort,
			 int pageIndex, int pageSize) {
		// TODO Auto-generated method stub
		return articleDao.relationList(uid,ralatoinid,sort,pageIndex,pageSize);
	}
	@Override
	public PageUtil count(long uid,String articleType, String sortId, String essence,
			String recycleBin, String keywords, int pageIndex, int pageSize) {
		long count = articleDao.selectByParamsCount(uid,articleType, sortId, essence,
				recycleBin, keywords);
		return new PageUtil((int) count, pageIndex, pageSize);
	}

	@Override
	public List<Article> list(long uid,String articleType, String sortId, String essence,
			String recycleBin, String keywords, String sort, int pageIndex,
			int pageSize) {
		List<Article> articles = articleDao.selectByParams(uid,articleType, sortId,
				essence, recycleBin, keywords, sort, pageIndex, pageSize);
		return articles;
	}
}
