package com.ginkgocap.ywxt.knowledge.dao.userpermission;

import java.util.List;

public interface UserPermissionDAO {

	// 查询知识ID
	List<Long> selectByreceive_user_id(long receive_user_id);
}
