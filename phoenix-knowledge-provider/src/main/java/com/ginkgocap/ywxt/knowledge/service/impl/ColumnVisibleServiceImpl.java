package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.entity.Column;
import com.ginkgocap.ywxt.knowledge.entity.ColumnExample;
import com.ginkgocap.ywxt.knowledge.entity.ColumnExample.Criteria;
import com.ginkgocap.ywxt.knowledge.entity.ColumnVisible;
import com.ginkgocap.ywxt.knowledge.entity.ColumnVisibleExample;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnMapper;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnValueMapper;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnVisibleMapper;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnVisibleValueMapper;
import com.ginkgocap.ywxt.knowledge.model.ColumnVO;
import com.ginkgocap.ywxt.knowledge.service.ColumnService;
import com.ginkgocap.ywxt.knowledge.service.ColumnVisibleService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.user.model.User;

@Service("columnVisibleService")
public class ColumnVisibleServiceImpl implements ColumnVisibleService {

	@Autowired
	ColumnService columnService;
	@Autowired
	ColumnVisibleMapper columnVisibleMapper;
	@Autowired
	ColumnVisibleValueMapper columnVisibleValueMapper;
	@Autowired
	ColumnValueMapper columnValueMapper;
	// 父级ID
	private final static long parentId = 0l;

	@Resource
	private ColumnMapper columnMapper;

	@Override
	public List<ColumnVisible> queryListByPidAndUserId(long userid, long cid) {
		ColumnVisibleExample cm = new ColumnVisibleExample();
		cm.createCriteria().andUserIdEqualTo(userid).andPcidEqualTo(cid);
		cm.setOrderByClause("column_id ");
		List<ColumnVisible> cs = columnVisibleMapper.selectByExample(cm);
		if (cs != null) {
			return cs;
		}
		return null;
	}

	@Override
	public List<ColumnVisible> queryListByPidAndUserIdAndState(long userid, long cid, short state) {
		ColumnVisibleExample cm = new ColumnVisibleExample();
		cm.createCriteria().andUserIdEqualTo(userid).andPcidEqualTo(cid).andStateEqualTo(state);
		cm.setOrderByClause("column_id ");
		List<ColumnVisible> cs = columnVisibleMapper.selectByExample(cm);
		if (cs != null) {
			return cs;
		}
		return null;
	}

	public ColumnVisible queryListBycidAndUserIdAndState(long userid, long cid, short state) {
		ColumnVisibleExample cm = new ColumnVisibleExample();
		cm.createCriteria().andUserIdEqualTo(userid).andColumnIdEqualTo(cid).andStateEqualTo(state);
		List<ColumnVisible> cs = columnVisibleMapper.selectByExample(cm);
		if (cs != null && cs.size() > 0) {
			return cs.get(0);
		}
		return null;
	}

	@Override
	public ColumnVisible queryListByCidAndUserId(long userid, long cid) {
		ColumnVisibleExample cm = new ColumnVisibleExample();
		cm.createCriteria().andUserIdEqualTo(userid).andColumnIdEqualTo(cid);
		List<ColumnVisible> cs = columnVisibleMapper.selectByExample(cm);
		if (cs != null && cs.size() == 1) {
			return cs.get(0);
		}
		return null;
	}

	@Override
	public void updateCids(long userid, String cids, long pcid) {
		String[] ids = cids.split(",");
		List<Long> idl = new ArrayList<Long>();
		for (String id : ids) {
			try {
				long c = Long.parseLong(id);
				idl.add(c);
			} catch (Exception e) {
			}
		}
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("instate", 0);
		m.put("pcid", pcid);
		m.put("utime", new Date());
		m.put("userId", userid);
		m.put("state", 0);
		List<String> ls1 = new ArrayList<String>();
		List<Long> idl3 = new ArrayList<Long>();
		List<String> ls3 = new ArrayList<String>();
		if (idl != null && idl.size() > 0) {
			for (long l : idl) {
				if (l <= 11) {
					ColumnVisibleExample example = new ColumnVisibleExample();
					example.createCriteria().andUserIdEqualTo(userid).andPcidEqualTo(l).andStateEqualTo((short) 0);
					int count = columnVisibleMapper.countByExample(example);
					if (count == 0) {
						idl3.add(l);
					}
				}
			}
			m.put("listl", idl);
			ls1 = columnVisibleValueMapper.selectSortIds(m);
			// 勾选父目录
			choiseParent(userid, pcid, 0);
		} else {// 全部取消
			idl.add(-1l);
			m.put("listl", idl);
			// 取消父目录
			choiseParent(userid, pcid, 1);
		}
		List<Long> idnl = columnVisibleValueMapper.selectNotinIds(m);
		List<String> ls2 = new ArrayList<String>();
		if (idnl != null && idnl.size() > 0) {
			m.put("listl", idnl);
			ls2 = columnVisibleValueMapper.selectSortIds(m);
		}
		if (ls1 != null && ls1.size() > 0) {
			m.put("listl", ls1);
			if (pcid == 0) {
				columnVisibleValueMapper.updateOneLevelTrue(m);
			} else {
				columnVisibleValueMapper.update(m);
			}
		}
		if (idl3 != null && idl3.size() > 0) {
			m.put("listl", idl3);
			ls3 = columnVisibleValueMapper.selectSortIds(m);
			m.put("listl", ls3);
			columnVisibleValueMapper.update(m);
		}
		m.put("instate", 1);
		m.put("state", 1);
		if (ls2 != null && ls2.size() > 0) {
			m.put("listl", ls2);
			columnVisibleValueMapper.update(m);
		}
	}

	@SuppressWarnings("null")
	private void choiseParent(long userid, long pcid, int i) {
		if (pcid == 0) {
			return;
		}
		ColumnVisible l = this.queryListBycidAndUserIdAndState(userid, pcid, (short) i);
		if (l == null) {
			short seti;
			if (i == 0) {
				seti = 1;
			} else {
				seti = 0;
			}
			ColumnVisible c = this.queryListBycidAndUserIdAndState(userid, pcid, seti);
			c.setState((short) i);
			ColumnVisibleExample example = new ColumnVisibleExample();
			example.createCriteria().andUserIdEqualTo(userid).andColumnIdEqualTo(pcid);
			columnVisibleMapper.updateByExampleSelective(c, example);
		}
	}

	@Override
	public void del(long id) {
		columnVisibleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public long countListByPidAndUserId(long userid, Long pid) {
		ColumnVisibleExample cm = new ColumnVisibleExample();
		com.ginkgocap.ywxt.knowledge.entity.ColumnVisibleExample.Criteria cri = cm.createCriteria();
		cri.andUserIdEqualTo(userid);
		if (pid != null) {
			cri.andPcidEqualTo(pid);
		}
		long cs = columnVisibleMapper.countByExample(cm);
		return cs;
	}

	// 新栏目
	@Override
	public long countListByCidAndUserId(long userid, Long cid) {
		ColumnVisibleExample cm = new ColumnVisibleExample();
		com.ginkgocap.ywxt.knowledge.entity.ColumnVisibleExample.Criteria cri = cm.createCriteria();
		cri.andUserIdEqualTo(userid);
		if (cid != null) {
			cri.andColumnIdEqualTo(cid);
		}
		long cs = columnVisibleMapper.countByExample(cm);
		return cs;
	}

	@Override
	public void init(long userid, long gtnid) {
		List<Column> l = columnValueMapper.selectByParams(null, Constants.gtnid, userid);
		List<ColumnVisible> cvl = new ArrayList<ColumnVisible>();
		for (Column c : l) {
			if (!c.getColumnname().equals("未分组")) {
				long id = c.getId();
				long pcid = c.getParentId();
				String cname = c.getColumnname();
				ColumnVisible cv = new ColumnVisible();
				cv.setColumnId(id);
				cv.setPcid(pcid);
				cv.setUserId(userid);
				cv.setCtime(new Date());
				cv.setUtime(new Date());
				cv.setSortId(c.getColumnLevelPath());
				cv.setColumnName(cname);
				cv.setState((short) 0);
				cvl.add(cv);
			}
		}
		if (cvl.size() > 0) {// 批量插入
			columnVisibleValueMapper.init(cvl);
		}
	}

	@Override
	public void initOne(long userid, long gid, Long cid) {
		List<Column> l = columnValueMapper.selectBySortId("00000000" + cid, Constants.gtnid, userid);
		List<ColumnVisible> cvl = new ArrayList<ColumnVisible>();
		for (Column c : l) {
			if (!c.getColumnname().equals("未分组")) {
				long id = c.getId();
				long pcid = c.getParentId();
				String cname = c.getColumnname();
				ColumnVisible cv = new ColumnVisible();
				cv.setColumnId(id);
				cv.setPcid(pcid);
				cv.setUserId(userid);
				cv.setCtime(new Date());
				cv.setUtime(new Date());
				cv.setSortId(c.getColumnLevelPath());
				cv.setColumnName(cname);
				cv.setState((short) 0);
				cvl.add(cv);
			}
		}
		if (cvl.size() > 0) {// 批量插入
			columnVisibleValueMapper.init(cvl);
		}
	}

	@Override
	public void saveCid(long userid, long cid) {
		Column c = checkExist(userid, cid);
		if (c == null) {
			return;
		}
		long id = c.getId();
		long pcid = c.getParentId();
		String cname = c.getColumnname();
		ColumnVisible cv = new ColumnVisible();
		cv.setColumnId(id);
		cv.setPcid(pcid);
		cv.setUserId(userid);
		cv.setCtime(new Date());
		cv.setUtime(new Date());
		cv.setColumnName(cname);
		cv.setSortId(c.getColumnLevelPath());
		cv.setState((short) 0);
		cv.setType(1);
		columnVisibleMapper.insert(cv);
	}

	private Column checkExist(long userid, long cid) {
		Column c = columnService.queryById(cid);
		if (c == null) {
			return null;
		} else {
			String length = c.getColumnLevelPath();
			if (length.length() > 18) {
				return null;
			}
		}
		return c;
	}

	@Override
	public void initDiff(long userId) {
		if (columnVisibleValueMapper.initcount(userId) >= 1) {
			List<Column> l = columnVisibleValueMapper.initvisible(userId);
			List<ColumnVisible> cvl = new ArrayList<ColumnVisible>();
			for (Column c : l) {
				if (!c.getColumnname().equals("未分组")) {
					long id = c.getId();
					long pcid = c.getParentId();
					String cname = c.getColumnname();
					ColumnVisible cv = new ColumnVisible();
					cv.setColumnId(id);
					cv.setPcid(pcid);
					cv.setUserId(userId);
					cv.setCtime(new Date());
					cv.setUtime(new Date());
					cv.setSortId(c.getColumnLevelPath());
					cv.setColumnName(cname);
					cv.setState((short) 0);
					cvl.add(cv);
				}
			}
			if (cvl.size() > 0) {// 批量插入
				columnVisibleValueMapper.init(cvl);
			}
		}
	}

	@Override
	public void delByUserIdAndColumnId(long userid, long cid) {
		Column c = checkExist(userid, cid);
		if (c == null) {
			return;
		}
		ColumnVisibleExample cm = new ColumnVisibleExample();
		com.ginkgocap.ywxt.knowledge.entity.ColumnVisibleExample.Criteria cri = cm.createCriteria();
		cri.andUserIdEqualTo(userid);
		cri.andColumnIdEqualTo(cid);
		columnVisibleMapper.deleteByExample(cm);
		if (userid == 0) {
			cm.clear();
			cm.createCriteria().andColumnIdEqualTo(c.getId());
			columnVisibleMapper.deleteByExample(cm);
		}
	}

	@Override
	public void saveOrUpdate(Column c) {
		if (c.getColumnLevelPath().length() >= 18) {
			return;
		}
		Long id = c.getId();
		long pcid = c.getParentId();
		long uid = c.getUserId();
		String cname = c.getColumnname();
		if (id == null) {// 新增栏目
			ColumnVisible cv = new ColumnVisible();
			cv.setColumnId(id);
			cv.setPcid(pcid);
			cv.setUserId(uid);
			cv.setCtime(new Date());
			cv.setUtime(new Date());
			cv.setColumnName(cname);
			cv.setSortId(c.getColumnLevelPath());
			cv.setState((short) 0);
			columnVisibleMapper.insert(cv);
		} else {// 修改栏目
			ColumnVisible cv = new ColumnVisible();
			cv.setColumnName(cname);
			cv.setUtime(new Date());
			ColumnVisibleExample e = new ColumnVisibleExample();
			e.createCriteria().andUserIdEqualTo(c.getUserId()).andColumnIdEqualTo(c.getId());
			columnVisibleMapper.updateByExampleSelective(cv, e);
		}

	}

	@Override
	public Map<String, Object> queryHomeColumn(User user) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<ColumnVO> list = new ArrayList<ColumnVO>();
		ColumnVO vo = null;
		if (user == null) {
			// 查询栏目表
			ColumnExample example = new ColumnExample();
			Criteria criteria = example.createCriteria();
			criteria.andDelStatusEqualTo((byte) Constants.ColumnDelStatus.common.v());
			criteria.andParentIdEqualTo(parentId);
			criteria.andUserIdEqualTo((long) Constants.Ids.jinTN.v());
			List<Column> columnList = columnMapper.selectByExample(example);
			for (Column column : columnList) {
				vo = new ColumnVO();
				vo.setId(column.getId());
				vo.setColumnname(column.getColumnname());
				// 0 可见 1 不可见
				vo.setState(0);
				list.add(vo);
			}
			result.put("columnList", list);
			result.put(Constants.status, Constants.ResultType.success.v());
		} else {
			// 查询定制表
			List<ColumnVisible> columnList = queryListByPidAndUserId(user.getId(), parentId);
			for (ColumnVisible column : columnList) {
				vo = new ColumnVO();
				vo.setId(column.getColumnId());
				vo.setColumnname(column.getColumnName());
				// 0 可见 1 不可见
				vo.setState(column.getState());
				list.add(vo);
			}
			result.put("columnList", list);
			result.put(Constants.status, Constants.ResultType.success.v());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ginkgocap.ywxt.knowledge.service.ColumnVisibleService#countVisible
	 * (long) Administrator
	 */
	@Override
	public int countUserVisible(long userId) {
		List<Long> columnIds = columnService.getSystemColumnIds(0);
		int count = columnVisibleValueMapper.countUserIdSystemColumn(columnIds, userId);
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ginkgocap.ywxt.knowledge.service.ColumnVisibleService#
	 * deleteUserVisibleColumn(long) Administrator
	 */
	@Override
	public int deleteUserVisibleColumn(long userId) {
		ColumnVisibleExample example = new ColumnVisibleExample();
		com.ginkgocap.ywxt.knowledge.entity.ColumnVisibleExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		return columnVisibleMapper.deleteByExample(example);
	}
}
