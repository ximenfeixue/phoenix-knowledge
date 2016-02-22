package com.ginkgocap.ywxt.knowledge.service;

import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.model.ColumnCustom;
import com.ginkgocap.ywxt.user.model.User;

public interface IColumnCustomService {
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

	public Map<String,Object> queryHomeColumn(User user);

	public List<ColumnCustom> queryListByPidAndUserIdAndState(long userid, long cid, short state);

}
