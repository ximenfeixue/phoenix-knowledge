package com.ginkgocap.ywxt.knowledge.dao.usercategory.impl;

import java.util.HashMap; 
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Component;
import com.ginkgocap.ywxt.knowledge.dao.usercategory.UserCategoryDao;
import com.ginkgocap.ywxt.knowledge.model.UserCategory;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 用户知识目录左树dao实现类 
 * <p>于2014-9-16 由 bianzhiwei 创建 </p>
 * @author  <p>当前负责人 bianzhiwei</p>   
 *
 */
@Component("userCategoryDao")
public class UserCategoryDaoImpl extends SqlMapClientDaoSupport implements
		UserCategoryDao {
	@Autowired
	SqlMapClient sqlMapClient;

	@PostConstruct
	public void initSqlMapClient() {
		super.setSqlMapClient(sqlMapClient);
	}

	@Override
	public UserCategory selectByPrimaryKey(long id) {
		UserCategory category = (UserCategory) getSqlMapClientTemplate().queryForObject("tb_user_category.selectByPrimaryKey", id);
		return category;
	}

	@Override
	public UserCategory insert(UserCategory category) {
		try {
			Long id = (Long) getSqlMapClientTemplate().insert("tb_user_category.insert", category);
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
	public void update(UserCategory category) {
		getSqlMapClientTemplate().update("tb_user_category.update", category);
	}

	@Override
	public void delete(long id) {
		getSqlMapClientTemplate().delete("tb_user_category.delete", id);
	}

	@Override
	public long selectChildCountById(long id) {
		return (Long)getSqlMapClientTemplate().queryForObject(
				"tb_user_category.selectChildCountById", id);
	}

	@Override
	public String selectMaxSortId(long uid, String parentSortId) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (uid <= 0)return null;
		map.put("userId", uid);
		map.put("parentSortId", parentSortId + "_________");
		return (String)getSqlMapClientTemplate().queryForObject("tb_user_category.selectMaxSortId",map);
	}

	@Override
	public List<UserCategory> selectChildBySortId(long uid, String sortId) {
		if (uid <=0)return null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", uid);
		map.put("sortId", sortId);
		List<UserCategory> list = getSqlMapClientTemplate().queryForList("tb_user_category.selectChildBySortId", map);
		return list;
	}
}
