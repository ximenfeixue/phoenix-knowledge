package com.ginkgocap.ywxt.knowledge.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ginkgocap.parasol.common.service.exception.BaseServiceException;
import com.ginkgocap.parasol.common.service.impl.BaseService;
import com.ginkgocap.ywxt.knowledge.dao.IColumnCustomDao;
import com.ginkgocap.ywxt.knowledge.model.ColumnCustom;
import com.ginkgocap.ywxt.user.model.User;

@Repository("columnCustomDao")
public class ColumnCustomDao extends BaseService<ColumnCustom> implements IColumnCustomDao {


	@Override
	public List<ColumnCustom> queryListByPidAndUserId(long userid, long pid) throws Exception {
		// TODO Auto-generated method stub
		List<ColumnCustom> list= this.getEntitys("ColumnCustomDao_queryListByPidAndUserId", userid,pid);
		return list;
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
	public ColumnCustom insert(ColumnCustom columnCustom) throws Exception {
		// TODO Auto-generated method stub
		if(columnCustom==null){
			return null;
		}
		Long id=(Long)this.saveEntity(columnCustom);
		return this.queryById(id);
	}

	@Override
	public ColumnCustom queryById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return this.getEntity(id);
	}

	@Override
	public ColumnCustom update(ColumnCustom columnCustom) throws Exception {
		// TODO Auto-generated method stub
		if(columnCustom==null){
			return null;
		}
		boolean status=this.updateEntity(columnCustom);
		if(status){
			return this.queryById(columnCustom.getId());
		}else{
			return null;
		}
	}

	@Override
	public Boolean del(long id) throws Exception {
		// TODO Auto-generated method stub
		return this.deleteEntity(id);
	}

	@Override
	public ColumnCustom queryMaxCCByUid(long userid)
			throws Exception {
		// TODO Auto-generated method stub
		List<ColumnCustom> list= this.getEntitys("ColumnCustomDao_queryMaxCCByUid", userid);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	@Override
	public List<ColumnCustom> queryListByUserId(long userid) throws Exception {
		// TODO Auto-generated method stub
		return this.getEntitys("ColumnCustomDao_queryListByUserId", userid);
	}


	@Override
	public List<ColumnCustom> queryListByCidAndUserId(long userid, long cid)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}	
}