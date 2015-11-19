package com.ginkgocap.ywxt.knowledge.service;

import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.entity.Column;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeColumnSubscribe;
import com.ginkgocap.ywxt.knowledge.form.KnowledgeSimpleMerge;
import com.ginkgocap.ywxt.knowledge.form.SubcribeNode;

/**
 * 栏目订阅操作接口
 * 
 * @author guangyuan
 * @since 1.2.1-SNAPSHOT
 * @createdtime 2014-09-10
 */
public interface ColumnSubscribeService {

	/**
	 * 增加用户订阅栏目的记录
	 * 
	 * @param kcs
	 *            要增加的记录
	 * @return 增加的记录
	 */
	public KnowledgeColumnSubscribe add(KnowledgeColumnSubscribe kcs);

	/**
	 * 更新订阅数
	 * 
	 * @param columnid
	 *            栏目id
	 * @return
	 */
	long updateSubscribeCount(long columnid);

	/**
	 * add 根据用户id和栏目id查询用户的是否已订阅
	 * 
	 * @param userId
	 *            用户id
	 * @param columnId
	 *            栏目id
	 */
	public boolean isExist(long userId, long columnId);

	/**
	 * 根据用户id和栏目id删除用户订阅栏目的记录
	 * 
	 * @param id
	 *            要删除的的订阅id
	 * @return none
	 */
	public void deleteByUIdAndKCId(long userId, long columnId);

	/**
	 * 查询用户所订阅栏目的记录列表
	 * 
	 * @param userId
	 *            用户id
	 * @return 订阅记录列表
	 */
	public List<KnowledgeColumnSubscribe> selectByUserId(long userId);

	/**
	 * 统计某栏目的被订阅总数
	 * 
	 * @param columnId
	 *            栏目id
	 * @return 统计数字
	 */
	public int countByKC(long columnId);

	/**
	 * 统计某人订阅栏目的总数
	 * 
	 * @param columnId
	 *            栏目id
	 * @return 统计数字
	 */
	public int countByUserId(long userId);

	/**
	 * 订阅排行
	 * 
	 * @param count
	 *            排行数量
	 * @return
	 */
	public Map<String, Object> selectRankList(int count, long userid);

	/**
	 * 订阅所有列表
	 * 
	 * @param userid
	 *            用户id
	 * @param type
	 *            0:所有 1自己
	 * @return
	 */
	public List<SubcribeNode> selectAllList(Long userid, short type);

	/**
	 * 用户是否订阅过该类型
	 * 
	 * @param userId
	 * @param type
	 * @return
	 */
	boolean isExistType(long userId, short type);

	/**
	 * 查询我的订阅
	 * 
	 * @param id
	 *            用户ID
	 * @param type
	 * @param psize
	 * @param pno
	 * @return
	 */
	public Map<String, Object> selectMySubscribe(long id, String type,
			Integer source, Integer pno, Integer psize);

	/**
	 * 我的订阅内部查询
	 * @param userId 用户ID 
	 * @param keywords 关键词
	 * @param type 知识类型
	 * @param source 知识来源
	 * @param currentPage 当前页
	 * @param pageSize 数量
	 * @return
	 */
	public Map<String, Object> searchKnowledgeFromSub(long userId, String keywords,
			String type, Integer source, Integer currentPage, Integer pageSize);
	
	/** 移动端订阅方法 */
	public void mobileSubscribeColumn(int type,long userId,long columnId);
}
