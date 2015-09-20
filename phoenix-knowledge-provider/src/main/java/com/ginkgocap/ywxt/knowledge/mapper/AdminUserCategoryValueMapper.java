package com.ginkgocap.ywxt.knowledge.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ginkgocap.ywxt.knowledge.model.AdminUserCategory;

/**
 * 左树dao接口
 * <p>
 * 于2014-9-18 由 bianzhiwei 创建
 * </p>
 * 
 * @author <p>
 *         当前负责人 bianzhiwei
 *         </p>
 * 
 */
public interface AdminUserCategoryValueMapper {

	/**
	 * 通过phoenix_user.tb_user.id得到此用户sortId下经过树形结构排序的所有分类
	 * 
	 * @param uid
	 * @param sortId
	 * @return
	 */
	List<AdminUserCategory> selectChildBySortId(@Param("uid") long uid,@Param("sortId") String sortId, @Param("type") Byte type);

	void insertUserCategory(AdminUserCategory adminUserCategory);

	public AdminUserCategory selectByPrimaryKey(long id);

	void updateByPrimaryKey(AdminUserCategory adminUserCategory);
	
	int updateUseType(@Param("userId") long userid,@Param("categoryType") int categoryType,@Param("sortid") String sortid,@Param("usetype") int userType);
	
	List<AdminUserCategory> selectUserCategory(@Param("uid") long userid,@Param("pid") long pid,@Param("categoryType") int type);
	
	void batchUpdateKnowledgeStatus(@Param("ids") List<Long> ids,@Param("status") int status);

}