package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.IColumnCustomDao;
import com.ginkgocap.ywxt.knowledge.dao.IColumnSysDao;
import com.ginkgocap.ywxt.knowledge.model.ColumnCustom;
import com.ginkgocap.ywxt.knowledge.model.ColumnSys;
import com.ginkgocap.ywxt.knowledge.service.IColumnCustomService;
import com.ginkgocap.ywxt.user.model.User;

@Service("columnCustomService")
public class ColumnCustomServiceImpl implements IColumnCustomService {
	
	private final static Logger logger = LoggerFactory.getLogger(ColumnCustomServiceImpl.class);

	@Resource
	private IColumnCustomDao columnCustomDao;
	
	@Resource
	private IColumnSysDao columnSysDao;
	
	private void delByUserId(Long userid){
		try {
			//删除
			List<ColumnCustom> list=this.columnCustomDao.queryListByUserId(userid);
			if(list!=null&&list.size()>0){
				for(ColumnCustom cc:list){
					cc.setDelStatus((short)1);
					this.update(cc);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("数据访问层异常", e);
		}
		
	}
	
	private ColumnCustom buidCC(ColumnSys cs,Long userId){
		if(cs==null){
			return null;
		}
		ColumnCustom cc=new ColumnCustom();
		cc.setUserId(userId);
		cc.setCid(cs.getId());
		cc.setColumnLevelPath(cs.getColumnLevelPath());
		cc.setColumnName(cs.getColumnName());
		cc.setCreateTime(new Date());
		cc.setPathName(cs.getPathName());
		cc.setOrderNum(0);
		cc.setPcid(cs.getParentId());
		cc.setTopColType(cs.getType());
		cc.setUpdateTime(new Date());
		cc.setUserOrSystem((short)0);
		return cc;
	}
	
	
	@Override
	public void init(long userid, long gtnid) {
		// TODO Auto-generated method stub
		//this.columnCustomDao.
		this.delByUserId(userid);
		try {
			List<ColumnSys> list=this.columnSysDao.queryListByUserId(userid);
			if(list!=null&&list.size()>0){
				for(ColumnSys cs:list){
					ColumnCustom cc=this.buidCC(cs, userid);
					this.insert(cc, userid);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("数据访问层异常", e);
		}
		
	}

	@Override
	public ColumnCustom queryListByCidAndUserId(long userid, long cid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ColumnCustom> queryListByPidAndUserId(long userid, long pid) {
		// TODO Auto-generated method stub
		try {
			List<ColumnCustom> list1=this.columnCustomDao.queryListByPidAndUserId(userid, pid);
			if(pid==0&&(list1==null||list1.size()==0)){
				this.init(userid, pid);
				return this.columnCustomDao.queryListByPidAndUserId(userid, pid);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("数据访问层异常", e);
		}
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
		try {
			this.columnCustomDao.update(columnCustom);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("数据访问层异常", e);
		}
	}


	@Override
	public ColumnCustom insert(ColumnCustom columnCustom, Long uid)
			throws Exception {
		// TODO Auto-generated method stub
		columnCustom.setUserId(uid);
		return this.columnCustomDao.insert(columnCustom);
	}

}
