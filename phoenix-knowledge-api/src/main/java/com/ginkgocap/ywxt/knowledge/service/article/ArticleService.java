package com.ginkgocap.ywxt.knowledge.service.article;

import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.file.model.FileIndex;
import com.ginkgocap.ywxt.knowledge.model.Article;
import com.ginkgocap.ywxt.util.PageUtil;

/**
 * 文章管理的service接口
 * @author lk
 * @创建时间：2013-03-29 10:40
 */
public interface ArticleService {
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
     */
	Map<String,Object> exportFileBySortId(long uid,String sortId,String taskId,String recycleBin,String essence);
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
	
	Map<String, Object> importFileBySortId(long uid, long categoryId,List<FileIndex> fileList, String taskId);
}
