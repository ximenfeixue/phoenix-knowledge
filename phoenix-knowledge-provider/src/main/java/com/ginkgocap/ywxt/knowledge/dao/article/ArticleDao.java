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
     * 得到此用户在此文章分类sortId下的文章数量(包括回收站的文章)
     * @param uid
     * @param sortId
     * @return
     */
    long selectCountBySortId(long uid,String sortId);
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
     * 根据条件查询
     * @param uid 
     * @param keywords 查询关键字
     * @param start 分页开始行
     * @param size 行大小
     * @return
     */
    List<Article> selectByParams(long uid,String keywords,Integer start,Integer size);
    /**
     * 根据条件查询后得查询后的文章总记录数
     * @param uid 
     * @param keywords 查询关键字
     */
    Long selectByParamsCount(long uid,String keywords);
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
     * 通过条件取到从start开始到end结束条文章
     * @param uid 
     * @param recycleBin 
     * @param essence 
     * @param sortId 
     * @param start 从地start条开始
     * @param size  读取size条信息
     * @return List
     */
	List<Article> selectArticleList(long uid, String recycleBin,String essence, String sortId, Integer start, Integer size);
	 /**
     * 取到selectArticleList方法通过条件查询的文章总共条数
     * @param uid 
     * @param recycleBin 
     * @param essence 
     * @param sortId 
     * @param start 从地start条开始
     * @param size  读取size条信息
     * @return List
     */
	long selectArticleListCount(long uid, String recycleBin, String essence,String sortId);
}
