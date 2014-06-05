package com.ginkgocap.ywxt.knowledge.dao.article;

import java.util.List;

import com.ginkgocap.ywxt.knowledge.model.Article;
/**
 * 文章管理分类的Dao接口
 * @author lk
 * @创建时间：2013-03-29 10:40
 */
public interface ArticleDao {
    /**
     * 通过主键获得文章记录
     * @param id
     * @return
     */
    Article selectByPrimaryKey(long id);
	 /**
     * 得到此用户在此文章分类ID下的文章数量(包括回收站的文章)
     * @param categoryid
     * @return
     */
    long selectCountByCategoryId(long categoryid);
    /**
     * 插入文章
     * @param article
     * @return
     */
    Article insert(Article article);
    /**
     * 修改文章
     * @param article
     */
    void update(Article article);
    /**
     * 删除文章
     * @param id
     */
    void delete(long id);
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
     * @param sortId
     * @param recycleBin 
     * @param essence 
     */
	List<Article> articleAllListBySortId(long uid, String sortId,String recycleBin, String essence);
    /**
     * 文章列表
     * @param uid 
     * @param sortId 分类配需ID
     * @param essence 是否为精华
     * @param recycleBin 是否回收站 
     * @param keywords 搜索关键字
     * @return long
     */
	long selectByParamsCount(long uid, String sortId, String essence,String recycleBin, String keywords);
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
	List<Article> selectByParams(long uid, String sortId, String essence,String recycleBin, String keywords, String sort,int pageIndex, int pageSize);
    /**
     * 批量删除文章
     * @param ids
     */
	void deleteArticles(String[] ids);
    /**
     * 删除某用户下的回收站文章
     * @param uid
     */
	void cleanRecyle(long uid);
	long selectRecyleNum(long uid);
	
	List<Article> relationList(long uid, long ralatoinid, String sort,
			int pageIndex, int pageSize);
	/**
     * 文章列表
     * @param uid 
     * @param articleType null：取全部；0：去正文类型；1：取url类型
     * @param sortId 分类配需ID
     * @param essence 是否为精华
     * @param recycleBin 是否回收站 
     * @param keywords 搜索关键字
     * @return long
     */
	long selectByParamsCount(long uid, String articleType,String sortId, String essence,String recycleBin, String keywords);
	/**
     * 得到文章列表的文章数量
     * @param uid 
     * @param articleType null：取全部；0：去正文类型；1：取url类型
     * @param sortId 分类配需ID
     * @param essence 是否为精华
     * @param recycleBin 是否回收站 
     * @param keywords 搜索关键字
     * @param pageIndex 当前页
     * @param pageSize  分页大小
     * @return List<Article>
     */
	List<Article> selectByParams(long uid,String articleType, String sortId, String essence,String recycleBin, String keywords, String sort,int pageIndex, int pageSize);

	/**
	 * 查询articleType为1的 文章是否存在
	 * @param url 文章url
	 * @param uid 用户uid  不是id
	 * @return true:已存在;false:不存在
	 */
	boolean checkArticleIsExist(String url,long uid);
}
