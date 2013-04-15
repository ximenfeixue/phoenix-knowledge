package com.ginkgocap.ywxt.knowledge.dao.category.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.category.CategoryDao;
import com.ginkgocap.ywxt.knowledge.model.Category;
import com.ibatis.sqlmap.client.SqlMapClient;

@Component("categoryDao")
public class CategoryDaoImpl extends SqlMapClientDaoSupport implements
		CategoryDao {
	@Autowired
	SqlMapClient sqlMapClient;

	@PostConstruct
	public void initSqlMapClient() {
		super.setSqlMapClient(sqlMapClient);
	}

	@Override
	public Category selectByPrimaryKey(long id) {
		Category category = (Category) getSqlMapClientTemplate().queryForObject("tb_category.selectByPrimaryKey", id);
		return category;
	}

	@Override
	public List<Category> selectTreeOfSortByUserid(long uid, String state) {
		if (uid <=0)return null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		if(StringUtils.isNotBlank(state))
        	map.put("state", state);
		List<Category> list = getSqlMapClientTemplate().queryForList("tb_category.selectTreeOfSortByUserid", map);
		return list;
	}

	@Override
	public Category insert(Category category) {
		try {
			Long id = (Long) getSqlMapClientTemplate().insert("tb_category.insert", category);
			if (id != null) {
				category.setId(id);
				return category;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void update(Category category) {
		getSqlMapClientTemplate().update("tb_category.update", category);
	}

	@Override
	public void delete(long id) {
		getSqlMapClientTemplate().delete("tb_category.delete", id);
	}

	@Override
	public Category selectBySortId(long uid, String sortId) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (uid <= 0)return null;
		map.put("uid", uid);
		if(StringUtils.isNotBlank(sortId))
        	map.put("sortId", sortId);
		return (Category)getSqlMapClientTemplate().queryForObject("tb_category.selectBySortId",map);
	}

	@Override
	public long selectChildCountById(long id) {
		return (Long)getSqlMapClientTemplate().queryForObject(
				"tb_category.selectChildCountById", id);
	}

	@Override
	public String selectMaxSortId(long uid, String parentSortId) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (uid <= 0)return null;
		map.put("uid", uid);
		map.put("parentSortId", parentSortId + "_________");
		return (String)getSqlMapClientTemplate().queryForObject("tb_category.selectMaxSortId",map);
	}

	@Override
	public List<Category> selectChildBySortId(long uid, String sortId) {
		if (uid <=0)return null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("sortId", sortId);
		List<Category> list = getSqlMapClientTemplate().queryForList("tb_category.selectChildBySortId", map);
		return list;
	}

}
