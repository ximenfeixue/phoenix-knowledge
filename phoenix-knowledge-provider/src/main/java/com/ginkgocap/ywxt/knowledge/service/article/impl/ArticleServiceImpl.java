package com.ginkgocap.ywxt.knowledge.service.article.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.URL;
import com.ginkgocap.ywxt.file.model.FileIndex;
import com.ginkgocap.ywxt.file.service.FileIndexService;
import com.ginkgocap.ywxt.knowledge.dao.article.ArticleDao;
import com.ginkgocap.ywxt.knowledge.dao.category.CategoryDao;
import com.ginkgocap.ywxt.knowledge.model.Article;
import com.ginkgocap.ywxt.knowledge.model.Category;
import com.ginkgocap.ywxt.knowledge.service.article.ArticleService;
import com.ginkgocap.ywxt.knowledge.util.Content;
import com.ginkgocap.ywxt.knowledge.util.CopyFile;
import com.ginkgocap.ywxt.knowledge.util.HTMLTemplate;
import com.ginkgocap.ywxt.knowledge.util.OpenOfficeConvert;
import com.ginkgocap.ywxt.knowledge.util.OpenOfficeServer;
import com.ginkgocap.ywxt.knowledge.util.ReadProperties;
import com.ginkgocap.ywxt.knowledge.util.gen.GenFile;
import com.ginkgocap.ywxt.knowledge.util.gen.GenHTML;
import com.ginkgocap.ywxt.knowledge.util.process.ExportWatched;
import com.ginkgocap.ywxt.knowledge.util.process.ExportWatcher;
import com.ginkgocap.ywxt.knowledge.util.process.ImportWatched;
import com.ginkgocap.ywxt.knowledge.util.process.ImportWatcher;
import com.ginkgocap.ywxt.knowledge.util.zip.ZipUtil;
import com.ginkgocap.ywxt.util.DateFunc;
import com.ginkgocap.ywxt.util.PageUtil;

@Service("articleService")
public class ArticleServiceImpl implements ArticleService{
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private CategoryDao categoryDao;
	@Autowired(required=false)
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
	
    private String getOfficeHome() {
        String osName = System.getProperty("os.name");
         if (Pattern.matches("Linux.*", osName)) {
            return "/opt/openoffice.org3";
         } else if (Pattern.matches("Windows.*", osName)) {
             return "C:/Program Files (x86)/OpenOffice.org 3";
         } else if (Pattern.matches("Mac.*", osName)) {
            return "/Application/OpenOffice.org.app/Contents";
        }
         return null;
    }
    private OfficeManager getOfficeManager() {
        DefaultOfficeManagerConfiguration config = new DefaultOfficeManagerConfiguration();
       // 获取OpenOffice.org 3的安装目录
       String officeHome = getOfficeHome();
       config.setOfficeHome(officeHome);
        // 启动OpenOffice的服务
         OfficeManager officeManager = config.buildOfficeManager();
        officeManager.start();
       return officeManager;
     }
	@Override
	public String exportArticleById(long id) {
		//**************************先从数据库获取文章信息*************************
		Article article = articleDao.selectByPrimaryKey(id);
		//*************************先通过取到的文章信息生成HTML文件*************************
		//生成HTML的路径
		String htmlPath = Content.EXPORTDOCPATH  + "/ID_" + id + "/" + article.getSortId() + "/" + "HTML/";
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
		OfficeManager os = this.getOfficeManager();
		OpenOfficeConvert oc = new OpenOfficeConvert(os);
		String wordPath =  Content.EXPORTDOCPATH + "/ID_" + id + "/" + article.getSortId() + "/" + "WORD/";
		String wordFileName = id + "_" + article.getArticleTitle() + ".doc";
		oc.htmlToWord(html, wordPath + wordFileName);
		os.stop();
		//*************************若成功生成word，将返回word路径*************************
		return wordPath + URL.encode(wordFileName);
	}


	@Override
	public List<Article> articleAllListBySortId(long uid, String sortId,
			String recycleBin, String essence) {
		List<Article> articles = articleDao.articleAllListBySortId(uid, sortId, recycleBin, essence);
		return articles;
	}

	private File createDir(String path){
		File f = new File(path);
		if (!f.exists())f.mkdirs();
		return f;
	}
	
	@Override	
	public Map<String,Object>exportFileBySortId(long uid, String sortId,String taskId,String recycleBin,String essence,String option){
		//dubbo树形文件
		Properties pro = new ReadProperties().getPro();
		//得到挂载路径
		String mountPath = Content.EXPORTMOUNTPATH;
		//返回的map信息
		Map<String, Object> map = new HashMap<String, Object>();
		//取到系统当前时间
		String now = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
		//需要压缩的路径
		String zipPath = Content.WEBSERVERPATH + "/" + Content.EXPORTDOCPATH + "/GENPATH_" + uid + "/" + now;
		//压缩输出路径
		String zipOutPath = Content.WEBSERVERPATH + "/" + Content.EXPORTDOCPATH + "/DOWNLOAD_" + uid + "/" + now;
		//已导出正确的文章列表
		List exportArticleList = new ArrayList();
		//导出时出错的文章列表
		List errorArticleList = new ArrayList();
		//通过sortId及Uid取到文章列表
		List<Article> list = articleDao.articleAllListBySortId(uid, sortId, recycleBin,essence);
		//生成word的文件夹
		File articleDir = null;
		//生成附件的文件夹名称
		File fileDir = null;
		//若文件列表为空不执行操作，直接返回map信息
		if(list != null && list.size() > 0){
			//执行到当前的序号
			int currentNum = 0;
			//被监听对象
			ExportWatched watched = new ExportWatched();
			//设置监听对象的任务标识
			watched.setTaskId(taskId);
			//设置监听任务的工作总数    加1为压缩的任务
			watched.setTotal(list.size() + 1);
			//将监听任务存放的Hash表中
			Content.MAP.put(taskId, watched);
			//得到OpenOffice服务端的实例
			OpenOfficeServer of = OpenOfficeServer.getInstance();
			//得到Office的管理对象以便启动服务
			OfficeManager om = of.getOfficeManager();
			//启动OpenOffice服务
			om.start();
			if ("1".equals(option)){
				//创建二级的文章目录
				articleDir = createDir(zipPath + "/" + "article");
			}else if ("2".equals(option)){
				//创建二级的附件目录
				fileDir = createDir(zipPath + "/" + "file");
			}else if ("3".equals(option)){
				//创建二级文章及附件目录
				articleDir = createDir(zipPath + "/" + "article");
				fileDir = createDir(zipPath + "/" + "file");
			}
			
			for(Article article:list){
				//当前任务序号通过文章数量的循环递增
				currentNum ++;
				//此篇文章处理的状态true为成功
				boolean flag = false;
				try{
					//当导出文章时生成word的名称为文章标题的名称，当导出附件时，多个附件时一个文章有多个附件时用文件夹存放，文件夹命名为文章
					String title = article.getArticleTitle();
					//生成文章包及内容
					if (articleDir != null){
						//根据每个文章的分类生成目录
						File articlepath = genPath(article,articleDir.getPath());
						//将文档转换到各个分类的目录中
						genDocFile(article,articlepath.getPath(),new OpenOfficeConvert(om));
					}
					//生成附件包及内容
					if (fileDir != null){
						//根据分类生成附件的目录
						File filepath = genPath(article,fileDir.getPath());
						//通过文章得到附件列表
						List<FileIndex> filelist = fileIndexService.selectByTaskId(taskId, "1");
						
						if (filelist != null){
							//已文章标题创建文件夹存放附件
							File articleFilePath = this.createDir(filepath.getParent() + "/" + title + "_ID" + article.getId());
							//将挂载点的文件拷贝到包中
							for(FileIndex f:filelist){
								//将文件拷贝到目录中
								CopyFile.copyFile(new File(mountPath + f.getFilePath()), new File(articleFilePath.getPath() + f.getFileTitle()));
							}
						}
					}
					//将转换完毕的文章对象存放到列表中
					exportArticleList.add(article);
					//标志当前文章处理成功
					flag = true;
				}catch(Exception e){
					//将出错的文章增加到列表中
					errorArticleList.add(article);
					e.printStackTrace();
				}
				watched.changeData(currentNum,article.getArticleTitle(),flag);
			}
			//压缩文件
			try {
				//创建压缩输出的路径
				createDir(zipOutPath);
				//初始化压缩工具
				ZipUtil util = new ZipUtil(zipOutPath + "/exportfile.zip");
				//压缩文件夹
				util.put(new String[]{zipPath});
				//关闭压缩工具流
				util.close();
				//设置监听为压缩成功
				watched.changeData(currentNum + 1,"文件压缩",true);
				//设置压缩路径到监听对象以便下载使用
				watched.setDownloadPath(zipOutPath + "/exportfile.zip");
				//设置整个任务成功
				map.put("result", "success");
			} catch (IOException e) {
				//设置监听为压缩失败
				watched.changeData(currentNum + 1,"文件压缩",false);
				//设置整个任务失败
				map.put("result", "error");
				e.printStackTrace();
			}
			//停止OpenOffice服务
			om.stop();
			//设置整个任务完成
			watched.setDone(true);
		}else{
			//文章列表为空
			map.put("noexport", "noexport");
		}
		//导出的文章
		map.put("export", exportArticleList);
		//错误未导出的文章
		map.put("errexport", errorArticleList);
		return map;
	}
	

	private File genPath(Article article,String sharePath){
		File f = null;
		try{
			//根据文章分类生成相应路径
			String path = article.getSortId();
			int len = path.length() / 9;
			Category[] categories = new Category[len];
			for(int i = 0 ; i < categories.length; i ++){
				String pathSortId = path.substring(0 * 9,9 + (i * 9));
				categories[i] = categoryDao.selectBySortId(article.getUid(), pathSortId);
			}
			String genPath = "";
			for (Category cat:categories){
				genPath += "/" + cat.getName();
			}
			f = this.createDir(sharePath + genPath);
		}catch(Exception e){
			e.printStackTrace();
		}
		return f;
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
					//生成需要转换的html
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
				file.setStatus(true);
				watched.changeData(k,file.getId(),flag);
			}
			fileIndexService.update(fileList);
			om.stop();
		}else{
			map.put("noimport", "noimport");
		}
		return map;
	}

	
	
	
	
	
	
	
	@Override
	public String importProcess(String taskId) {
		Map<String,Object> map = new HashMap<String,Object>();
		//从hash表中得到被监听的对象
		ImportWatched watched = (ImportWatched)Content.MAP.get(taskId);
		//通过被监听对象初始化监听对象
		ImportWatcher w = new ImportWatcher(watched);
		//得到被监听对象状态
		w.update(watched, "");
		//假如事务完成则从Hash表中清楚被监听对象
		if (watched.isDone()){
			Content.MAP.remove(taskId);
			System.out.println("监听列表移除:" + (Content.MAP.get(taskId) == null));
		}
		return w.getMes();
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

	@Override
	public void updateEssence(String[] ids, String essence) {
		articleDao.updateEssence(essence, ids);
	}

	@Override
	public void cleanRecyle(long uid) {
		articleDao.cleanRecyle(uid);
	}


}
