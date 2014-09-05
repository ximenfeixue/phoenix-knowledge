package com.ginkgocap.ywxt.knowledge.dao.article.impl;

import java.util.HashMap;


import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.article.ArticleDao;
import com.ginkgocap.ywxt.knowledge.model.Article;
import com.ibatis.sqlmap.client.SqlMapClient;

@Component("articleDao")
public class ArticleDaoImpl  extends SqlMapClientDaoSupport implements ArticleDao {
	
	@Autowired
	SqlMapClient sqlMapClient;

	@PostConstruct
	public void initSqlMapClient() {
		super.setSqlMapClient(sqlMapClient);
	}

	@Override
	public Article selectByPrimaryKey(long id) {
		Article article = (Article) getSqlMapClientTemplate().queryForObject("tb_article.selectByPrimaryKey", id);
		return article;
	}
	
	@Override
	public long selectCountByCategoryId(long categoryid) {
		return (Long)getSqlMapClientTemplate().queryForObject("tb_article.selectCountByCategoryId", categoryid);
	}

//	@Override
//	public long selectCountBySortId(long uid, String sortId) {
//		// TODO Auto-generated method stub
//		return 0;
//	}

	@Override
	public Article insert(Article article) {
		try {
			Long id = (Long) getSqlMapClientTemplate().insert("tb_article.insert", article);
			if (id != null) {
				article.setId(id);
				return article;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void update(Article article) {
		getSqlMapClientTemplate().update("tb_article.update",article);
	}

	@Override
	public void delete(long id) {
		getSqlMapClientTemplate().delete("tb_article.delete", id);
	}

//	@Override
//	public List<Article> selectByParams(long uid, String keywords,
//			Integer pageIndex, Integer pageSize) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		long start = (pageIndex - 1) * pageSize;
//		long size = pageSize;
//		if (uid > 0)
//			map.put("uid", new Long(uid));
//		if(StringUtils.isNotBlank(keywords))
//        	map.put("keywords", keywords);
//        map.put("start", start);
//        map.put("size", size);
//        List<Article> list = getSqlMapClientTemplate().queryForList("tb_article.selectByParams", map);
//        return list;
//	}
//
//	@Override
//	public Long selectByParamsCount(long uid, String keywords) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		if (uid > 0)
//			map.put("uid", new Long(uid));
//		if(StringUtils.isNotBlank(keywords))
//        	map.put("keywords", keywords);
//		return (Long) getSqlMapClientTemplate().queryForObject("tb_article.selectCountByParams", map);
//	}

	@Override
	public void updateEssence(String essence, String[] ids) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("essence", essence);
		map.put("ids", ids);
		getSqlMapClientTemplate().update("tb_article.updateEssence",map);
	}

	@Override
	public void updateRecycleBin(String recycleBin, String[] ids) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("recycleBin", recycleBin);
		map.put("ids", ids);
		getSqlMapClientTemplate().update("tb_article.updateRecycleBin",map);
	}

//	@Override
//	public List<Article> selectArticleList(long uid, String recycleBin,
//			String essence, String sortId, Integer pageIndex, Integer pageSize) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		long start = (pageIndex - 1) * pageSize;
//		long size = pageSize;
//		if (uid > 0)
//			map.put("uid", new Long(uid));
//		if(StringUtils.isNotBlank(recycleBin))
//        	map.put("recycleBin", recycleBin);
//		if(StringUtils.isNotBlank(essence))
//        	map.put("essence", essence);
//		if(StringUtils.isNotBlank(sortId))
//        	map.put("sortId", sortId);
//		map.put("start", start);
//		map.put("size", size);
//		return (List<Article>)getSqlMapClientTemplate().queryForList("tb_article.selectArticleList",map);
//	}

//	@Override
//	public long selectArticleListCount(long uid, String recycleBin,
//			String essence, String sortId) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		if (uid > 0)
//			map.put("uid", new Long(uid));
//		if(StringUtils.isNotBlank(recycleBin))
//        	map.put("recycleBin", recycleBin);
//		if(StringUtils.isNotBlank(essence))
//        	map.put("essence", essence);
//		if(StringUtils.isNotBlank(sortId))
//        	map.put("sortId", sortId);
//		return (Long) getSqlMapClientTemplate().queryForObject("tb_article.selectArticleListCount", map);
//	}

	@Override
	public List<Article> articleAllListBySortId(long uid, String sortId,
			String recycleBin, String essence) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (uid > 0)
			map.put("uid", new Long(uid));
		if(StringUtils.isNotBlank(recycleBin))
        	map.put("recycleBin", recycleBin);
		if(StringUtils.isNotBlank(essence))
        	map.put("essence", essence);
		if(StringUtils.isNotBlank(sortId))
        	map.put("sortId", sortId);
		return (List<Article>)getSqlMapClientTemplate().queryForList("tb_article.articleAllListBySortId",map);
	}

	@Override
	public long selectByParamsCount(long uid, String sortId, String essence,String recycleBin, String keywords) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (uid > 0)
			map.put("uid", new Long(uid));
		if(StringUtils.isNotBlank(sortId))
        	map.put("sortId", sortId);
		if(StringUtils.isNotBlank(essence))
        	map.put("essence", essence);
		if(StringUtils.isNotBlank(recycleBin))
        	map.put("recycleBin", recycleBin);
		if(StringUtils.isNotBlank(keywords))
        	map.put("keywords", keywords);
		return (Long) getSqlMapClientTemplate().queryForObject("tb_article.selectListCount", map);
	}

	@Override
	public List<Article> selectByParams(long uid, String sortId, String essence, String recycleBin, String keywords,String sort, int pageIndex,
			int pageSize) {
		Map<String, Object> map = new HashMap<String, Object>();
		long start = (pageIndex - 1) * pageSize;
		long size = pageSize;
		if (uid > 0)
			map.put("uid", new Long(uid));
		if(StringUtils.isNotBlank(sortId))
        	map.put("sortId", sortId);
		if(StringUtils.isNotBlank(essence))
        	map.put("essence", essence);
		if(StringUtils.isNotBlank(recycleBin))
        	map.put("recycleBin", recycleBin);
		if(StringUtils.isNotBlank(keywords))
        	map.put("keywords", keywords);
		if(StringUtils.isNotBlank(keywords))
        	map.put("keywords", keywords);
		if(StringUtils.isNotBlank(sort)){
//			if ("clickNum".equalsIgnoreCase(sort)){
//				map.put("sort", "clickNum");
//			}else if ("pubdate".equalsIgnoreCase(sort)){
//				map.put("sort", "pubdate");
//			}else if ("modifyTime".equalsIgnoreCase(sort)){
//				map.put("sort", "modifyTime");
//			}else if ("id".equalsIgnoreCase(sort)){
//				map.put("sort", "articleId");
//			}
//			
			map.put("sort", sort);
		}
        map.put("start", start);
        map.put("size", size);
        List<Article> list = getSqlMapClientTemplate().queryForList("tb_article.selectList", map);
        return list;
	}

	@Override
	public void deleteArticles(String[] ids) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ids", ids);
		getSqlMapClientTemplate().update("tb_article.deleteArticles",map);
	}

	@Override
	public void cleanRecyle(long uid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		getSqlMapClientTemplate().update("tb_article.cleanRecyle",map);
	}

	@Override
	public long selectRecyleNum(long uid) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (uid > 0)
			map.put("uid", new Long(uid));
		return (Long) getSqlMapClientTemplate().queryForObject("tb_article.selectRecyleNum", map);
	}

	@Override
	public List<Article> relationList(long uid, long relationid, String sort,
			int pageIndex, int pageSize) {
		Map<String, Object> map = new HashMap<String, Object>();
		long start = (pageIndex - 1) * pageSize;
		long size = pageSize;
		if (uid > 0)
			map.put("uid", new Long(uid));
		if (relationid > 0)
			map.put("relationid", new Long(relationid));
		if(StringUtils.isNotBlank(sort))
        	map.put("sort", sort);
        map.put("start", start);
        map.put("size", size);
        List<Article> list = getSqlMapClientTemplate().queryForList("tb_article.relationList", map);
        return list;
	}
	@Override
	public long selectByParamsCount(long uid, String articleType,
			String sortId, String essence, String recycleBin, String keywords) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (uid > 0)
			map.put("uid", new Long(uid));
		if(StringUtils.isNotBlank(articleType))
        	map.put("articleType", articleType);
		if(StringUtils.isNotBlank(sortId))
        	map.put("sortId", sortId);
		if(StringUtils.isNotBlank(essence))
        	map.put("essence", essence);
		if(StringUtils.isNotBlank(recycleBin))
        	map.put("recycleBin", recycleBin);
		if(StringUtils.isNotBlank(keywords))
        	map.put("keywords", keywords);
		return (Long) getSqlMapClientTemplate().queryForObject("tb_article.selectListCount", map);
	}

	@Override
	public List<Article> selectByParams(long uid, String articleType,
			String sortId, String essence, String recycleBin, String keywords,
			String sort, int pageIndex, int pageSize) {
		Map<String, Object> map = new HashMap<String, Object>();
		long start = (pageIndex - 1) * pageSize;
		long size = pageSize;
		if (uid > 0)
			map.put("uid", new Long(uid));
		if(StringUtils.isNotBlank(sortId))
        	map.put("sortId", sortId);
		if(StringUtils.isNotBlank(essence))
        	map.put("essence", essence);
		if(StringUtils.isNotBlank(recycleBin))
        	map.put("recycleBin", recycleBin);
		if(StringUtils.isNotBlank(keywords))
        	map.put("keywords", keywords);
		if(StringUtils.isNotBlank(keywords))
        	map.put("keywords", keywords);
		if(StringUtils.isNotBlank(articleType))
        	map.put("articleType", articleType);
		if(StringUtils.isNotBlank(sort)){
//			if ("clickNum".equalsIgnoreCase(sort)){
//				map.put("sort", "clickNum");
//			}else if ("pubdate".equalsIgnoreCase(sort)){
//				map.put("sort", "pubdate");
//			}else if ("modifyTime".equalsIgnoreCase(sort)){
//				map.put("sort", "modifyTime");
//			}else if ("id".equalsIgnoreCase(sort)){
//				map.put("sort", "articleId");
//			}
//			
			map.put("sort", sort);
		}
        map.put("start", start);
        map.put("size", size);
        List<Article> list = getSqlMapClientTemplate().queryForList("tb_article.selectList", map);
        return list;
	}

	@Override
	public boolean checkArticleIsExist(String url, long uid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("url", url); 
        map.put("uid", uid);
		Long count=(Long)getSqlMapClientTemplate().queryForObject("tb_article.checkArticleIsExist", map);
		if(count>0){
			return true;
		}else{
			return false;
		}
		
	}
}
