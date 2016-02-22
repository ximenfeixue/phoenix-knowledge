package com.ginkgocap.ywxt.knowledge.dao;

import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.model.ColumnCustom;
import com.ginkgocap.ywxt.user.model.User;

/**
 * @Title: 用户定制栏目表
 * @Description: 存储用户定制栏目，用户定制栏目栏目为用户选择哪些栏目显示以及栏目排序
 * @author 周仕奇
 * @date 2016年1月11日 下午2:31:19
 * @version V1.0.0
 */
public interface IColumnCustomDao {
	public void init(long userid, long gtnid);

	/**
	 * 根据用户id和栏目id查询
	 * @param userid
	 * @param cid
	 * @return
	 */
    public ColumnCustom queryListByCidAndUserId(long userid, long cid);

    /**
     * 根据用户id和上级栏目id查询
     * @param userid
     * @param pid
     * @return
     */
    public List<ColumnCustom> queryListByPidAndUserId(long userid, long pid);

    /**
     * 根据用户id和上级栏目id查询栏目数量
     * @param userid
     * @param pid
     * @return
     */
    public long countListByPidAndUserId(long userid, Long pid);

    /**
     * 根据用户id和栏目id删除
     * @param userid
     * @param cid
     */
    public void delByUserIdAndColumnId(long userid, long cid);

    /**
     * 根据栏目主键删除
     * @param id
     */
    public void del(long id);
    
    /**
     * 更新栏目
     * @param id
     * @param viewStatus 0-可见，1-不可见
     */
    public void updateColumnViewStatus(long id,short viewStatus);

	public Map<String,Object> queryHomeColumn(User user);

	/**
	 * 根据用户id和上级栏目id及可见状态查询
	 * @param userid
	 * @param cid
	 * @param state
	 * @return
	 */
	public List<ColumnCustom> queryListByPidAndUserIdAndState(long userid, long cid, short state);
	
	void update(ColumnCustom columnCustom);
	
}