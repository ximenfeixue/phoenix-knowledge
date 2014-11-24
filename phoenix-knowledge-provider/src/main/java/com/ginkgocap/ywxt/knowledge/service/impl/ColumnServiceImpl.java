package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.entity.Column;
import com.ginkgocap.ywxt.knowledge.entity.ColumnExample;
import com.ginkgocap.ywxt.knowledge.entity.ColumnExample.Criteria;
import com.ginkgocap.ywxt.knowledge.entity.ColumnTag;
import com.ginkgocap.ywxt.knowledge.entity.ColumnTagExample;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnMapper;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnMapperManual;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnTagMapper;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnTagMapperManual;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnVOValueMapper;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnValueMapper;
import com.ginkgocap.ywxt.knowledge.model.ColumnVO;
import com.ginkgocap.ywxt.knowledge.service.ColumnService;
import com.ginkgocap.ywxt.knowledge.service.ColumnVisibleService;
import com.ginkgocap.ywxt.knowledge.service.DataCenterService;
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

	private Logger logger = LoggerFactory.getLogger(ColumnServiceImpl.class);

	@Autowired
	ColumnMapper columnMapper;
	@Autowired
	private ColumnValueMapper columnValueMapper;
	@Autowired
	private ColumnVOValueMapper columnVOValueMapper;
	@Autowired
	private ColumnVisibleService columnVisibleService;
	@Autowired
	private ColumnTagMapperManual columnTagMapperManual;
	@Autowired
	private ColumnMapperManual columnMapperManual;
	@Autowired
	private ColumnTagMapper columnTagMapper;
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private DataCenterService dataCenterService;

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

		if (null == kc) return null;
		// 获取栏目id
		Long id = kc.getId();
		// 获取当前时间
		Date date = new Date();
		// 父级id
		Long pid = kc.getParentId();
		// 检查上级栏目id
		if (pid == null || pid < 0)		return null;
		// 检测必要信息
		if (!checkColumnByParam(kc.getColumnname(), pid,
				kc.getUserId(), id)) {
			logger.error("新增或修改栏目时,检测重复报错！[缺少必要的参数如：userid,parentid,columnname]");
			return null;
		}
		// id不存在，则表示新增栏目
		if (null == id || id <= 0) {
			// 添加一级目录默认为资讯type为1
			if(pid == 0) {
				kc.setType((short)1);
			} // 设置type为父级type 
			else {
				logger.info("--进入根据父级id查询栏目树请求,父级栏目id:{},当前登陆用户:{}--", pid, kc.getUserId());
				Column column = columnMapper.selectByPrimaryKey(pid);
				logger.info("--根据父级id查询栏目树请求成功,父级栏目id:{},当前登陆用户:{}--", pid, kc.getUserId());
				kc.setType(column.getType());
			}
			kc.setCreatetime(date);
			kc.setUpdateTime(date);
			kc.setDelStatus((byte) 0);
			kc.setSubscribeCount(0l);
			// 插入数据之前必须有columnpath
			String currentColumnLevelPath = getColumnLevelPath(pid);
			// 设置columnLevelPath
			kc.setColumnLevelPath(currentColumnLevelPath);

			columnMapper.insert(kc);
			// 新增栏目通知大数据
			try {
				logger.info("--进入添加栏目请求时通知大数据,方法名：saveOrUpdate,栏目名称:{},当前登陆用户:{}--", kc.getColumnname(), kc.getUserId());
				dataCenterService.noticeDataCenterWhileColumnChange(kc.getId());
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("--进入添加栏目请求时通知大数据异常,方法名：saveOrUpdate,栏目名称:{},当前登陆用户:{}--", kc.getColumnname(), kc.getUserId());
			}
			ColumnExample ce = new ColumnExample();
			filterDel(ce.createCriteria().andUpdateTimeEqualTo(date)
					.andCreatetimeEqualTo(date));
			kc = columnMapper.selectByExample(ce).get(0);
			columnVisibleService.saveCid(kc.getUserId(), kc.getId());
			return kc;
		}else if (id > 0) {
			kc.setUpdateTime(date);
			columnMapper.updateByPrimaryKey(kc);
			columnVisibleService.saveOrUpdate(kc);
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
	public Map<String, Object> selectColumnByPid(long userId, long pid) {
		logger.info("--进入根据父级id查询栏目树请求,父级栏目id:{},当前登陆用户:{}--", pid, userId);
		Map<String, Object> result = new HashMap<String, Object>();
		// 根据父级id查询栏目
		List<ColumnVO> cl = columnVOValueMapper.selectColumnByPid(userId, pid);

		logger.info("--根据父级id查询栏目树请求成功,父级栏目id:{},当前登陆用户:{}--", pid, userId);
		result.put("list", cl);
		return result;
	}

	@Override
	public String selectColumnTreeByParams(long userId, String sortId,
			String status, String columnType) {
		List<Column> cl = columnValueMapper.selectColumnTreeBySortId(userId,
				sortId);
		if (cl != null && cl.size() > 0) {
			JSONObject jo = JSONObject.fromObject(Tree.build(ConvertUtil
					.convert2Node(cl, "userId", "id", "columnname", "parentId",
							"columnLevelPath")));
			JSONArray ja = jo.getJSONArray("list");
			for (int i = 0; i < ja.size(); i++) {
				JSONObject joi = ja.getJSONObject(i);
				if (columnType.equals(joi.getString("id"))) {
					return joi.getJSONArray("list").toString();
				}
			}
		}
		return "";
	}

	@Override
	public String selectColumnTreeByParamsCustom(long userId, String sortId) {

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
		ColumnExample example = new ColumnExample();
		Criteria criteria = example.createCriteria();
		criteria.andColumnnameEqualTo(columnName);
		if (parentColumnId != -1) {
			criteria.andParentIdEqualTo(parentColumnId);
		}

		return columnMapper.countByExample(example) > 0 ? true : false;
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

	public int countByParentId(long parentId, long userId) {
		ColumnExample ce = new ColumnExample();
		Criteria c = ce.createCriteria().andParentIdEqualTo(parentId)
				.andUserIdEqualTo(userId);
		filterDel(c);
		ce.setOrderByClause("id ASC");
		int count = columnMapper.countByExample(ce);
		return count;
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
	public List<Column> queryByUserId(long createUserId, long parentid) {

		ColumnExample example = new ColumnExample();

		Criteria criteria = example.createCriteria();

		criteria.andUserIdEqualTo(createUserId);
		criteria.andParentIdEqualTo(parentid);

		return columnMapper.selectByExample(example);
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
		
		logger.info("--进入添加栏目请求,栏目名称:{},当前登陆用户:{}--", columnname, userid);
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
		// 设置类型
		column.setType((short) type);
		long v = columnMapperManual.insertAndGetId(column);
		if (v == 0) {
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.addColumnFail.c());
			return result;
		}
		long currentColumnId = column.getId();
		
		logger.info("--进入添加栏目请求时存储栏目标签信息,栏目名称:{},当前登陆用户:{}--", columnname, userid);
		// 存储栏目标签信息
		batchSaveColumnTags(userid, currentColumnId, tags,columnname,currentColumnLevelPath);

		batchSaveColumnTags(userid, currentColumnId, tags, columnname,
				currentColumnLevelPath);

		logger.info("--结束添加栏目请求时存储栏目标签信息,栏目名称:{},当前登陆用户:{}--", columnname, userid);
		columnVisibleService.saveCid(userid, currentColumnId);
		
		// 新增栏目通知大数据
		try {
			logger.info("--进入添加栏目请求时通知大数据,栏目名称:{},当前登陆用户:{}--", columnname, userid);
			dataCenterService.noticeDataCenterWhileColumnChange(currentColumnId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("--进入添加栏目请求时通知大数据异常,栏目名称:{},当前登陆用户:{}--", columnname, userid);
		}
		result.put(Constants.status, Constants.ResultType.success.v());
		result.put("id", currentColumnId);
		return result;
	}

	public long addColumnForNongroup(String columnname, long pid,
			String pathName, int type, String tags, long userid) {

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
		column.setColumnLevelPath("111111111");
		// 设置类型
		column.setType((short) type);
		columnMapperManual.insertAndGetId(column);
		long currentColumnId = column.getId();
		columnVisibleService.saveCid(userid, currentColumnId);
		return currentColumnId;
	}

	/**
	 * 存储栏目标签
	 * 
	 * @param userid
	 * @param columnId
	 * @param tags
	 * @param columnName
	 * @param columnLevelPath
	 */
	public void batchSaveColumnTags(long userid, long columnId, String tags,
			String columnName, String columnLevelPath) {
		TagUtils tagUtil = new TagUtils();
		String[] currTags = tagUtil.getTagListByTags(tags);
		List<ColumnTag> columnList = new ArrayList<ColumnTag>();
		if (currTags != null) {
			for (String tag : currTags) {
				ColumnTag ct = new ColumnTag();
				ct.setColumnId(columnId);
				ct.setCreatetime(new Date());
				ct.setTag(tag);
				ct.setUserId(userid);
				ct.setColumnname(columnName);
				ct.setColumnPath(columnLevelPath);
				columnList.add(ct);
			}
		}
		if (columnList.size() > 0) {
			columnTagMapperManual.batchInsertColumnTag(columnList);
		}
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
	public Map<String, Object> delColumn(long columnid, long userid,
			boolean verify) {
		logger.info("--进入删除栏目请求,栏目id:{},当前登陆用户:{}--", columnid, userid);
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

		// 更改mongo中知识所属栏目ID为未分组
		ColumnExample col = new ColumnExample();
		Criteria criteria = col.createCriteria();
		criteria.andColumnLevelPathEqualTo(Constants.unGroupSortId);
		criteria.andUserIdEqualTo(userid);
		logger.info("--进入删除栏目请求时获取未分组栏目,栏目id:{},当前登陆用户:{}--", columnid, userid);
		// 获取未分组栏目
		List<Column> colList = columnMapper.selectByExample(col);
		if (colList != null && colList.size() > 0) {
			Query query = new Query(
					org.springframework.data.mongodb.core.query.Criteria.where(
							"columnid").is(String.valueOf(columnid)));
			String obj = Constants.getTableName(column.getType() + "");
			obj = obj.substring(obj.lastIndexOf(".") + 1, obj.length());
			// 是否确认操作
			if (!verify) {
				long count = mongoTemplate.count(query, obj);
				if (count > 0)
					result.put("has", true);
				else
					result.put("has", false);
				return result;
			}
			Update update = new Update();
			update.set("columnid", String.valueOf(colList.get(0).getId()));
			update.set("cpathid", Constants.unGroupSortName);
			logger.info("--进入删除栏目请求时,将栏目分录下文章归到未分组目录,栏目id:{},当前登陆用户:{}--", columnid, userid);
			mongoTemplate.updateMulti(query, update, obj);
			logger.info("--完成删除栏目请求时,将栏目分录下文章归到未分组目录,栏目id:{},当前登陆用户:{}--", columnid, userid);
		}
		// 删除栏目标签表
		ColumnTagExample example = new ColumnTagExample();
		com.ginkgocap.ywxt.knowledge.entity.ColumnTagExample.Criteria critera = example
				.createCriteria();
		critera.andColumnIdEqualTo(columnid);
		logger.info("--进入删除栏目请求时,删除相应栏目标签,栏目id:{},当前登陆用户:{}--", columnid, userid);
		columnTagMapper.deleteByExample(example);
		logger.info("--结束删除栏目请求时,删除相应栏目标签,栏目id:{},当前登陆用户:{}--", columnid, userid);
		// 删除栏目定制表
		columnVisibleService.delByUserIdAndColumnId(userid, columnid);

		// 删除栏目表
		int v = columnMapper.deleteByPrimaryKey(columnid);
		logger.info("--结束删除栏目请求,栏目id:{},当前登陆用户:{}--", columnid, userid);
		if (v == 0) {
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.delFail.c());
			return result;
		}
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
			this.addColumnForNongroup("未分组", 0, "未分组", 1, "", uid);
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
		c.andParentIdEqualTo(0l);
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
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	@Override
	public Column getUnGroupColumnIdBySortId(long userId) {

		return getColumnIdBySortId(Constants.unGroupSortId, userId);
	}

	@Override
	public Map<String, Object> updateColumn(long id, String columnName,
			String tags, long userId) {
		Map<String, Object> result = new HashMap<String, Object>();
		Column column = columnMapper.selectByPrimaryKey(id);
		if (column == null) {
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.notFindColumn.c());
			return result;
		}
		if (column.getUserId() != userId) {
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.delColumnNotPermission.c());
			return result;
		}
		column.setColumnname(columnName);
		column.setUpdateTime(new Date());
		int v = columnMapper.updateByPrimaryKeySelective(column);
		if (v == 0) {
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.updateFail.c());
			return result;
		}
		// 修改Mongod栏目路径

		// 删除栏目标签信息
		ColumnTagExample example = new ColumnTagExample();
		com.ginkgocap.ywxt.knowledge.entity.ColumnTagExample.Criteria criteria = example
				.createCriteria();
		criteria.andColumnIdEqualTo(id);
		int ctV = columnTagMapper.deleteByExample(example);
		if (ctV == 0) {
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.updateFail.c());
			return result;
		}
		batchSaveColumnTags(userId, id, tags, column.getColumnname(),
				column.getColumnLevelPath());
		result.put(Constants.status, Constants.ResultType.success.v());

		return result;
	}

	@Override
	public Map<String, Object> queryOne(long id) {
		Map<String, Object> result = new HashMap<String, Object>();
		Column column = columnMapper.selectByPrimaryKey(id);
		ColumnTagExample example = new ColumnTagExample();
		com.ginkgocap.ywxt.knowledge.entity.ColumnTagExample.Criteria criteria = example
				.createCriteria();
		criteria.andColumnIdEqualTo(id);
		List<ColumnTag> ct = columnTagMapper.selectByExample(example);
		String tags = "";
		if (ct != null && ct.size() > 0) {
			StringBuffer buffer = new StringBuffer();
			for (ColumnTag tag : ct) {
				buffer.append(tag.getTag());
				buffer.append(",");
			}
			buffer.deleteCharAt(buffer.length() - 1);
			tags = buffer.toString();
		}
		result.put(Constants.status, Constants.ResultType.success.v());
		result.put("tags", tags);
		result.put("column", column);

		return result;
	}

	@Override
	public Column selectByPrimaryKey(long columnid) {

		return columnMapper.selectByPrimaryKey(columnid);
	}

	@Override
	public Map<String, Object> selectparColumnidBycolumnid(long columnid) {

		Map<String, Object> map = new HashMap<String, Object>();
		Column column = null;
		String parcolumnid = "";
		if (columnid != 0) {

			try {
				column = columnMapper.selectByPrimaryKey(columnid);
				map.put("columnid", columnid);
				map.put("columnName", column.getColumnname());

			} catch (Exception e) {
				logger.error("栏目名称不存在,栏目ID{}", columnid);
				e.printStackTrace();
			}
		}
		for (int i = 0; i < 5; i++) {
			if (i == 0) {
				if (column.getParentId() == 0) {
					parcolumnid = column.getId() + "";
					map.put("parcolumnid", -1);
					map.put("parcolumnName", "自义定栏目");
					break;
				}
			} else {
				if (column.getParentId() == 0) {
					parcolumnid = column.getId() + "";
					map.put("parcolumnid", parcolumnid);
					if (StringUtils.isNotBlank(parcolumnid)) {
						column = columnMapper.selectByPrimaryKey(Long
								.parseLong(parcolumnid));
						map.put("parcolumnName", column.getColumnname());
					}
					break;
				}
			}
			column = columnMapper.selectByPrimaryKey(column.getParentId());
		}

		return map;
	}

	@Override
	public List<Column> querySubByUserIdOrderById(long createUserId) {
		List<Column> list = columnValueMapper
				.selectSubByUserIdOrderById(createUserId);
		return list;
	}

	@Override
	public boolean checkColumnByParams(String columnName, long parentId,
			long uid) {
		logger.info("--进入验证栏目名称请求,栏目名称:{},当前登陆用户:{}--", columnName, uid);
		ColumnExample example = new ColumnExample();
		Criteria c = example.createCriteria();
		List<Long> values = new ArrayList<Long>();
		values.add(0L);
		values.add(uid);
		c.andUserIdIn(values);
		if (columnName != null && !"".equals(columnName)) {
			c.andColumnnameEqualTo(columnName);
		}
		c.andParentIdEqualTo(parentId);
		int count = columnMapper.countByExample(example);
		logger.info("--验证栏目名称请求成功,栏目名称:{},当前登陆用户:{}--", columnName, uid);
		return count > 0 ? false : true;
	}

	private boolean checkColumnByParam(String columnName, Long parentId,
			Long uid, Long id) {
		ColumnExample example = new ColumnExample();
		Criteria c = example.createCriteria();
		List<Long> values = new ArrayList<Long>();
		values.add(0L);
		values.add(uid);
		c.andUserIdIn(values);
		if (id != null) {
			c.andIdNotEqualTo(id);
		}
		if (columnName != null && !"".equals(columnName)) {
			c.andColumnnameEqualTo(columnName);
		}
		c.andParentIdEqualTo(parentId);
		int count = columnMapper.countByExample(example);
		return count > 0 ? false : true;
	}

}
