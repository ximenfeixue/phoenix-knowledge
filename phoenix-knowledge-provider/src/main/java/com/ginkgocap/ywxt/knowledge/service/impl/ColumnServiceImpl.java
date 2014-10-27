package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.entity.Column;
import com.ginkgocap.ywxt.knowledge.entity.ColumnExample;
import com.ginkgocap.ywxt.knowledge.entity.ColumnTagExample;
import com.ginkgocap.ywxt.knowledge.entity.UserCategory;
import com.ginkgocap.ywxt.knowledge.entity.UserCategoryExample;
import com.ginkgocap.ywxt.knowledge.entity.ColumnExample.Criteria;
import com.ginkgocap.ywxt.knowledge.entity.ColumnTag;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnMapper;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnMapperManual;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnTagMapper;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnTagMapperManual;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnValueMapper;
import com.ginkgocap.ywxt.knowledge.service.ColumnService;
import com.ginkgocap.ywxt.knowledge.service.ColumnVisibleService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.knowledge.util.KCHelper;
import com.ginkgocap.ywxt.knowledge.util.TagUtils;
import com.ginkgocap.ywxt.knowledge.util.tree.ConvertUtil;
import com.ginkgocap.ywxt.knowledge.util.tree.Tree;

@Service("columnService")
public class ColumnServiceImpl implements ColumnService {

	public static int NO_DEL_VALUE = 0;
	public static int ROOT_PARENT_ID = 0;
	public static int MAX_ALLOWED_LEVEL = 7;
	private static final int sortV = 9;

	@Autowired
	ColumnMapper columnMapper;
	@Autowired
	private ColumnValueMapper columnValueMapper;
	@Autowired
	private ColumnVisibleService columnVisibleService;
	@Autowired
	private ColumnTagMapperManual columnTagMapperManual;
	@Autowired
	private ColumnMapperManual columnMapperManual;
	@Autowired
	private ColumnTagMapper columnTagMapper;

	/**
	 * 在查询条件中增加delstatus条件，过滤掉已删除对象
	 * 
	 * @param c
	 * @return
	 */
	public Criteria filterDel(Criteria c) {
		c.andDelStatusEqualTo(Byte.valueOf(NO_DEL_VALUE + ""));
		return c;
	}

	@Override
	public List<Column> selectAncestors(Column c) {

		List<Column> list = new ArrayList<Column>(MAX_ALLOWED_LEVEL);
		list.add(c);

		if (c.getParentId().intValue() == ROOT_PARENT_ID) {
			return list;
		}

		Column cs = c;
		while (cs.getParentId().intValue() > ROOT_PARENT_ID) {
			cs = columnMapper.selectByPrimaryKey(cs.getParentId());
			list.add(cs);
		}
		return list;
	}

	@Override
	public Column saveOrUpdate(Column kc) {

		if (null == kc) {
			return null;
		}

		Long id = kc.getId();
		Date date = new Date();

		if (null == id || id.intValue() <= 0) {

			if (kc.getParentId() == null || kc.getParentId() < 0) {
				return null;
			}
			kc.setCreatetime(date);
			kc.setUpdateTime(date);
			kc.setDelStatus((byte) 0);
			kc.setSubscribeCount(0l);

			// 参考UserCategoryServiceImpl.insert 生成columnlevelpath
			// 系统栏目的USERID应为金桐脑的id

			// 插入数据之前必须有columnpath

			List<Column> ancestors = selectAncestors(kc);

			List<Long> pids = new ArrayList<Long>();
			for (int i = 0; i < ancestors.size(); i++) {
				pids.add(ancestors.get(i).getParentId());
			}

			Long pathId = columnValueMapper.selectMaxID() + 1;

			String sort = KCHelper.getSortPath(pids, pathId);

			kc.setColumnLevelPath(sort);

			columnMapper.insert(kc);

			ColumnExample ce = new ColumnExample();
			filterDel(ce.createCriteria().andUpdateTimeEqualTo(date)
					.andCreatetimeEqualTo(date));
			kc = columnMapper.selectByExample(ce).get(0);
			columnVisibleService.saveCid(kc.getUserId(), kc.getId());
			return kc;
		}

		if (id > 0) {
			// Column okc= ColumnDao.queryById(kc.getId());
			//
			// if(null != okc){
			// okc.setColumnLevelPath(kc.getColumnLevelPath());
			// okc.setSubscribeCount(kc.getSubscribeCount());
			// okc.setUpdateTime(date);
			// ColumnDao.update(okc);
			// }

			kc.setUpdateTime(date);
			columnMapper.updateByPrimaryKey(kc);
			return kc;
		}

		return null;
	}

	@Override
	public Column queryById(long id) {
		return columnMapper.selectByPrimaryKey(id);
	}

	@Override
	public String selectColumnTreeBySortId(long userId, String sortId,
			String status) {
		List<Column> cl = columnValueMapper.selectColumnTreeBySortId(userId,
				sortId);
		if (cl != null && cl.size() > 0) {
			return JSONObject.fromObject(
					Tree.build(ConvertUtil.convert2Node(cl, "userId", "id",
							"columnname", "parentId", "columnLevelPath")))
					.toString();
		}
		return "";
	}

	@Override
	public List<Column> selectFullPath(long id) {
		List<Column> l = new ArrayList<Column>();
		Column k = this.queryById(id);
		if (k != null) {
			l.add(k);
			getFullPath(l, k.getParentId());
			Collections.reverse(l);
		}
		return l;
	}

	public void getFullPath(List<Column> l, long pid) {
		if (pid == 0) {
			return;
		}
		Column kn = this.queryById(pid);
		l.add(kn);
	}

	@Override
	public boolean isExist(long parentColumnId, String columnName) {
		ColumnExample ce = new ColumnExample();

		ce.createCriteria().andParentIdEqualTo(parentColumnId)
				.andColumnnameEqualTo(columnName);

		return columnMapper.countByExample(ce) > 0 ? true : false;
	}

	@Override
	public List<Column> querySubByUserId(long createUserId) {
		List<Column> list = columnValueMapper.selectSubByUserId(createUserId);
		return list;
	}

	@Override
	public List<Column> queryByUserIdAndSystem(long createUserId, long systemId) {
		List<Long> values = new ArrayList<Long>();
		values.add(createUserId);
		values.add(systemId);

		ColumnExample ce = new ColumnExample();
		Criteria c = ce.createCriteria().andUserIdIn(values);
		filterDel(c);

		// 排序 column_level_path
		ce.setOrderByClause("id ASC");

		List<Column> list = columnMapper.selectByExample(ce);
		return list;
	}

	@Override
	public void delById(long id) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<Column> queryByParentId(long parentId, long userId) {
		ColumnExample ce = new ColumnExample();
		Criteria c = ce.createCriteria().andParentIdEqualTo(parentId)
				.andUserIdEqualTo(userId);
		filterDel(c);
		ce.setOrderByClause("id ASC");
		List<Column> list = columnMapper.selectByExample(ce);
		return list;
	}

	@Override
	public List<Column> queryByParentIdAndSystem(long parentId, long userId) {
		List<Long> values = new ArrayList<Long>();
		values.add(userId);
		values.add(Constants.gtnid);

		ColumnExample ce = new ColumnExample();
		Criteria c = ce.createCriteria().andParentIdEqualTo(parentId)
				.andUserIdIn(values);
		filterDel(c);

		// 排序 column_level_path
		ce.setOrderByClause("id ASC");

		List<Column> list = columnMapper.selectByExample(ce);
		return list;
	}

	@Override
	public List<Map<String, Object>> querySubAndStatus(long userId) {
		//
		List<Column> list = this.querySubBySystem(Constants.gtnid);

		List<Column> subList = this.querySubByUserId(userId);
		List<Long> subListId = new ArrayList<Long>();
		for (int i = 0; i < subList.size(); i++) {
			subListId.add(subList.get(i).getId());
		}

		List<Map<String, Object>> ss = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			Long id = list.get(i).getId();

			Map<String, Object> map = new HashMap<String, Object>();

			map.put("column", list.get(i));

			if (subListId.contains(id)) {
				map.put("status", true);
			} else {
				map.put("status", false);
			}

			ss.add(map);
		}
		return ss;
	}

	@Override
	public Map<Column, List<Map<String, Object>>> querySubAndStatusAndParent(
			long userId) {

		List<Map<String, Object>> ss = this.querySubAndStatus(userId);

		List<Column> zz = this.queryByParentId(0, Constants.gtnid);

		Map<Column, List<Map<String, Object>>> complexx = new HashMap<Column, List<Map<String, Object>>>();

		for (int j = 0; j < zz.size(); j++) {
			Column c = zz.get(j);
			List<Map<String, Object>> headachee = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < ss.size(); i++) {
				Column cc = (Column) ss.get(i).get("column");
				if (cc.getParentId().equals(c.getId())) {
					headachee.add(ss.get(i));
				}
			}

			complexx.put(c, headachee);
		}

		return complexx;
	}

	@Override
	public List<Column> queryByUserId(long createUserId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Column> querySubBySystem(long systemId) {

		List<Column> list = columnValueMapper.selectSubC(systemId);

		return list;
	}

	@Override
	public List<Column> queryAll() {
		ColumnExample ce = new ColumnExample();
		Criteria c = ce.createCriteria();
		filterDel(c);
		List<Column> list = columnMapper.selectByExample(ce);
		return list;
	}

	@Override
	public List<Column> queryAllDel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void recoverOneKC(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearById(long id) {
		// TODO Auto-generated method stub
	}

	// @Override
	// public boolean isExist(int parentColumnId, String columnName) {
	// if (ColumnDao.countByPidAndName(parentColumnId, columnName) > 0) {
	// return true;
	// }
	// return false;
	// }
	//
	// @Override
	// public void delById(long id) {
	// ColumnDao.delById(id);
	// }
	//
	// @Override
	// public List<Column> queryByParentId(int parentColumnId, int createUserId)
	// {
	// return ColumnDao.queryByParentId(parentColumnId, createUserId);
	// }
	//
	// @Override
	// public List<Column> queryByUserId(long createUserId) {
	// return ColumnDao.queryByUserId(createUserId);
	// }
	//
	// @Override
	// public List<Column> queryByUserIdAndSystem(long createUserId, long
	// systemId) {
	// return ColumnDao.queryByUserIdAndSystem(createUserId, systemId);
	// }
	//
	// @Override
	// public List<Column> queryAll() {
	// return ColumnDao.queryAll();
	// }
	//
	// @Override
	// public List<Column> queryAllDel() {
	// return ColumnDao.queryAllDel();
	// }
	//
	// @Override
	// public void recoverOneKC(Long id) {
	// if (null == id || id.longValue() < 0) {
	// return;
	// }
	//
	// ColumnDao.recoverOneKC(id);
	// }
	//
	// @Override
	// public void clearById(long id) {
	// ColumnDao.clearById(id);
	// }
	//
	// @Override
	// public List<Column> querySubByUserId(long createUserId) {
	// return ColumnDao.querySubByUserId(createUserId);
	// }

	@Override
	public List<Column> queryFisrtLevelCustomerColumns(long userId) {
		ColumnExample ce = new ColumnExample();
		Criteria c = ce.createCriteria();
		c.andUserIdEqualTo(userId);
		filterDel(c);
		List<Column> list = columnMapper.selectByExample(ce);
		return list;
	}

	@Override
	public Map<String, Object> addColumn(String columnname, long pid,
			String pathName, int type, String tags, long userid) {
		Map<String, Object> result = new HashMap<String, Object>();
		String currentColumnLevelPath = getColumnLevelPath(pid);
		if (StringUtils.isBlank(currentColumnLevelPath)) {
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.notFindColumn.c());
			return result;
		}
		// 存储栏目信息
		Date d = new Date();
		Column column = new Column();
		column.setColumnname(columnname);
		column.setParentId(pid);
		column.setCreatetime(d);
		column.setDelStatus((byte) Constants.ColumnDelStatus.common.v());
		column.setSubscribeCount(0l);
		column.setPathName(pathName);
		column.setUpdateTime(d);
		column.setUserId(userid);
		column.setColumnLevelPath(currentColumnLevelPath);

		if (type != 0) {
			// column.setType((byte) type);
		}
		long v = columnMapperManual.insertAndGetId(column);
		if (v == 0) {
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.addColumnFail.c());
			return result;
		}
		long currentColumnId = column.getId();
		// 存储栏目标签信息
		TagUtils tagUtil = new TagUtils();
		String[] currTags = tagUtil.getTagListByTags(tags);
		List<ColumnTag> columnList = new ArrayList<ColumnTag>();
		for (String tag : currTags) {
			ColumnTag ct = new ColumnTag();
			ct.setColumnId(currentColumnId);
			ct.setCreatetime(d);
			ct.setTag(tag);
			ct.setUserId(userid);
			columnList.add(ct);
		}
		columnTagMapperManual.batchInsertColumnTag(columnList);

		columnVisibleService.saveCid(userid, currentColumnId);

		result.put(Constants.status, Constants.ResultType.success.v());

		return result;
	}

	/** 获取sortId全路径 **/
	private String getColumnLevelPath(long pid) {
		String currentColumn = null;
		if (pid != 0) {
			Column column = columnMapper.selectByPrimaryKey(pid);
			if (column == null)
				return null;
			currentColumn = column.getColumnLevelPath();
		}

		String maxLevel = getMaxLevelPath(pid);
		if (StringUtils.isBlank(maxLevel)) {
			currentColumn = (currentColumn == null ? "" : currentColumn)
					+ "000000001";
		} else {
			currentColumn = (currentColumn == null ? "" : currentColumn)
					+ getCurrentSortId(maxLevel);
		}

		return currentColumn;
	}

	/** 获取当前sortId值 **/
	public String getCurrentSortId(String v) {
		StringBuffer sb = new StringBuffer();
		v = v.length() > 9 ? v.substring(v.length() - 9, v.length()) : v;
		String currV = String.valueOf(Integer.parseInt(v) + 1);
		for (int z = 0; z < sortV - currV.length(); z++) {
			sb.append("0");
		}
		String currSortId = sb.toString();

		return currSortId.concat(currV);
	}

	/** 获取子栏目下path最大值 **/
	private String getMaxLevelPath(long pid) {
		ColumnExample example = new ColumnExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(pid);
		example.setOrderByClause("column_level_path desc ");

		List<Column> clist = columnMapper.selectByExample(example);
		if (clist == null || clist.size() == 0)
			return null;

		return clist.get(0).getColumnLevelPath();
	}

	@Override
	public Map<String, Object> delColumn(long columnid, long userid) {
		Map<String, Object> result = new HashMap<String, Object>();
		Column column = columnMapper.selectByPrimaryKey(columnid);
		if (column == null) {
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.notFindColumn.c());
			return result;
		}
		if (column.getUserId() != userid) {
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.delColumnNotPermission.c());
			return result;
		}

		// 删除栏目表
		int v = columnMapper.deleteByPrimaryKey(columnid);
		if (v == 0) {
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.delFail.c());
			return result;
		}
		// 删除栏目标签表
		ColumnTagExample example = new ColumnTagExample();
		com.ginkgocap.ywxt.knowledge.entity.ColumnTagExample.Criteria critera = example
				.createCriteria();
		critera.andColumnIdEqualTo(columnid);

		columnTagMapper.deleteByExample(example);

		columnVisibleService.delByUserIdAndColumnId(userid, columnid);

		result.put(Constants.status, Constants.ResultType.success.v());

		return result;
	}

	@Override
	public String getColumnPathById(long columnid) {
		Column column = null;

		if (columnid != 0) {
			column = columnMapper.selectByPrimaryKey(columnid);
			if (column != null) {
				if (StringUtils.isNotBlank(column.getPathName())) {
					return column.getPathName();
				}
			} else {
				return null;
			}
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < 5; i++) {
				if (column == null) {
					break;
				}
				sb.append("/").append(column.getColumnname());
				column = columnMapper.selectByPrimaryKey(columnid);
			}
			return sb.toString();
		}
		return null;
	}

	@Override
	public void checkNogroup(Long uid) {
		List<Column> l = this.selectColumnByParams(uid, "未分组");
		// 初始化未分组
		if (l.size() == 0) {
			this.addColumn("未分组", 0, "未分组", 0, "", uid);
		}
	}

	private List<Column> selectColumnByParams(Long uid, String columnname) {
		ColumnExample example = new ColumnExample();
		Criteria c = example.createCriteria();
		if (uid > 0) {
			c.andUserIdEqualTo(uid);
		}
		if (columnname != null && !"".equals(columnname)) {
			c.andColumnnameEqualTo(columnname);
		}
		List<Column> ll = columnMapper.selectByExample(example);
		return ll;
	}

	@Override
	public Column getColumnIdBySortId(String sortId, long userId) {
		ColumnExample example = new ColumnExample();
		Criteria criteria = example.createCriteria();
		criteria.andColumnLevelPathEqualTo(sortId);
		criteria.andUserIdEqualTo(userId);

		List<Column> list = columnMapper.selectByExample(example);
		if (list == null)
			return null;
		return list.get(0);
	}

	@Override
	public Column getUnGroupColumnIdBySortId(long userId) {
		
		return getColumnIdBySortId(Constants.unGroupSortId, userId);
	}

}
