/**
 * @title UserCategoryDAOImpl.java 
 * @description     
 * @create             2015-11-10 下午5:17:01 By maoyun
 * @package         com.ginkgocap.ywxt.knowledge.dao.usercategory.impl
 * @copyright         Copyright (c) 2011-2012      
 * @version         $Id$
 * NUBB-Java-Project
 */
package com.ginkgocap.ywxt.knowledge.dao.usercategory.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.usercategory.UserCategoryDAO;
import com.ginkgocap.ywxt.knowledge.entity.UserCategory;
import com.ginkgocap.ywxt.knowledge.entity.UserCategoryExample;
import com.ginkgocap.ywxt.knowledge.entity.UserCategoryExample.Criteria;
import com.ginkgocap.ywxt.knowledge.mapper.UserCategoryMapper;

/**
 * @Title: UserCategoryDAOImpl.java
 * @Package com.ginkgocap.ywxt.knowledge.dao.usercategory.impl 
 * @Description:
 * @author caihe
 * @date 2015-11-10 下午5:17:01
 */
@Component("userCategoryDAO")
public class UserCategoryDAOImpl  implements UserCategoryDAO{

	@Resource
	private UserCategoryMapper userCategoryMapper;
	/* (non-Javadoc)
	 * @see com.ginkgocap.ywxt.knowledge.dao.usercategory.UserCategoryDAO#selectUserCategoryByPid(long, long, int, int)
	 * Administrator
	 */
	@Override
	public List<UserCategory> selectUserCategoryByPid(long userId, long pid, int page, int size) {
		UserCategoryExample example = new UserCategoryExample();
		Criteria c = example.createCriteria();
		c.andUserIdEqualTo(userId);
		c.andParentIdEqualTo(pid);
		c.andCategoryTypeEqualTo((short) 0);
		example.setOrderByClause("createtime desc");
		example.setLimitStart(page);
		example.setLimitEnd(size);
		return userCategoryMapper.selectByExample(example);
	}

	/* (non-Javadoc)
	 * @see com.ginkgocap.ywxt.knowledge.dao.usercategory.UserCategoryDAO#selectCategoryCountByPid(long, long)
	 * Administrator
	 */
	@Override
	public int selectCategoryCountByPid(long userId, long pid) {
		UserCategoryExample example = new UserCategoryExample();
		Criteria c = example.createCriteria();
		c.andUserIdEqualTo(userId);
		c.andParentIdEqualTo(pid);
		c.andCategoryTypeEqualTo((short) 0);
		return userCategoryMapper.countByExample(example);
	}

}
