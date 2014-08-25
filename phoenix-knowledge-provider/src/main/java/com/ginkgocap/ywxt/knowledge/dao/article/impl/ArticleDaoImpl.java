package com.ginkgocap.ywxt.knowledge.dao.article.impl;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.article.ArticleDao;
import com.ginkgocap.ywxt.knowledge.model.Article;

@Component("articleDao")
public class ArticleDaoImpl implements ArticleDao {

	@Override
	public Article selectByPrimaryKey(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long selectCountByCategoryId(long categoryid) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Article insert(Article article) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Article article) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateEssence(String essence, String[] ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateRecycleBin(String recycleBin, String[] ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Article> articleAllListBySortId(long uid, String sortId,
			String recycleBin, String essence) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long selectByParamsCount(long uid, String sortId, String essence,
			String recycleBin, String keywords) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Article> selectByParams(long uid, String sortId,
			String essence, String recycleBin, String keywords, String sort,
			int pageIndex, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteArticles(String[] ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cleanRecyle(long uid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long selectRecyleNum(long uid) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Article> relationList(long uid, long ralatoinid, String sort,
			int pageIndex, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long selectByParamsCount(long uid, String articleType,
			String sortId, String essence, String recycleBin, String keywords) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Article> selectByParams(long uid, String articleType,
			String sortId, String essence, String recycleBin, String keywords,
			String sort, int pageIndex, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
