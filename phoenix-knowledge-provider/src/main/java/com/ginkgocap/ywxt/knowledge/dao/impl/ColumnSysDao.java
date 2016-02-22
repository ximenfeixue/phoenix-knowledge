package com.ginkgocap.ywxt.knowledge.dao.impl;

import java.util.List;

import com.ginkgocap.parasol.common.service.exception.BaseServiceException;
import com.ginkgocap.parasol.common.service.impl.BaseService;
import com.ginkgocap.ywxt.knowledge.dao.IColumnSysDao;
import com.ginkgocap.ywxt.knowledge.model.ColumnSys;


public class ColumnSysDao extends BaseService<ColumnSys> implements IColumnSysDao {

	@Override
	public List<ColumnSys> selectByUserId(Long userId) throws Exception {
		// TODO Auto-generated method stub
		if(userId!=null){
			return this.getEntitys("ColumnSysDao_selectByUserId", userId);
		}
		return null;
	}
	
}