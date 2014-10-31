package com.ginkgocap.ywxt.knowledge.service;

import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.entity.Column;

/**
 * <p>
 * 知识栏目操作接口
 * </p>
 * <p>
 * 于2014-8-19 由 bianzhiwei 创建
 * </p>
 * 
 * @author <p>
 *         当前负责人 guangyuan
 *         </p>
 * @since <p>
 *        1.2.1-SNAPSHOT
 *        </p>
 */
public interface ColumnService {

	/**
	 * saveOrUpdate
	 * <p>
	 * (保存知识栏目)
	 * </p>
	 * 
	 * @param kc
	 * @return Column
	 */
	public Column saveOrUpdate(Column kc);

	/**
	 * queryById
	 * <p>
	 * (查询栏目)
	 * </p>
	 * 
	 * @param id
	 *            id
	 * @return 知识栏目
	 */
	public Column queryById(long id);

	/**
	 * isExist
	 * <p>
	 * (是否存在)
	 * </p>
	 * 
	 * @param parentColumnId
	 *            上级栏目id
	 * @param columnName
	 *            栏目名称
	 * @return true:存在 false:不存在
	 */
	public boolean isExist(long parentColumnId, String columnName);

	/**
	 * delById
	 * <p>
	 * (删除栏目)
	 * </p>
	 * 
	 * @param id
	 */
	public void delById(long id);

	/**
	 * queryByParentId根据上级栏目查询子栏目
	 * 
	 * @param parentColumnId
	 *            父栏目id
	 * @param createUserId
	 *            创建者名称
	 * @return 栏目列表
	 */
	public List<Column> queryByParentId(long parentId, long userId);

	/**
	 * queryByUserId 查询用户创建的所有栏目
	 * 
	 * @param createUserId
	 *            用户id
	 * @return 用户创建的所有栏目列表
	 */
	public List<Column> queryByUserId(long userId);

	/**
	 * 只能用来查询用户的自定义的二级栏目，不能查询一级栏目
	 * 
	 * @param parentColumnId
	 *            父栏目id
	 * @param createUserId
	 *            创建者名称
	 * @return 栏目列表
	 */
	public List<Column> queryByParentIdAndSystem(long parentId, long userId);

	/**
	 * queryByUserId 查询用户订阅的所有栏目
	 * 
	 * @param createUserId
	 *            用户id
	 * @return 用户订阅的所有栏目列表
	 */
	public List<Column> querySubByUserId(long createUserId);

	/**
	 * queryByUserId 查询所有可订阅栏目和该用户订阅状态 map.get("column") 返回column对象
	 * map.get("status") 返回订阅状态，boolean值，true为已订阅
	 * 
	 * @param createUserId
	 *            用户id
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> querySubAndStatus(long createUserId);

	/**
	 * queryByUserId 查询系统所有可订阅的栏目
	 * 
	 * @param systemId
	 *            系统用户id
	 * @return 系统栏目列表
	 */
	public List<Column> querySubBySystem(long systemId);

	/**
	 * queryByUserId 查询用户创建的所有栏目和系统栏目
	 * 
	 * @param createUserId
	 *            用户id
	 * @param systemId
	 *            系统用户id
	 * @return 用户创建的所有栏目和系统栏目列表
	 */
	public List<Column> queryByUserIdAndSystem(long createUserId, long systemId);

	/**
	 * 查询该节点所有上级节点，参数对象c必须包含父id
	 * 
	 * @param c
	 * @return 父节点列表，列表的最后一个是根节点
	 */
	public List<Column> selectAncestors(Column c);

	/**
	 * queryAll 查询所有未删除的栏目
	 * 
	 * @return 所有栏目列表
	 */
	public List<Column> queryAll();

	/**
	 * queryAll 查询所有已删除的栏目
	 * 
	 * @return 所有已删除栏目列表
	 */
	public List<Column> queryAllDel();

	/**
	 * recoverOneKC 恢复一个已删除的栏目
	 * 
	 * @return none
	 */
	public void recoverOneKC(Long id);

	/**
	 * clearById
	 * <p>
	 * (删除栏目, 物理删除 ,数据库中将清除此条数据，慎用！！！)
	 * </p>
	 * <p>
	 * 只有已删除状态的数据才能被清空
	 * </p>
	 * 
	 * @param id
	 */
	public void clearById(long id);

	/**
	 * 查询目录树
	 * 
	 * @param userId
	 *            用户id
	 * @param sortId
	 *            排序id :为空表示所有
	 * @param status
	 *            状态:0正常 1删除
	 * @return
	 */
	String selectColumnTreeBySortId(long userId, String sortId, String status);

	/**
	 * 获取全路径
	 * 
	 * @param id
	 *            栏目id
	 * @return
	 */
	List<Column> selectFullPath(long id);

	/**
	 * 查询订阅数据
	 * 
	 * @param userId
	 * @return
	 */
	public Map<Column, List<Map<String, Object>>> querySubAndStatusAndParent(
			long userId);

	/**
	 * 查询一级自定义栏目
	 * 
	 * @param userId
	 * @return
	 */
	public List<Column> queryFisrtLevelCustomerColumns(long userId);

	/**
	 * 添加栏目
	 * @param columnname
	 *            栏目名称
	 * @param pid
	 *            父ID
	 * @param pathName
	 *            栏目路径
	 * @param type
	 *            栏目所属类型
	 * @param tags
	 *            栏目标签
	 * @param userid
	 *            登陆用户ID
	 */
	public Map<String, Object> addColumn(String columnname, long pid,
			String pathName, int type, String tags, long userid);

	/**
	 * 删除栏目
	 * 
	 * @param columnid
	 * @param userid
	 * @return
	 */
	public Map<String, Object> delColumn(long columnid, long id);

	/**
	 * 初始化未分组
	 * 
	 * @param uid
	 *            用户id
	 */
	void checkNogroup(Long uid);

	/**
	 * 根据栏目ID查询栏目路径
	 * 
	 * @param columnid
	 * @return
	 */
	public String getColumnPathById(long columnid);

	/**
	 * 根据sortId,userId查询columnId
	 * 
	 * @param userId
	 *            用户Id　
	 * @return
	 */
	public Column getColumnIdBySortId(String sortId, long id);

	/**
	 * 根据userId查询未分组columnId
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	Column getUnGroupColumnIdBySortId(long userId);
	
    String selectColumnTreeByParams(long userId, String sortId, String status, String type);
    
    /**
     * 
     */
    String selectColumnTreeByParamsCustom(long userId, String sortId);
	/**
	 * 修改栏目  
	 * @param id 栏目ID
	 * @param columnName 栏目名称 
	 * @param tags 栏目标签 
	 * @param userId 用户ID
	 * @return
	 */
	public Map<String, Object> updateColumn(long id, String columnName,
			String tags, long userId);

}
