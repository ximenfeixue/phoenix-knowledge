package com.ginkgocap.ywxt.knowledge.service.article;

import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.file.model.FileIndex;
import com.ginkgocap.ywxt.knowledge.model.Article;
import com.ginkgocap.ywxt.util.PageUtil;
/**
 * 手机端调用文章管理的service接口
 * @author jc
 * @创建时间：2014-05-26 15:11
 */
public interface MobileArticleService {
	 /**
     * 通过主键获得文章
     * @param id
     * @return
     */
    Article selectByPrimaryKey(long id);
	 /**
     * 新增文章
     * @param article
     * @return
     */
    Article insert(Article article);
    /**
     * 删除文章
     * @param id
     */
    void delete(long id);
    /**
     * 修改文章
     * @param article
     */
    void update(Article article);
    /**
     * 根据多个文章id设置是否批量加精
     * @param essence 
     * @param ids
     */
    void updateEssence(String essence,String[] ids);
    /**
     * 根据多个文章id设置文章进入回收站或从回收站恢复
     * @param recycleBin 
     * @param ids
     */
    void updateRecycleBin(String recycleBin,String[] ids);
    /**
     * 批量删除文章
     * @param ids
     */
    void deleteArticles(String[] ids);
    /**
     * 通过文章ID导出此文章生成Doc并下载
     * @param id 
     * @return String
     */
	String exportArticleById(long id);
    /**
     * 通过条件取到从文章列表
     * @param uid 
     * @param sortId
     * @param recycleBin 
     * @param essence 
     */
	List<Article> articleAllListBySortId(long uid, String sortId, String recycleBin, String essence);
    /**
     * sortId得到所有文章并生成word文档提供下载
     * @param uid 
     * @param sortId
     * @param taskId
     * @param recycleBin 是否包括回收站的文章
     * @param essence
     * @param option
     */
	Map<String,Object> exportFileBySortId(long uid,String sortId,String taskId,String recycleBin,String essence,String option);
    /**
     * 导出文章时转换word文件的进度
     */
	Map<String,Object> processView(String taskId);
    /**
     * 将文本导入相应的categoryid分类下的数据库中
     * @param uid 
     * @param sortId
     * @param taskId 是否包括回收站的文章
     */
	Map<String, Object> importFileBySortId(long uid, long category,String taskId);
    /**
     * 导入文章到数据时的转换
     */
	String importProcess(String taskId);
    /**
     * 文章列表
     * @param uid 
     * @param sortId 分类配需ID
     * @param essence 是否为精华
     * @param recycleBin 是否回收站 
     * @param sort 排序字段名称
     * @param keywords 搜索关键字
     * @param pageIndex 当前页
     * @param pageSize  分页大小
     * @return List<Article>
     */
	PageUtil count(long uid, String sortId, String essence, String recycleBin, String keywords, int pageIndex, int pageSize);
    /**
     * 得到文章列表的文章数量
     * @param uid 
     * @param sortId 分类配需ID
     * @param essence 是否为精华
     * @param recycleBin 是否回收站 
     * @param keywords 搜索关键字
     * @param pageIndex 当前页
     * @param pageSize  分页大小
     * @return List<Article>
     */
	List<Article> list(long uid, String sortId, String essence,String recycleBin, String keywords,String sort, int pageIndex, int pageSize);
    /**
     * 将文章导入categoryid分类下
     * @param uid 
     * @param categoryId
     * @param fileList
     * @param taskId
     * @param essence
     * @param option
     * @return Map<String, Object>
     */
	Map<String, Object> importFileBySortId(long uid, long categoryId,List<FileIndex> fileList, String taskId);
    /**
     * 批量更新是否加精
     * @param ids
     * @param essence
     */
	void updateEssence(String[] ids, String essence);
    /**
     * 根据用户id清除此用户下回收站的文章
     * @param uid
     */
	void cleanRecyle(long uid);
    /**
     * 根据用户id得到此用户下回收站文章的数量
     * @param uid
     */
	long selectRecyleNum(long uid);
    /**
     * 相关文章
     * @param uid
     */
	List<Article> relationList(long uid, long ralatoinid,String sort,  int pageIndex, int pageSize);
	/**
	 * 
	 * @param uid user对象uid，不是id
	 * @param articleType null：取全部；0：去正文类型；1：取url类型
	 * @param sortId 分类排序ID
     * @param essence 是否为精华
     * @param recycleBin 是否回收站 
     * @param keywords 搜索关键字
     * @param pageIndex 当前页
     * @param pageSize  分页大小
	 * @return
	 */
	PageUtil count(long uid, String articleType,String sortId, String essence, String recycleBin, String keywords, int pageIndex, int pageSize);
	/**
	 * 
	 * @param uid user对象uid，不是id
	 * @param articleType null：取全部；0：去正文类型；1：取url类型
	 * @param sortId 分类排序ID
     * @param essence 是否为精华
     * @param recycleBin 是否回收站 
     * @param keywords 搜索关键字
     * @param pageIndex 当前页
     * @param pageSize  分页大小
	 * @return
	 */
	List<Article> list(long uid,String articleType, String sortId, String essence,String recycleBin, String keywords,String sort, int pageIndex, int pageSize);
	
	/**
	 * 
	 * @param id 文章id
	 * @param uid 用户uid  不是id
	 * @return true:已存在;false:不存在
	 */
	boolean checkArticleIsExist(long id,long uid);
}
