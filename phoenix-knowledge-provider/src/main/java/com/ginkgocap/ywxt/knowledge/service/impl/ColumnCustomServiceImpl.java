package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.model.ColumnCustom;
import com.ginkgocap.ywxt.knowledge.service.IColumnCustomService;
import com.ginkgocap.ywxt.user.model.User;

public class ColumnCustomServiceImpl implements IColumnCustomService {

	@Override
	public void init(long userid, long gtnid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ColumnCustom queryListByCidAndUserId(long userid, long cid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ColumnCustom> queryListByPidAndUserId(long userid, long pid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long countListByPidAndUserId(long userid, Long pid) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delByUserIdAndColumnId(long userid, long cid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void del(long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> queryHomeColumn(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ColumnCustom> queryListByPidAndUserIdAndState(long userid,
			long cid, short state) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateColumnViewStatus(long id, short viewStatus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(ColumnCustom columnCustom) {
		// TODO Auto-generated method stub
		
	}

}
