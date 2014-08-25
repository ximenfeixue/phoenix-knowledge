package com.ginkgocap.ywxt.knowledge.dao.category.impl;

import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.category.CategoryDao;
import com.ginkgocap.ywxt.knowledge.model.Category;

@Component("categoryDao")
public class CategoryDaoImpl implements
		CategoryDao {

	@Override
	public Category selectByPrimaryKey(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Category> selectTreeOfSortByUserid(long uid, String state) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Category insert(Category category) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Category category) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Category selectBySortId(long uid, String sortId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long selectChildCountById(long id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String selectMaxSortId(long uid, String parentSortId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Category> selectChildBySortId(long uid, String sortId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Category> findByParam(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

}
