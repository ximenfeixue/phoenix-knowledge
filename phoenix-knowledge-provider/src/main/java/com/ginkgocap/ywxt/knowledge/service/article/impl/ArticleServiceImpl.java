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
		//******************************************************************����HTML���ļ�����
		String htmlFileName = article.getId() + "_" + article.getArticleTitle() + ".html";
		//ƴ��HTMLģ��
		StringBuffer HTML = new StringBuffer("");
		HTML.append(HTMLTemplate.getTemplate().replaceAll(HTMLTemplate.ARTICLE_TITLE, article.getArticleTitle())
				   .replaceAll(HTMLTemplate.ARTICLE_CONTENT, article.getArticleContent()));
		//html���ɶ���
		GenFile gf = new GenHTML();
		//����HTML������HTML�ļ�����
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
       // ��ȡOpenOffice.org 3�İ�װĿ¼
       String officeHome = getOfficeHome();
       //����openoffice��װĿ¼
       config.setOfficeHome(officeHome);
       //����ת���˿ڣ�Ĭ��Ϊ8100 
       config.setPortNumbers(8100);
       //��������ִ�г�ʱΪ5����  
       config.setTaskExecutionTimeout(1000 * 60 * 5L);
       //����������г�ʱΪ24Сʱ  
       config.setTaskQueueTimeout(1000 * 60 * 60 * 24L);
       // ����OpenOffice�ķ���
       OfficeManager officeManager = config.buildOfficeManager();
       //��������
       officeManager.start();
       return officeManager;
     }
	@Override
	public String exportArticleById(long id) {
		//**************************�ȴ����ݿ��ȡ������Ϣ*************************
		Article article = articleDao.selectByPrimaryKey(id);
		//*************************��ͨ��ȡ����������Ϣ����HTML�ļ�*************************
		//����HTML��·��
		String htmlPath = Content.EXPORTDOCPATH  + "/ID_" + id + "/" + article.getSortId() + "/" + "HTML/";
		//����HTML���ļ�����
		String htmlFileName = id + "_" + article.getArticleTitle() + ".html";
		//ƴ��HTMLģ��
		StringBuffer HTML = new StringBuffer("");
		HTML.append(HTMLTemplate.getTemplate().replaceAll(HTMLTemplate.ARTICLE_TITLE, article.getArticleTitle())
											   .replaceAll(HTMLTemplate.ARTICLE_CONTENT, article.getArticleContent()));
		//html���ɶ���
		GenFile gf = new GenHTML();
		//����HTML������HTML�ļ�����
		File html = gf.genFile(HTML.toString(), htmlPath, htmlFileName);
		//*************************�����ɵ�HTML�ļ�ת����word�ĵ�*************************
		OfficeManager os = this.getOfficeManager();
		OpenOfficeConvert oc = new OpenOfficeConvert(os);
		String wordPath =  Content.EXPORTDOCPATH + "/ID_" + id + "/" + article.getSortId() + "/" + "WORD/";
		String wordFileName = id + "_" + article.getArticleTitle() + ".doc";
		oc.htmlToWord(html, wordPath + wordFileName);
		os.stop();
		//*************************���ɹ�����word��������word·��*************************
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
		//dubbo�����ļ�
		Properties pro = new ReadProperties().getPro();
		//�õ�����·��
		String mountPath = Content.EXPORTMOUNTPATH;
		//���ص�map��Ϣ
		Map<String, Object> map = new HashMap<String, Object>();
		//ȡ��ϵͳ��ǰʱ��
		String now = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
		//��Ҫѹ����·��
		String zipPath = Content.WEBSERVERPATH  + Content.EXPORTDOCPATH + "/GENPATH_" + uid + "/" + now;
		//ѹ�����·��
		String zipOutPath = Content.WEBSERVERPATH + Content.EXPORTDOCPATH + "/DOWNLOAD_" + uid + "/" + now;
		//���ص�ַ
		String downloadPath = Content.EXPORTDOCPATH + "/DOWNLOAD_" + uid + "/" + now;
		//�ѵ�����ȷ�������б�
		List exportArticleList = new ArrayList();
		//����ʱ����������б�
		List errorArticleList = new ArrayList();
		//ͨ��sortId��Uidȡ�������б�
		List<Article> list = articleDao.articleAllListBySortId(uid, sortId, recycleBin,essence);
		//����word���ļ���
		File articleDir = null;
		//���ɸ������ļ�������
		File fileDir = null;
		//���ļ��б�Ϊ�ղ�ִ�в�����ֱ�ӷ���map��Ϣ
		if(list != null && list.size() > 0){
			//ִ�е���ǰ�����
			int currentNum = 0;
			//����������
			ExportWatched watched = new ExportWatched();
			//���ü�������������ʶ
			watched.setTaskId(taskId);
			//���ü�������Ĺ�������    ��1Ϊѹ��������
			watched.setTotal(list.size() + 1);
			//�����������ŵ�Hash����
			Content.MAP.put(taskId, watched);
//			//�õ�OpenOffice����˵�ʵ��
//			OpenOfficeServer of = OpenOfficeServer.getInstance();
			//�õ�Office�Ĺ�������Ա���������
			DefaultOfficeManagerConfiguration config = new DefaultOfficeManagerConfiguration();
			// ��ȡOpenOffice.org 3�İ�װĿ¼
			String officeHome = getOfficeHome();
			config.setOfficeHome(officeHome);
			// ����OpenOffice�ķ���
			OfficeManager om = config.buildOfficeManager();
			//����OpenOffice����
			om.start();
			if ("1".equals(option)){
				//��������������Ŀ¼
				articleDir = createDir(zipPath + "/" + "article");
			}else if ("2".equals(option)){
				//���������ĸ���Ŀ¼
				fileDir = createDir(zipPath + "/" + "file");
			}else if ("3".equals(option)){
				//�����������¼�����Ŀ¼
				articleDir = createDir(zipPath + "/" + "article");
				fileDir = createDir(zipPath + "/" + "file");
			}
			
			for(Article article:list){
				//��ǰ�������ͨ������������ѭ������
				currentNum ++;
				//��ƪ���´����״̬trueΪ�ɹ�
				boolean flag = false;
				try{
					//����������ʱ����word������Ϊ���±�������ƣ�����������ʱ���������ʱһ�������ж������ʱ���ļ��д�ţ��ļ�������Ϊ����
					String title = article.getArticleTitle();
					//�������°�������
					if (articleDir != null){
						//����ÿ�����µķ�������Ŀ¼
						File articlepath = genPath(article,articleDir.getPath());
						//���ĵ�ת�������������Ŀ¼��
						genDocFile(article,articlepath.getPath(),new OpenOfficeConvert(om));
					}
					//���ɸ�����������
					if (fileDir != null){
						//���ݷ������ɸ�����Ŀ¼
						File filepath = genPath(article,fileDir.getPath());
						//ͨ�����µõ������б�
						List<FileIndex> filelist = fileIndexService.selectByTaskId(taskId, "1");
						
						if (filelist != null){
							//�����±��ⴴ���ļ��д�Ÿ���
							File articleFilePath = this.createDir(filepath.getParent() + "/" + title + "_ID" + article.getId());
							//�����ص���ļ�����������
							for(FileIndex f:filelist){
								//���ļ�������Ŀ¼��
								CopyFile.copyFile(new File(mountPath + f.getFilePath()), new File(articleFilePath.getPath() + f.getFileTitle()));
							}
						}
					}
					//��ת����ϵ����¶����ŵ��б���
					exportArticleList.add(article);
					//��־��ǰ���´���ɹ�
					flag = true;
				}catch(Exception e){
					//��������������ӵ��б���
					errorArticleList.add(article);
					e.printStackTrace();
				}
				watched.changeData(currentNum,article.getArticleTitle(),flag);
			}
			//ѹ���ļ�
			try {
				//����ѹ�������·��
				createDir(zipOutPath);
				//��ʼ��ѹ������
				ZipUtil util = new ZipUtil(zipOutPath + "/exportfile.zip");
				//ѹ���ļ���
				util.put(new String[]{zipPath});
				//�ر�ѹ��������
				util.close();
				//���ü���Ϊѹ���ɹ�
				watched.changeData(currentNum + 1,"�ļ�ѹ��",true);
				//����ѹ��·�������������Ա�����ʹ��
				watched.setDownloadPath(downloadPath + "/exportfile.zip");
				//������������ɹ�
				map.put("result", "success");
			} catch (IOException e) {
				//���ü���Ϊѹ��ʧ��
				watched.changeData(currentNum + 1,"�ļ�ѹ��",false);
				//������������ʧ��
				map.put("result", "error");
				e.printStackTrace();
			}
			//ֹͣOpenOffice����
			om.stop();
			//���������������
			watched.setDone(true);
		}else{
			//�����б�Ϊ��
			map.put("noexport", "noexport");
		}
		//����������
		map.put("export", exportArticleList);
		//����δ����������
		map.put("errexport", errorArticleList);
		return map;
	}
	

	private File genPath(Article article,String sharePath){
		File f = null;
		try{
			//�������·���������Ӧ·��
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
		//��hash���еõ��������Ķ���
		ExportWatched watched = (ExportWatched)Content.MAP.get(taskId);
		//ͨ�������������ʼ����������
		ExportWatcher w = new ExportWatcher(watched);
		//�õ�����������״̬
		w.update(watched, "");
		//��������������Hash�����������������
		if (watched.isDone()){
			Content.MAP.remove(taskId);
			System.out.println("�����б��Ƴ�:" + (Content.MAP.get(taskId) == null));
		}
		map.put("mes", w.getMes());
		map.put("downloadPath", w.getProgen());
		return map;
	}


	@Override
	public Map<String, Object> importFileBySortId(long uid, long categoryId,List<FileIndex> fileList,String taskId) {
		Map<String, Object> map = new HashMap<String, Object>();
		//�õ������sortId
		String sortId = categoryDao.selectByPrimaryKey(categoryId).getSortId();
		if (fileList != null && fileList.size() > 0){
			//html��ʱ����·��
			String HtmlPath = Content.WEBSERVERPATH  + Content.EXPORTDOCPATH + "/GENPATH_" + uid + "/HTMLTEMP";
			//��ת����ļ��б�
			List importFileList = new ArrayList();
			//ת��ʱ������б�
			List errorFileList = new ArrayList();
			//�õ�Office�Ĺ�������Ա���������
			DefaultOfficeManagerConfiguration config = new DefaultOfficeManagerConfiguration();
			// ��ȡOpenOffice.org 3�İ�װĿ¼
			String officeHome = getOfficeHome();
			config.setOfficeHome(officeHome);
			// ����OpenOffice�ķ���
			OfficeManager om = config.buildOfficeManager();
			//����OpenOffice����
			om.start();
			//����������
			ImportWatched watched = new ImportWatched();
			watched.setTaskId(taskId);
			watched.setTotal(fileList.size());
			Content.MAP.put(taskId, watched);
			int k=0;
			for (FileIndex file:fileList){
				boolean flag = false;
				k ++;
				try{
					//������ʱ��html�ļ�����
					String htmlName = HtmlPath + "/" + file.getId() + "_" + file.getFileTitle() + ".html";
					String date = DateFunc.getDate();
					OpenOfficeConvert oc = new OpenOfficeConvert(om);
					//���ļ����ƴ���Ϊ���±���
					String Articleitle = file.getFileTitle();
					//���ļ����ߴ���Ϊ��������
					String author = file.getAuthorName();
					//������Ҫת����html
					genHtmlFile(new File(file.getFilePath()),htmlName,oc);
					System.out.println("word�ļ�·��:" + file.getFilePath());
					System.out.println("���ɵ�html�ļ�:" + htmlName);
					//�õ�����
					String content = oc.getHTML(new File(htmlName));
					//���ô������ݿ�Ķ���
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
		Map<String,Object> map = new HashMap<String,Object>();
		//��hash���еõ��������Ķ���
		ImportWatched watched = (ImportWatched)Content.MAP.get(taskId);
		//ͨ�������������ʼ����������
		ImportWatcher w = new ImportWatcher(watched);
		//�õ�����������״̬
		w.update(watched, "");
		//��������������Hash�����������������
		if (watched.isDone()){
			Content.MAP.remove(taskId);
			System.out.println("�����б��Ƴ�:" + (Content.MAP.get(taskId) == null));
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
