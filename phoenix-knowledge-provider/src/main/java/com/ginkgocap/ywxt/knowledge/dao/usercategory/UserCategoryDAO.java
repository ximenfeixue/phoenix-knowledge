package com.ginkgocap.ywxt.knowledge.dao.usercategory;

import java.util.Date; 
import java.util.List;

import com.ginkgocap.ywxt.knowledge.entity.UserCategory;
 

public interface UserCategoryDAO {
	
	
	/**
	 * 通过PID分页查询用户目录
	 * @param userId
	 * @param pid
	 * @param page
	 * @param size
	 * @return
	 */
	List<UserCategory> selectUserCategoryByPid(long userId,long pid,int page,int size);
	
	/**
	 * 通过PID分页查询用户目录
	 * @param userId
	 * @param pid
	 * @param page
	 * @param size
	 * @return
	 */
	int selectCategoryCountByPid(long userId,long pid);
}
