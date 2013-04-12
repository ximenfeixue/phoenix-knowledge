package com.ginkgocap.ywxt.knowledge.service.article;

import java.util.List;

import java.util.Map;

import com.ginkgocap.ywxt.knowledge.form.DataGridModel;
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
     * 返回查询列表并带分页信息
     * @param uid 文章发布人登陆框的ID
     * @param keywords 查询的关键字
     * @param dgm  分页信息
     */
    Map<String,Object> selectListByParam(long uid,String keywords, DataGridModel dgm);
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
     * 通过条件取到从文章列表
     * @param uid 
     * @param recycleBin 
     * @param essence 
     * @param sortId 
     * @param dgm 分页信息
     * @return Map<String, Object>
     */
	Map<String, Object> selectByParam(long uid, String recycleBin,String essence, String sortId,DataGridModel dgm);
    /**
     * 通过文章ID导出此文章生成Doc并下载
     * @param id 
     * @return String
     */
	String exportArticleById(long id);
    /**
     * 通过条件取到从文章列表条数
     * @param uid 
     * @param recycleBin 
     * @param essence 
     * @param sortId 
     * @param pageIndex 当前页
     * @param pageSize  分页大小
     * @return PageUtil
     */
	PageUtil articleCount(long uid, String essence, String recycleBin, String sortId,
			int pageIndex, int pageSize);
    /**
     * 通过条件取到从文章列表
     * @param uid 
     * @param recycleBin 
     * @param essence 
     * @param sortId 
     * @param pageIndex 当前页
     * @param pageSize  分页大小
     * @return PageUtil
     */
	List<Article> articlelist(long uid, String essence, String recycleBin,
			String sortId, int pageIndex, int pageSize);
    /**
     * 通过关键字查询
     * @param uid 
     * @param keywords 关键字 
     * @param pageIndex 当前页
     * @param pageSize  分页大小
     * @return PageUtil
     */
	PageUtil count(long uid, String keywords, int pageIndex, int pageSize);
    /**
     * 通过关键字查询
     * @param uid 
     * @param keywords 关键字 
     * @param pageIndex 当前页
     * @param pageSize  分页大小
     * @return List<Article>
     */
	List<Article> list(long uid, String keywords, int pageIndex, int pageSize);
}
